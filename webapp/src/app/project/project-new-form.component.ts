import { Component, Input, OnInit } from '@angular/core';
import { Config, Project, ConfigDoc } from '../configer.model';
import { ProjectService } from '../project/project.service';

@Component({
  selector: 'app-project-new-form',
  templateUrl: './project-new-form.component.html',
})
export class ProjectNewFormComponent {
  @Input()
  private project: Project;

  public opened = false;
  public projectName: string;
  public profile: string;
  public description: string;

  constructor(private projectService: ProjectService) { }

  public open(): void {
    this.opened = true;
  }

  public close(): void {
    this.opened = false;
  }

  public onSave(): void {
    const prj = new Project();
    prj.name = this.projectName;
    prj.description = this.description;
    prj.profile = this.profile;
    this.projectService.createProject(prj);
    this.projectName = '';
    this.description = '';
    this.profile = '';
    this.close();
  }

}
