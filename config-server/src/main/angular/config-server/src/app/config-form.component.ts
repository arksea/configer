import { Component,Input,OnInit } from '@angular/core';
import { Config,Project,ConfigDoc }  from './configer.entity';
import { ConfigerService } from './configer.service';

@Component({
  selector: 'config-form',
  templateUrl: './config-form.component.html'
})
export class ConfigFormComponent {
  @Input()
  private project : Project;

  public visible = false;
  public visibleAnimate = false;

  public configName : string;
  public description : string;
  public value : string;

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
      let config = new Config();
      config.name = this.configName;
      config.description = this.description;
      config.doc = new ConfigDoc();
      config.doc.value = this.value;
      config.doc.metadata = 'json';
      config.project = this.project;
      this.configService.createConfig(config);
      this.configName = '';
      this.description = '';
      this.value = '';
      this.hide();
  }


}
