import { Component, Injectable } from '@angular/core';
import { AppNotifyDialogService } from './app-notify-dialog.service';

@Component({
  selector: 'app-notify-dialog',
  templateUrl: './app-notify-dialog.component.html',
})
export class AppNotifyDialogComponent {

  public opened = false;
  public title = 'Notify';
  public message = '';
  public description = '';

  constructor(private service: AppNotifyDialogService) {
      service.notify.subscribe(
          event => {
              this.title = event.title;
              this.message = event.message;
              this.description = event.description;
              this.open();
          }
      );
  }

  public open(): void {
    this.opened = true;
  }

  public close(): void {
    this.opened = false;
  }
}
