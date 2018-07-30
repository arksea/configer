import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SignupInfo, LoginInfo } from '../configer.model';
import { AccountService } from './account.service';

@Component({
  selector: 'app-sign-up',
  styleUrls: ['./sign-up.component.css'],
  templateUrl: './sign-up.component.html'
})
export class SignUpComponent implements OnInit {

    constructor(public router: Router, private accountService: AccountService) {
    }

    ngOnInit(): void {
    }

    signup(event, name, email, password, password2) {
      event.preventDefault();
      const body = new SignupInfo();
      body.name = name;
      body.email = email;
      body.password = password;
      this.accountService.signup(body);
    }
}
