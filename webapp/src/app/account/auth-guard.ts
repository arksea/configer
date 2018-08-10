import { Injectable } from '@angular/core';
import { Router, CanActivate} from '@angular/router';
import { AccountService } from './account.service';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private accountService: AccountService) {}

  canActivate() {
    const exp = localStorage.getItem('token_expires');
    const now = new Date().valueOf();
    console.log('token_expires=' + exp + ', now=' + now);
    if (exp && parseInt(exp, 10) > now) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
