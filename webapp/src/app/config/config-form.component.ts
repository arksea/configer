import { Component, Input, OnInit } from '@angular/core';
import { Config } from '../configer.model';
import { ConfigService } from '../config/config.service';
import {
  WidgetRegistry,
  Validator,
  DefaultWidgetRegistry,
} from 'ngx-schema-form';

@Component({
  selector: 'app-config-form',
  templateUrl: './config-form.component.html',
  providers: [{ provide: WidgetRegistry, useClass: DefaultWidgetRegistry }]
})
export class ConfigFormComponent implements OnInit {
  @Input() config: Config;
  opened = false;
  lineCount: number;
  schema: any;
  constructor(private service: ConfigService, private registry: WidgetRegistry) {
  }

  ngOnInit(): void {
    this.lineCount = this.config.doc.value.split(/\n/).length;
    if (this.lineCount > 25) {
      this.lineCount = 25;
    } else if (this.lineCount < 5) {
      this.lineCount = 5;
    }
    this.schema = JSON.parse(this.config.doc.metadata);
    // this.registry.register('clrCheckbox', ClrCheckboxModule);
  }

  public open(): void {
    this.opened = true;
  }

  public close(): void {
    this.opened = false;
  }

  public saveDoc(): void {
    this.service.updateValue(this.config.id, this.config.doc.id, this.config.doc.value);
  }
}
