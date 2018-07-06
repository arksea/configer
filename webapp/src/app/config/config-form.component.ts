import { Component, Input, OnInit } from '@angular/core';
import { Config } from '../configer.model';
import { ConfigService } from '../config/config.service';
import { WidgetRegistry } from 'ngx-schema-form';
import { ArWidgetRegistry } from '../widget/ar-widget-registry';

@Component({
  selector: 'app-config-form',
  templateUrl: './config-form.component.html',
  providers: [{ provide: WidgetRegistry, useClass: ArWidgetRegistry }]
})
export class ConfigFormComponent {
  config: Config;
  opened = false;
  lineCount = 5;
  schema: any;
  model: any;
  value: any;
  constructor(private service: ConfigService, private registry: WidgetRegistry) {
  }

  public open(config: Config): void {
    this.config = config;
    this.lineCount = this.config.doc.value.split(/\n/).length + 5;
    if (this.lineCount > 25) {
      this.lineCount = 25;
    } else if (this.lineCount < 5) {
      this.lineCount = 5;
    }
    this.schema = JSON.parse(this.config.doc.metadata);
    this.model = JSON.parse(this.config.doc.value);
    this.opened = true;
  }

  public close(): void {
    this.opened = false;
  }

  public saveDoc(): void {
    const json = JSON.stringify(this.value);
    this.service.updateValue(this.config.id, this.config.doc.id, json);
    this.opened = false;
  }

  public setValue(value: any): void {
    this.value = value;
  }

  public logErrors(errors): void {
    if (errors) {
      console.debug('ERRORS', errors);
    }
  }
}
