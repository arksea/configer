import { Component, Input, OnInit } from '@angular/core';
import { Config, Project, ConfigDoc, ConfigUser } from '../configer.model';
import { ConfigService } from '../config/config.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-config-auth-form',
  templateUrl: './config-auth-form.component.html',
})
export class ConfigAuthFormComponent {
  users : Observable<ConfigUser[]> = this.configService.configUsers;
  config: Config;
  public opened = false;
  userName: string = '';
  constructor(private configService: ConfigService) { }

  public open(config: Config): void {
    this.config = config;
    this.configService.getConfigUsers(config.id, config.project.id).subscribe(
        succeed => {
            if (succeed) {
                this.opened = true;
            }
        }
    );
  }

  public close(): void {
     this.configService.clearConfigAuth();
     this.opened = false;
  }

  public onAddUser(name: string): void {
    this.configService.addConfigAuth(this.config.id, name).subscribe(
        succeed => {
            if (succeed) {
                this.userName = '';
            } else {
                this.opened = false;
            }
        }
    );
  }

  public btnDisabled(): boolean {
     return !this.userName;
  }

  public onBtnDelClick(user: ConfigUser): void {
      this.configService.delConfigAuth(user.configId, user.userId).subscribe(
        succeed => {
            this.opened = succeed;
        }
      );
  }
}
