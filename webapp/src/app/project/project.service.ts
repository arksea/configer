import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject, Observable, ObjectUnsubscribedError } from 'rxjs';
import { ConfigerRestAPI } from '../configer.restapi';
import { Project } from '../configer.model';
import { scan, map, publishReplay, refCount } from 'rxjs/operators';
import * as _ from 'lodash';

export interface TreeNode {
    expanded: boolean;
    title: string;
    value: any;
    nodes: TreeNode[];
}

type ProjectMap = Map<string, Project>;
type IProjectMapOperation = (projects: ProjectMap) => ProjectMap;
interface AddProject {
    prjId: number;
    project: Project;
}

@Injectable()
export class ProjectService {

    // updateProjects --> updates --> projects --> projectTreeRoot

    projects: Observable<ProjectMap>;
    projectTreeRoot: Subject<TreeNode[]> = new BehaviorSubject<TreeNode[]>([]);
    addProject: Subject<AddProject> = new Subject();
    delProject: Subject<number> = new Subject();
    updates: Subject<any> = new Subject<any>();
    updateProjects: Subject<Project[]> = new Subject(); // 更新项目列表： 从服务端读取项目列表，并更新本地存储

    selectedProject: Subject<Project> = new BehaviorSubject<Project>({
        id: -1, name: '', profile: '', description: ''
    });

    constructor(private api: ConfigerRestAPI) {
        this.addProject.pipe(
            map(function (add: AddProject): IProjectMapOperation {
                return (prjMap: ProjectMap) => {
                    prjMap.set(add.prjId.toString(), add.project);
                    return prjMap;
                };
            })
        ).subscribe(this.updates);

        this.delProject.pipe(
            map(function (prjId: number): IProjectMapOperation {
                return (prjMap: ProjectMap) => {
                    prjMap.delete(prjId.toString());
                    return prjMap;
                };
            })
        ).subscribe(this.updates);

        // 定义更新项目列表操作：丢弃旧值，用新值替换（新值是数组，需要map成ProjectMap类型）
        this.updateProjects.pipe(
            map(function (prjs: Project[]): IProjectMapOperation {
                return (prjMap: ProjectMap) => {
                    const newMap: ProjectMap = new Map();
                    prjs.forEach(it => newMap.set(it.id.toString(), it));
                    return newMap;
                };
            })
        ).subscribe(this.updates);

        // projects订阅所有的更新操作
        this.projects = this.updates.pipe(
            scan((prjMap: ProjectMap, op: IProjectMapOperation) => {
                const newMap = op(prjMap);
                return newMap;
            }, new Map()),
            publishReplay(1),
            refCount()
        );

        // 生成项目的树状视图
        this.projects.pipe(
            map((prjMap: ProjectMap) => {
                const list: Project[] = [];
                prjMap.forEach((it, key) => {
                    list.push(it);
                });
                return list;
            })
        ).pipe(
            map((prjs: Project[]) => {
                const tree: Map<string, TreeNode> = new Map();
                for (const prj of prjs) {
                    let prjNode: TreeNode = tree.get(prj.name);
                    if (prjNode == null) {
                        prjNode = {
                            expanded: false,
                            title: prj.name,
                            value: prj,
                            nodes: []
                        };
                        tree.set(prj.name, prjNode);
                    }
                    const profileNode: TreeNode = {
                        expanded: false,
                        title: prj.profile,
                        value: prj,
                        nodes: []
                    };
                    prjNode.nodes.push(profileNode);
                }
                const list: TreeNode[] = [];
                tree.forEach((it, key) => {
                    list.push(it);
                });
                return _.sortBy(list, (t: TreeNode) => t.title);
            })
        ).subscribe(this.projectTreeRoot);
    }

    public updateProjectTree(): void {
        this.api.getAllProjects().subscribe(
            result => {
                if (result.code === 0) {
                    this.updateProjects.next(result.result);
                }
            }
        );
    }

    public selectProject(id: number): void {
        this.api.getProject(id).subscribe(
            ret => {
                if (ret.code === 0) {
                    this.selectedProject.next(ret.result);
                }
            }
        );
    }

    public createProject(prj: Project): void {
        this.api.createProject(prj).subscribe(
            result => {
                if (result.code === 0 && result.result > 0) {
                    prj.id = result.result;
                    this.addProject.next({prjId: result.result, project: prj});
                }
            }
        );
    }

    public deleteProject(prjId: number): void {
        this.api.deleteProject(prjId).subscribe(
            result => {
                if (result.code === 0 && result.result === 'succeed') {
                    this.delProject.next(prjId);
                }
            }
        );
    }
}

