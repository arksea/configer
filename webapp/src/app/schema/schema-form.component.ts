import { Component, Input, OnInit } from '@angular/core';
import { Config } from '../configer.model';

@Component({
  selector: 'app-schema-form',
  templateUrl: './schema-form.component.html'
})
export class SchemaFormComponent implements OnInit {
  @Input() config: Config;
  opened = false;
  visibleAnimate = false;
  lineCount: number;

  constructor() {
  }

  ngOnInit(): void {
    this.lineCount = this.config.doc.metadata.split(/\n/).length;
    if (this.lineCount > 30) {
      this.lineCount = 30;
    } else if (this.lineCount < 5) {
      this.lineCount = 5;
    }
  }

  public open(): void {
    this.opened = true;
  }

  public close(): void {
    this.opened = false;
  }

}
