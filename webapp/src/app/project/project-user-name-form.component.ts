import { Component } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Component({
  selector: 'app-project-user-name-form',
  styles: ['.modal-body {display: flex; flex-direction: column; padding: 1rem 0 0.5rem 0;}; .userName {margin: 0.25rem 0 0.75rem 0;}'],
  templateUrl: './project-user-name-form.component.html',
})
export class ProjectUserNameFormComponent {
  private result: Subject<string>;
  userName: string;
  public opened = false;

  public open(): Observable<string> {
    this.opened = true;
    this.result = new Subject<string>();
    return this.result;
  }

  public close(): void {
    this.opened = false;
    this.userName = '';
  }

  public onSave(): void {
    this.opened = false;
    this.result.next(this.userName);
    this.userName = '';
  }
}

