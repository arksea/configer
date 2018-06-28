import { Component, Input, OnInit } from '@angular/core';
import { Config, Project } from '../configer.model';
import { ConfigerRestAPI } from '../configer.restapi';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
})
export class ConfigComponent implements OnInit {
  @Input() config: Config;
  docLineCount: number;
  hideContent: boolean;
  showBtnText: string;

  constructor(private service: ConfigerRestAPI) {}

  ngOnInit(): void {
      this.hideContent = true;
      this.showBtnText = 'Show';
      this.docLineCount = this.config.doc.value.split(/\n/).length;
      if (this.docLineCount > 30) {
          this.docLineCount = 30;
      }
  }

  onShow(event) {
    this.hideContent = !this.hideContent;
    this.showBtnText = this.hideContent ? 'Show' : 'Hide';
  }
}
