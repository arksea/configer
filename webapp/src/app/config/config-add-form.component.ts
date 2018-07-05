import { Component, Input, OnInit } from '@angular/core';
import { Config, Project, ConfigDoc } from '../configer.model';
import { ConfigService } from '../config/config.service';

@Component({
  selector: 'app-config-add-form',
  templateUrl: './config-add-form.component.html',
})
export class ConfigAddFormComponent {
  @Input()
  private project: Project;

  public opened = false;
  public configName: string;
  public description: string;

  constructor(private configService: ConfigService) { }

  public open(): void {
    this.opened = true;
  }

  public close(): void {
    this.opened = false;
  }

  public onSave(): void {
    const config = new Config();
    config.name = this.configName;
    config.description = this.description;
    config.doc = new ConfigDoc();
    config.doc.value = '""';
    config.doc.metadata = 'json';
    config.project = this.project;
    this.configService.createConfig(config);
    this.configName = '';
    this.description = '';
    this.close();
  }

}
