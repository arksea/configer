import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SignupInfo, LoginInfo } from '../configer.model';
import { AccountService } from './account.service';

@Component({
  selector: 'app-sign-in',
  styleUrls: ['./sign-in.component.css'],
  templateUrl: './sign-in.component.html'
})
export class SignInComponent implements OnInit {

    constructor(public router: Router, private accountService: AccountService) {
    }

    ngOnInit(): void {
    }

    login(event, name, password) {
      event.preventDefault();
      const body = new LoginInfo();
      body.name = name;
      body.password = password;
      this.accountService.login(body);
    }

    signup(event) {
      event.preventDefault();
      this.router.navigate(['signup']);
    }
}
