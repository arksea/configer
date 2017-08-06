import { Component,Input,OnInit } from '@angular/core';
import { Config,Project,ConfigDoc }  from './configer.entity';
import { ConfigerService } from './configer.service';

@Component({
  selector: 'project-form',
  templateUrl: './project-form.component.html'
})
export class ProjectFormComponent {

  public visible = false;
  public visibleAnimate = false;

  public projectName : string;
  public description : string;
  public profile     : string;

  constructor(
      private configService: ConfigerService
  ) {}

  public show(): void {
    this.visible = true;
    setTimeout(() => this.visibleAnimate = true, 100);
  }

  public hide(): void {
    this.visibleAnimate = false;
    this.visible = false;
    setTimeout(() => this.visible = false, 300);
  }

  public onContainerClicked(event: MouseEvent): void {
    if ((<HTMLElement>event.target).classList.contains('modal')) {
      this.hide();
    }
  }

  public onSave(): void {
      let project = new Project();
      project.name = this.projectName;
      project.description = this.description;
      project.profile = this.profile;
      this.configService.createProject(project);
      this.projectName = '';
      this.description = '';
      this.profile = '';
      this.hide();
  }
}

