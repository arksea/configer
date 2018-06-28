import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject, Observable, ObjectUnsubscribedError } from 'rxjs';
import { ConfigerRestAPI } from '../configer.restapi';
import { Project, Config } from '../configer.model';

export interface TreeNode {
    expanded: boolean;
    title: string;
    value: any;
    nodes: TreeNode[];
}

@Injectable()
export class ProjectService {
    projectTreeRoot: Subject<TreeNode[]> = new BehaviorSubject<TreeNode[]>([]);
    selectedProject: Subject<Project> = new BehaviorSubject<Project>(null);
    selectedConfigs: Subject<Config[]> =  new BehaviorSubject<Config[]>([]);

    constructor(private api: ConfigerRestAPI) {
    }

    public updateProjectTree(): void {
        this.api.getAllProjects().subscribe(
            (prjs: Project[]) => {
                const prjMap: Map<string, TreeNode> = new Map();
                for (const prj of prjs) {
                    let prjNode: TreeNode = prjMap.get(prj.name);
                    if (prjNode == null) {
                        prjNode = {
                            expanded: false,
                            title: prj.name,
                            value: prj,
                            nodes: []
                        };
                        prjMap.set(prj.name, prjNode);
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
                prjMap.forEach( (value, key) =>
                    list.push(value)
                );
                this.projectTreeRoot.next(list);
            }
        );
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

