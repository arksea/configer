import { Component } from '@angular/core';
import { ProjectUser, Project } from '../configer.model';
import { ProjectService } from '../project/project.service';

@Component({
  selector: 'app-project-user-auth-form',
  styles: ['.modal-body {display: flex; flex-direction: column; padding: 1rem 0 0.5rem 0;}; .auth {margin: 0.25rem 0 0.75rem 0;}'],
  templateUrl: './project-user-auth-form.component.html',
})
export class ProjectUserAuthFormComponent {
  user: ProjectUser;
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

