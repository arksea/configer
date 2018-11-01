import { Component, Input, OnInit } from '@angular/core';
import { Config, Project, ConfigDoc, ConfigUser } from '../configer.model';
import { ConfigService } from '../config/config.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-config-auth-form',
  templateUrl: './config-auth-form.component.html',
})
export class ConfigAuthFormComponent {
//  @Input()
//  private project: Project;
  users : Observable<ConfigUser[]> = this.configService.configUsers;
  config: Config;
  public opened = false;
  userName: string = '';
  constructor(private configService: ConfigService) { }

  public open(config: Config): void {
    this.opened = true;
    this.config = config;
    this.configService.getConfigUsers(config.id, config.project.id);
  }

  public close(): void {
    this.opened = false;
  }

  public onAddUser(name: string): void {
    this.configService.addConfigAuth(this.config.id, name);
    this.userName = '';
  }

  public btnDisabled(): boolean {
     return !this.userName;
  }

  public onBtnDelClick(user: ConfigUser): void {

  }
}
