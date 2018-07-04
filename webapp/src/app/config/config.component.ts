import { Component, Input, OnInit } from '@angular/core';
import { Config } from '../configer.model';
import { ConfigService } from '../config/config.service';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
})
export class ConfigComponent implements OnInit {
  config: Config;
  opened = false;
  lineCount = 10;
  json = '';

  constructor(private service: ConfigService) {
  }

  ngOnInit(): void {
  }

  public open(config: Config): void {
    this.config = config;
    this.lineCount = this.config.doc.value.split(/\,|\n|\}|\{/).length;
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
    this.config.doc.value = this.json;
    this.service.updateValue(this.config.id, this.config.doc.id, this.json);
    this.opened = false;
  }

}
