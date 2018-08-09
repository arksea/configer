import { Component, Input, OnInit } from '@angular/core';
import { Config } from '../configer.model';
import { ConfigService } from '../config/config.service';

@Component({
  selector: 'app-schema-form',
  templateUrl: './schema-form.component.html',
  styles: ['.sample {font-size: 65%; text-align:right;}']
})
export class SchemaFormComponent implements OnInit {
  config: Config;
  opened = false;
  lineCount = 10;
  schema = '';

  constructor(private service: ConfigService) {
  }

  ngOnInit(): void {
  }

  public open(config: Config): void {
    this.config = config;
    this.lineCount = this.config.doc.metadata.split(/\,|\n|\}|\{/).length;
    if (this.lineCount > 25) {
      this.lineCount = 25;
    } else if (this.lineCount < 10) {
      this.lineCount = 10;
    }
    this.opened = true;
    this.schema = config.doc.metadata;
  }

  public close(): void {
    this.opened = false;
  }

  public saveSchema(): void {
    this.service.updateSchema(this.config.id, this.config.doc.id, this.schema);
    this.opened = false;
  }

}
