import { Component } from '@angular/core';
import { CheckboxWidget } from 'ngx-schema-form';

// 因为使用了clr控件集，ngx-schema-form部分widget不能正常显示，此处显式使用clr的控件
@Component({
  selector: 'app-ar-boolean-widget',
  templateUrl: './ar-boolean-widget.component.html'})
export class ArBooleanWidgetComponent extends CheckboxWidget {

}
