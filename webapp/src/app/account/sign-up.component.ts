import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SignupInfo, LoginInfo } from '../configer.model';
import { AccountService } from './account.service';
import { AppNotifyDialogService } from '../app-notify-dialog.service';

@Component({
  selector: 'app-sign-up',
  styleUrls: ['./sign-up.component.css'],
  templateUrl: './sign-up.component.html'
})
export class SignUpComponent implements OnInit {

    constructor(public router: Router, private accountService: AccountService, private notify: AppNotifyDialogService) {
    }

    ngOnInit(): void {
    }

    signup(event, name, email, password, password2) {
      event.preventDefault();
      if (password === password2) {
        const body = new SignupInfo();
        body.name = name;
        body.email = email;
        body.password = password;
        this.accountService.signup(body);
      } else {
        this.notify.openWidthTitle('Error', 'Different passwords for the two input');
      }
    }
}
