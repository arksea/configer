import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { environment } from '../../environments/environment';
import { ConfigerRestAPI } from '../configer.restapi';
import { Project } from '../configer.model';
// import { ProjectFormComponent } from './project-form.component';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html'
})
export class ProjectListComponent implements OnInit {

  // @ViewChild(ProjectFormComponent)
  // private projectForm: ProjectFormComponent;

  projects: Project[];

  constructor(private restapi: ConfigerRestAPI) { }

  ngOnInit(): void {
    this.restapi.getAllProjects().subscribe(data => {
      this.projects = data;
    });
  }

  // onCreateProject() {
  //   this.projectForm.show();
  // }
}
