import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject, Observable, ObjectUnsubscribedError } from 'rxjs';
import { ConfigerRestAPI } from '../configer.restapi';
import { Project, Config } from '../configer.model';
import { scan, map, publishReplay, refCount } from 'rxjs/operators';
import * as _ from 'lodash';

export interface TreeNode {
    expanded: boolean;
    title: string;
    value: any;
    nodes: TreeNode[];
}

export interface ProjectMap {
    [key: string]: Project;
}

type ITreeOperation = (nodes: TreeNode[]) => TreeNode[];
type IProjectMapOperation = (projects: ProjectMap) => ProjectMap;

@Injectable()
export class ProjectService {

    // updateProject --> updates --> projects --> projectTreeRoot

    projects: Observable<ProjectMap>;
    projectTreeRoot: Subject<TreeNode[]> = new BehaviorSubject<TreeNode[]>([]);
    updates: Subject<any> = new Subject<any>();

    updateProjects: Subject<Project[]> = new Subject(); // 更新项目列表： 从服务端读取项目列表，并更新本地存储

    selectedProject: Subject<Project> = new BehaviorSubject<Project>({
        id: -1, name: '', profile: '', description: ''
    });
    selectedConfigs: Subject<Config[]> = new BehaviorSubject<Config[]>([]);

    constructor(private api: ConfigerRestAPI) {
        // 定义更新项目列表操作：丢弃旧值，用新值替换（新值是数组，需要map成ProjectMap类型）
        this.updateProjects.pipe(
            map(function (prjs: Project[]): IProjectMapOperation {
                return (prjMap: ProjectMap) => {
                    const newMap: { [key: string]: Project } = {};
                    prjs.forEach(it => prjMap[it.id] = it);
                    return newMap;
                };
            })
        ).subscribe(this.updates);

        // projects订阅所有的更新操作
        this.projects = this.updates.pipe(
            scan((prjMap: ProjectMap, op: IProjectMapOperation) => {
                return op(prjMap);
            }, {}),
            publishReplay(1),
            refCount()
        );

        // 生成项目的树状视图
        this.projects.pipe(
            map((prjMap: ProjectMap) => {
                return _.values(prjMap);
            }),
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
                return _.sortBy(_.values(tree), (t: TreeNode) => t.title);
            })
        ).subscribe(this.projectTreeRoot);
    }

    public updateProjectTree(): void {
        this.api.getAllProjects().subscribe(this.updateProjects);
    }

    public selectProject(id: number): void {
        this.api.getProject(id).subscribe(prj => {
            this.selectedProject.next(prj);
        });
        this.api.getProjectConfigs(id).subscribe(configs => {
            this.selectedConfigs.next(configs);
        });
    }
}

