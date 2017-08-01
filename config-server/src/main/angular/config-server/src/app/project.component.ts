import { Component,OnInit } from '@angular/core';
import { HttpClient }       from '@angular/common/http';
import { environment }      from 'environments/environment'
import { ConfigerService }    from './configer.service'
import { Config,Project }   from './configer.entity';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'project',
  templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit {
  configs: Config[];
  project: Project = new Project();

  constructor(
      private configService: ConfigerService,
      private router: Router,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
      let id = parseInt(this.route.snapshot.paramMap.get('id'));
      this.configService.getProjectConfigs(id).subscribe(data => {
          this.configs = data;
          this.project = data[0].project;
      });
  }
}

