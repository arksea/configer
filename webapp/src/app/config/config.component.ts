import { Component, Input, ViewChild, OnInit } from '@angular/core';
import { Config, Project } from '../configer.model';
import { ConfigerRestAPI } from '../configer.restapi';
import { SchemaFormComponent } from '../schema/schema-form.component';
import { ConfigFormComponent } from './config-form.component';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
})
export class ConfigComponent implements OnInit {
  @Input() config: Config;
  @ViewChild(SchemaFormComponent) dialogSchemaForm: SchemaFormComponent;
  @ViewChild(ConfigFormComponent) dialogConfigForm: ConfigFormComponent;
  docLineCount: number;
  hideContent: boolean;
  showBtnText: string;


  constructor() {}

  ngOnInit(): void {
      this.hideContent = true;
      this.showBtnText = 'Show';
      this.docLineCount = this.config.doc.value.split(/\,|\n|}|{/).length + 3;
      if (this.docLineCount > 30) {
        this.docLineCount = 30;
      } else if (this.docLineCount < 5) {
        this.docLineCount = 5;
      }
  }

  onBtnShowClick() {
    this.hideContent = !this.hideContent;
    this.showBtnText = this.hideContent ? 'Show' : 'Hide';
  }

  onBtnSchemaClick() {
    this.dialogSchemaForm.open();
  }

  onBtnEditClick() {
    this.dialogConfigForm.open();
  }

  getConfigObj() {
    return JSON.parse(this.config.doc.value);
  }
}
