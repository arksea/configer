import { ArBooleanWidgetComponent } from './ar-boolean-widget.component';
import {
    DefaultWidgetRegistry,
  } from 'ngx-schema-form';

export class ArWidgetRegistry extends DefaultWidgetRegistry {
  constructor() {
    super();
    this.register('boolean',  ArBooleanWidgetComponent);
  }
}
