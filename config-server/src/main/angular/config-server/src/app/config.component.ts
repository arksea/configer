import { Component,Input,OnInit } from '@angular/core';
import { Config,Project }  from './configer.entity';
import { ConfigerService } from './configer.service';

@Component({
  selector: 'config',
  templateUrl: './config.component.html',
})
export class ConfigComponent implements OnInit {
  @Input()
  config: Config;

  edit: string = "true";
  editBtnText: string = "修改";

  constructor(
      private configService: ConfigerService
  ) {}
  ngOnInit(): void {
  }
  onEdit() {
      if (this.edit=="") {
          this.edit = "true";
          this.editBtnText = "修改";
          this.configService.saveConfigDoc(this.config.doc.id, this.config.doc.value);
      } else {
          this.edit = "";
          this.editBtnText = "保存";
      }
  }
}
