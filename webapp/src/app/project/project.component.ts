import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Subject } from 'rxjs';
import { ProjectService } from '../project/project.service';
import { ConfigService } from '../config/config.service';
import { Config, Project } from '../configer.model';
import { SchemaFormComponent } from '../schema/schema-form.component';
import { ConfigFormComponent } from '../config/config-form.component';
import { ConfigValueFormComponent } from '../config/config-value-form.component';
import { ConfigAddFormComponent } from '../config/config-add-form.component';
import { AppNotifyDialogService } from '../app-notify-dialog.service';

@Component({
  selector: 'app-project',
  styleUrls: ['./project.component.css'],
  templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit {
  @Input() projectId: number;
  @ViewChild(SchemaFormComponent) dialogSchemaForm: SchemaFormComponent;
  @ViewChild(ConfigFormComponent) dialogConfigForm: ConfigFormComponent;
  @ViewChild(ConfigValueFormComponent) dialogConfigValueForm: ConfigValueFormComponent;
  @ViewChild(ConfigAddFormComponent) dialogConfigAddForm: ConfigAddFormComponent;
  configs: Subject<Config[]> = this.svcCfg.configList;
  project: Subject<Project> = this.svcPrj.selectedProject;

  constructor(
    private svcPrj: ProjectService,
    private svcCfg: ConfigService,
    private notify: AppNotifyDialogService
  ) { }

  ngOnInit(): void {
    if (this.projectId) {
      this.svcPrj.selectProject(this.projectId);
      this.svcCfg.selectProject(this.projectId);
    }
  }

  onBtnEditClick(config: Config) {
    this.dialogConfigForm.open(config);
  }

  onBtnSchemaClick(config: Config) {
    this.dialogSchemaForm.open(config);
  }

  onBtnJsonClick(config: Config) {
    this.dialogConfigValueForm.open(config);
  }

  onBtnConfigAddClick() {
    this.dialogConfigAddForm.open();
  }

  onBtnConfigDelClick(config: Config) {
    this.notify.openWidthConfirm('Warning', 'Make sure that you want to delete?', config.name).subscribe(
      del => {
        if (del) {
          this.svcCfg.deleteConfig(config.id);
        }
      }
    );
  }
}


