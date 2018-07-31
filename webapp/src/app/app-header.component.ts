import { Component } from '@angular/core';
import { Subject } from 'rxjs';
import { AccountService } from './account/account.service';
@Component({
    selector: 'app-header',
    templateUrl: './app-header.component.html'
})
export class AppHeaderComponent {
    loginUser: Subject<string> = null;
    constructor(private accountService: AccountService) {
        this.loginUser = accountService.loginUser;
    }

    logout() {
        this.accountService.logout();
    }
}
