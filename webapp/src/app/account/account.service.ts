import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, BehaviorSubject, Observable, ObjectUnsubscribedError } from 'rxjs';
import { LoginInfo, SignupInfo, RestResult } from '../configer.model';
import { ConfigerRestAPI } from '../configer.restapi';
import { AppNotifyDialogService } from '../app-notify-dialog.service';

@Injectable()
export class AccountService {
  loginUser: Subject<string> = new BehaviorSubject<string>(null);
  public constructor(private api: ConfigerRestAPI, private router: Router, private notify: AppNotifyDialogService) {
    const userName = localStorage.getItem('login_user');
    this.loginUser.next(userName);
  }

  public login(info: LoginInfo): void {
    this.api.userLogin(info).subscribe(
      response => {
        if (response.code === 0) {
          this.loginUser.next(info.name);
          localStorage.setItem('token_expires',  response.result);
          localStorage.setItem('login_user', info.name);
          this.router.navigate(['/projects']);
        } else {
          this.loginUser.next(null);
          this.notify.openWidthTitle('Warning', response.error);
        }
      });
  }

  public signup(info: SignupInfo): void {
    this.api.userSignup(info).subscribe(
      response => {
        if (response.code === 0) {
          this.loginUser.next(info.name);
          localStorage.setItem('token_expires',  response.result);
          localStorage.setItem('login_user', info.name);
          this.router.navigate(['/projects']);
          this.notify.openWidthTitle('Notify', 'Signup succeed');
        } else {
          this.loginUser.next(null);
          this.notify.openWidthTitle('Warning', response.error);
        }
      });
  }

  public logout(): void {
    this.loginUser.next(null);
    localStorage.setItem('token_expires',  '0');
    localStorage.setItem('login_user', '');
    const exp: Date = new Date();
    exp.setTime(exp.getTime() - 24 * 60 * 60_000);
    const cookie = 'access_token=;path=/api/v1;expires=' + exp.toUTCString();
    document.cookie = cookie;
  }

  private getCookie(name) {
    let arr;
    const reg = new RegExp('(^| )' + name + '=([^;]*)(;|$)');
    if (arr = document.cookie.match(reg)) {
      return unescape(arr[2]);
    } else {
      return null;
    }
  }

}
