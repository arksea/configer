import { Component, OnInit, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';
import { ProjectService } from '../project/project.service';
import { ConfigService } from '../config/config.service';
import { Config, Project } from '../configer.model';
// import { ConfigFormComponent } from '../config-form.component';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit {
  configs: Subject<Config[]> = this.svcCfg.configList;
  project: Subject<Project> = this.svcPrj.selectedProject;

  constructor(
      private svcPrj: ProjectService,
      private svcCfg: ConfigService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id: number = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (id) {
        this.svcPrj.selectProject(id);
        this.svcCfg.selectProject(id);
    }
  }

  onCreateConfig() {
  }
}


