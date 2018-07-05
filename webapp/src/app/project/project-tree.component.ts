import { Component, ViewChild, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { ProjectService, TreeNode } from '../project/project.service';
import { ConfigService } from '../config/config.service';
import { Project } from '../configer.model';
import { ProjectNewFormComponent } from './project-new-form.component';
import { AppNotifyDialogService } from '../app-notify-dialog.service';

@Component({
  selector: 'app-project-tree',
  templateUrl: './project-tree.component.html'
})
export class ProjectTreeComponent implements OnInit {
    treeRoot: Subject<TreeNode[]> = null;
    selectedProject = null;
    @ViewChild(ProjectNewFormComponent) dialogProjectNewForm: ProjectNewFormComponent;

    constructor(private svcPrj: ProjectService,
                private svcCfg: ConfigService,
                private notify: AppNotifyDialogService) {
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
        this.selectedProject = prj;
        this.svcPrj.selectProject(prj.id);
        this.svcCfg.selectProject(prj.id);
    }

    onClickProjectAddBtn() {
        this.dialogProjectNewForm.open();
    }

    onClickProjectDelBtn(prj: Project) {
        this.notify.openWidthConfirm('Warning', 'Make sure that you want to delete the project?', prj.name).subscribe(
            del => {
            if (del) {
                this.svcPrj.deleteProject(prj.id);
            }
            }
        );
    }
}
