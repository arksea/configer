import { Component, OnInit, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';
import { ProjectService } from './project.service';
import { Config, Project } from '../configer.model';
// import { ConfigFormComponent } from '../config-form.component';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit {
  configs: Subject<Config[]> = this.service.selectedConfigs;
  project: Subject<Project> = this.service.selectedProject;

  constructor(
      private service: ProjectService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id: number = parseInt(this.route.snapshot.paramMap.get('id'), 10);
    if (id) {
        this.service.selectProject(id);
    }
  }

  onCreateConfig() {
  }
}


