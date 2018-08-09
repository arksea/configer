import { Component, Input, OnInit } from '@angular/core';
import { Config } from '../configer.model';
import { ConfigService } from '../config/config.service';

@Component({
  selector: 'app-config-value-form',
  templateUrl: './config-value-form.component.html',
})
export class ConfigValueFormComponent implements OnInit {
  config: Config = <Config>{ name: '' };
  opened = false;
  lineCount = 10;
  json = '""';

  constructor(private service: ConfigService) {
  }

  ngOnInit(): void {
  }

  public open(config: Config): void {
    this.config = config;
    this.lineCount = this.config.doc.value.split(/[\[\]\,\n\{\}]/).length * 1.2;
    if (this.lineCount > 25) {
      this.lineCount = 25;
    } else if (this.lineCount < 5) {
      this.lineCount = 5;
    }
    this.opened = true;
    this.json = config.doc.value;
  }

  public close(): void {
    this.opened = false;
  }

  public saveJson(): void {
    this.service.updateValue(this.config.id, this.config.doc.id, this.json);
    this.opened = false;
  }

  public getJsonObject() {
     return JSON.parse(this.json);
  }

}
