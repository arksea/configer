import { Component } from '@angular/core';
import { ProjectUser, Project } from '../configer.model';
import { ProjectService } from '../project/project.service';

@Component({
  selector: 'app-project-user-auth-form',
  templateUrl: './project-user-auth-form.component.html',
})
export class ProjectUserAuthFormComponent {
  private user: ProjectUser;
  private projectId: number;
  public opened = false;

  constructor(private projectService: ProjectService) {
    this.user = {
      query: false,
      manage: false,
      config: false,
      userId: -1,
      userName: ''
    };
  }

  public open(prjId: number, user: ProjectUser): void {
    this.user = user;
    this.projectId = prjId;
    this.opened = true;
  }

  public close(): void {
    this.opened = false;
  }

  public onSave(): void {
    this.opened = false;
    this.projectService.updateProjectUser(this.projectId, this.user);
  }
}

