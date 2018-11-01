import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject, Observable, ObjectUnsubscribedError } from 'rxjs';
import { ConfigerRestAPI } from '../configer.restapi';
import { Config, ConfigUser } from '../configer.model';
import { scan, map, publishReplay, refCount } from 'rxjs/operators';
import * as _ from 'lodash';

type ConfigMap = Map<string, Config>;
type IConfigMapOperation = (projects: ConfigMap) => ConfigMap;
interface UpdateConfigField {
    cfgId: number;
    fieldValue: any;
}
interface AddConfig {
    cfgId: number;
    config: Config;
}

type IConfigUsersOperation = (users: ConfigUser[]) => ConfigUser[];
interface AddConfigUser {
    user: ConfigUser;
}

@Injectable()
export class ConfigService {

    // updateConfigs       ──┬──＞ updates ──＞ configMap ──＞ configList
    // updateConfigSchema  ──┤
    // updateConfigDoc     ──┤
    // updateConfigDesc    ──┤
    // addConfig           ──┤
    // delConfig           ──┘
    updateConfigs: Subject<Config[]> = new Subject();
    updateConfigDoc: Subject<UpdateConfigField> = new Subject();
    updateConfigSchema: Subject<UpdateConfigField> = new Subject();
    updateConfigDesc: Subject<UpdateConfigField> = new Subject();
    addConfig: Subject<AddConfig> = new Subject();
    delConfig: Subject<number> = new Subject();
    updates: Subject<any> = new Subject<any>();
    configMap: Observable<ConfigMap>;
    configList: Subject<Config[]> = new BehaviorSubject<Config[]>([]);

    addConfigUser: Subject<AddConfigUser> = new Subject();
    delConfigUser: Subject<number> = new Subject();
    updateConfigUsers: Subject<ConfigUser[]> = new Subject();
    userUpdates: Subject<any> = new Subject<any>();
    configUsers: Observable<ConfigUser[]>;

    constructor(private api: ConfigerRestAPI) {
        this.addConfig.pipe(
            map(function (add: AddConfig): IConfigMapOperation {
                return (cfgMap: ConfigMap) => {
                    cfgMap.set(add.cfgId.toString(), add.config);
                    return cfgMap;
                };
            })
        ).subscribe(this.updates);

        this.delConfig.pipe(
            map(function (cfgId: number): IConfigMapOperation {
                return (cfgMap: ConfigMap) => {
                    cfgMap.delete(cfgId.toString());
                    return cfgMap;
                };
            })
        ).subscribe(this.updates);

        this.updateConfigs.pipe(
            map(function (cfgs: Config[]): IConfigMapOperation {
                return (cfgMap: ConfigMap) => {
                    const newMap: ConfigMap = new Map();
                    cfgs.forEach(it => newMap.set(it.id.toString(), it));
                    return newMap;
                };
            })
        ).subscribe(this.updates);

        this.updateConfigDoc.pipe(
            map(function (f: UpdateConfigField): IConfigMapOperation {
                return (cfgMap: ConfigMap) => {
                    const old = cfgMap.get(f.cfgId.toString());
                    old.doc.value = f.fieldValue;
                    return cfgMap;
                };
            })
        ).subscribe(this.updates);

        this.updateConfigSchema.pipe(
            map(function (f: UpdateConfigField): IConfigMapOperation {
                return (cfgMap: ConfigMap) => {
                    const old = cfgMap.get(f.cfgId.toString());
                    old.doc.metadata = f.fieldValue;
                    return cfgMap;
                };
            })
        ).subscribe(this.updates);

        this.updateConfigDesc.pipe(
            map(function (f: UpdateConfigField): IConfigMapOperation {
                return (cfgMap: ConfigMap) => {
                    const old = cfgMap.get(f.cfgId.toString());
                    old.description = f.fieldValue;
                    return cfgMap;
                };
            })
        ).subscribe(this.updates);

        // configMap订阅更新操作
        this.configMap = this.updates.pipe(
            scan((cfgMap: ConfigMap, op: IConfigMapOperation) => {
                const newMap = op(cfgMap);
                return newMap;
            }, new Map()),
            publishReplay(1),
            refCount()
        );

        // 生成排序的列表
        this.configMap.pipe(
            map((cfgMap: ConfigMap) => {
                const list: Config[] = [];
                cfgMap.forEach((it, key) => {
                    list.push(it);
                });
                return _.sortBy(list, (t: Config) => t.name);
            })
        ).subscribe(this.configList);



        //---------------------------------------------------
        // updateConfigUsers ──┬──＞ userUpdates ──＞ configUsers
        // delConfigUser     ──┤
        // addConfigUser     ──┘

        this.addConfigUser.pipe(
            map(function (add: AddConfigUser): IConfigUsersOperation {
                return (users: ConfigUser[]) => {
                    users.push(add.user);
                    return users;
                };
            })
        ).subscribe(this.userUpdates);

        this.delConfigUser.pipe(
            map(function (userId: number): IConfigUsersOperation {
                return (users: ConfigUser[]) => {
                    const newUsers: ConfigUser[] = [];
                    users.forEach((it) => {
                        if (it.userId != userId) {
                            newUsers.push(it);
                        }
                    });
                    return newUsers;
                };
            })
        ).subscribe(this.userUpdates);

        this.updateConfigUsers.pipe(
            map(function (newUsers: ConfigUser[]): IConfigUsersOperation {
                return (users: ConfigUser[]) => {
                    return newUsers;
                };
            })
        ).subscribe(this.userUpdates);

        // configUsers订阅更新操作
        this.configUsers = this.userUpdates.pipe(
            scan((users: ConfigUser[], op: IConfigUsersOperation) => {
                const newUsers = op(users);
                return newUsers;
            }, []),
            publishReplay(1),
            refCount()
        );
        //---------------------------------------------------

    }

    public selectProject(id: number): void {
        this.api.getProjectConfigs(id).subscribe(
            ret => {
                if (ret.code === 0) {
                    this.updateConfigs.next(ret.result);
                }
            });
    }

    public updateSchema(configId: number , docId: number, schema: string): void {
        this.api.updateConfigSchema(configId, docId, schema).subscribe(
            ret => {
                if (ret.code === 0) {
                    this.updateConfigSchema.next({ cfgId: configId, fieldValue: schema });
                }
            });
    }

    public updateValue(cfgId: number, docId: number, value: string): void {
        this.api.updateConfigDoc(cfgId, docId, value).subscribe(
            ret => {
                if (ret.code === 0) {
                    this.updateConfigDoc.next({ cfgId: cfgId, fieldValue: value });
                }
            });
    }

    public updateDesc(cfgId: number, desc: string): void {
        this.api.updateConfigDescription(cfgId, desc).subscribe(
            ret => {
                if (ret.code === 0) {
                    this.updateConfigDesc.next({ cfgId: cfgId, fieldValue: desc });
                }
            });
    }

    public createConfig(config: Config): void {
        this.api.createConfig(config).subscribe(
            result => {
                if (result.code === 0) {
                    this.addConfig.next({ cfgId: result.result.id, config: result.result });
                }
            }
        );
    }

    public deleteConfig(cfgId: number): void {
        this.api.deleteConfig(cfgId).subscribe(
            result => {
                if (result.code === 0 && result.result === 'succeed') {
                    this.delConfig.next(cfgId);
                }
            }
        );
    }

    public getConfigUsers(cfgId: number, prjId: number): void {
        this.api.getConfigUsers(cfgId, prjId).subscribe(
            result => {
                if (result.code === 0) {
                    this.updateConfigUsers.next(result.result);
                }
            }
        );
    }

    public addConfigAuth(cfgId: number, user: string) {
        this.api.addConfigUser(cfgId, user).subscribe(
            result => {
                if (result.code === 0) {
                    this.addConfigUser.next({user: result.result});
                }
            }
        );
    }

    public delConfigAuth(cfgId: number, userId: number) {
        this.api.delConfigUser(cfgId, userId).subscribe(
            result => {
                if (result.code === 0) {
                    this.delConfigUser.next(userId);
                }
            }
        );
    }

}
