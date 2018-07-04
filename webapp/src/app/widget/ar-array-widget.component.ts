import { Component } from '@angular/core';
import { ArrayLayoutWidget } from 'ngx-schema-form';

@Component({
  selector: 'app-ar-array-widget',
  templateUrl: './ar-array-widget.component.html'
})
export class ArArrayWidgetComponent extends ArrayLayoutWidget {

  addItem() {
    this.formProperty.addItem();
  }

  removeItem(index: number) {
    this.formProperty.removeItem(index);
  }

  reset() {
    this.formProperty.reset([]);
  }

  trackByIndex(index: number, item: any) {
    return index;
  }
}
