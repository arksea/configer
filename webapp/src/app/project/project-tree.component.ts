import { Component, Inject, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { ProjectService, TreeNode } from '../project/project.service';
import { ConfigService } from '../config/config.service';
import { Project } from '../configer.model';


@Component({
  selector: 'app-project-tree',
  templateUrl: './project-tree.component.html'
})
export class ProjectTreeComponent implements OnInit {
    treeRoot: Subject<TreeNode[]> = null;

    constructor(private svcPrj: ProjectService,
                private svcCfg: ConfigService) {
        this.treeRoot =  this.svcPrj.projectTreeRoot;
    }

    ngOnInit(): void {
        this.updateProjectTree();
    }

    onClickRefreshBtn() {
        this.updateProjectTree();
    }

    updateProjectTree() {
        this.svcPrj.updateProjectTree();
    }

    onClickOne(prj: Project) {
        this.svcPrj.selectProject(prj.id);
        this.svcCfg.selectProject(prj.id);
    }
}

