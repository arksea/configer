import { Component,OnInit }   from '@angular/core';
import { HttpClient }         from '@angular/common/http';
import { RouterModule }       from '@angular/router'
import { environment }        from 'environments/environment'
import { ConfigService }      from './config.service'
import { Project }            from './config.entity';

@Component({
  selector: 'project-list',
  templateUrl: './project-list.component.html'
})
export class ProjectListComponent implements OnInit {

  projects: Project[];

  constructor(private configService: ConfigService) {}

  ngOnInit(): void {
    this.configService.getAllProjects().subscribe(data => {
      this.projects = data;
    });
  }
}
