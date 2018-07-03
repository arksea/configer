import { Component, Input, OnInit } from '@angular/core';
import { Config } from '../configer.model';
import { ConfigService } from '../config/config.service';

@Component({
  selector: 'app-schema-form',
  templateUrl: './schema-form.component.html'
})
export class SchemaFormComponent implements OnInit {
  @Input() config: Config;
  opened = false;
  lineCount: number;

  constructor(private service: ConfigService) {
  }

  ngOnInit(): void {
    this.lineCount = this.config.doc.value.split(/\,|\n|}|{/).length;
    if (this.lineCount > 25) {
      this.lineCount = 25;
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

  public saveSchema(): void {
    this.service.updateSchema(this.config.id, this.config.doc.id, this.config.doc.metadata);
    this.opened = false;
  }

}
