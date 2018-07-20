import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoginInfo, SignupInfo, RestResult } from '../configer.model';
import { ConfigerRestAPI } from '../configer.restapi';
import { AppNotifyDialogService } from '../app-notify-dialog.service';

@Injectable()
export class AccountService {

    public constructor(private api: ConfigerRestAPI, private router: Router, private notify: AppNotifyDialogService) {}

    public login(info: LoginInfo): void {
      this.api.userLogin(info).subscribe(
         response => {
           if (response.code === 0) {
              this.router.navigate(['/projects']);
           } else {
              this.notify.openWidthConfirm('Warning', 'Login failed', response.error);
           }

         });
    }
}

