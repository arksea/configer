import { Component } from '@angular/core';
import {
  WidgetRegistry,
  Validator,
  DefaultWidgetRegistry,
} from 'ngx-schema-form';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [{ provide: WidgetRegistry, useClass: DefaultWidgetRegistry }]
})
export class AppComponent {

  constructor(registry: WidgetRegistry) {
  }
}
