import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject, Observable, ObjectUnsubscribedError } from 'rxjs';
import { ConfigerRestAPI } from '../configer.restapi';
import { Config } from '../configer.model';

@Injectable()
export class ConfigService {
    selectedConfigs: Subject<Config[]> = new BehaviorSubject<Config[]>([]);

    constructor(private api: ConfigerRestAPI) {
    }

    public selectProject(id: number): void {
        this.api.getProjectConfigs(id).subscribe(configs => {
            this.selectedConfigs.next(configs);
        });
    }

    public updateSchema(cfgId: number, docId: number, schema: string): void {
        this.api.updateConfigSchema(cfgId, docId, schema);
    }

    public updateValue(cfgId: number, docId: number, value: string): void {
        this.api.updateConfigDoc(cfgId, docId, value);
    }
}

