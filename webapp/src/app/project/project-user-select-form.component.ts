import { Component } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { ProjectUser, Project } from '../configer.model';
import { ProjectService } from '../project/project.service';

@Component({
  selector: 'app-project-user-select-form',
  styles: ['.modal-body {display: flex; flex-direction: column; padding: 1rem 0 0.5rem 0;};  .select {margin: 0.25rem 0 0.75rem 0;}'],
  templateUrl: './project-user-select-form.component.html',
})
export class ProjectUserSelectFormComponent {
  users: Observable<ProjectUser[]> = new Subject<ProjectUser[]>();
  private selected: Subject<number>;
  selectedUser: number;
  public opened = false;

  constructor(private projectService: ProjectService) {
  }

  public open(users: Observable<ProjectUser[]>): Observable<number> {
    this.users = users;
    this.opened = true;
    this.selected = new Subject<number>();
    return this.selected;
  }

  public close(): void {
    this.opened = false;
    this.selectedUser = undefined;
  }

  public onSave(): void {
    this.opened = false;
    this.selected.next(this.selectedUser);
    this.selectedUser = undefined;
  }
}

