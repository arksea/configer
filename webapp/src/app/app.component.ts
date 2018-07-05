import { Component, ViewChild } from '@angular/core';
import { AppNotifyDialogComponent } from './app-notify-dialog.component';
import { }
import {
  WidgetRegistry,
  Validator,
  DefaultWidgetRegistry,
} from 'ngx-schema-form';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  @ViewChild(AppNotifyDialogComponent) appNotifyDialog: AppNotifyDialogComponent;
  constructor() {
  }
}
