import { ArBooleanWidgetComponent } from './ar-boolean-widget.component';
import { ArRadioWidgetComponent } from './ar-radio-widget.component';
import { ArCheckboxWidgetComponent } from './ar-checkbox-widget.component';
import { ArSelectWidgetComponent } from './ar-select-widget.component';
import { ArArrayWidgetComponent } from './ar-array-widget.component';
import {
    DefaultWidgetRegistry,
  } from 'ngx-schema-form';

export class ArWidgetRegistry extends DefaultWidgetRegistry {
  constructor() {
    super();
    this.register('boolean',  ArBooleanWidgetComponent);
    this.register('checkbox', ArCheckboxWidgetComponent);
    this.register('radio',    ArRadioWidgetComponent);
    this.register('select',   ArSelectWidgetComponent);
    this.register('array',    ArArrayWidgetComponent);
  }
}
