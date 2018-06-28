import { Component, Inject, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { ProjectService, TreeNode } from './project.service';
import { Project } from '../configer.model';


@Component({
  selector: 'app-project-tree',
  templateUrl: './project-tree.component.html'
})
export class ProjectTreeComponent implements OnInit {
    treeRoot: Subject<TreeNode[]> = null;

    constructor(private service: ProjectService) {
        this.treeRoot =  this.service.projectTreeRoot;
    }

    ngOnInit(): void {
        this.updateProjectTree();
    }

    onClickRefreshBtn() {
        this.updateProjectTree();
    }

    updateProjectTree() {
        this.service.updateProjectTree();
    }

    onClickOne(prj: Project) {
        this.service.selectProject(prj.id);
    }
}

