import { Component,Input } from '@angular/core';
import { Config,Project }   from './configer.entity';

@Component({
  selector: 'config',
  templateUrl: './config.component.html',
})
export class ConfigComponent {
  @Input()
  config: Config;
  edit: string = "true";
  editBtnText: string = "修改";


  onEdit() {
      if (this.edit=="") {
          this.edit = "true";
          this.editBtnText = "修改";
      } else {
          this.edit = "";
          this.editBtnText = "保存";
      }
  }
}
