import { Component,OnInit }   from '@angular/core';
import { HttpClient }         from '@angular/common/http';
import { RouterModule }       from '@angular/router'
import { environment }        from 'environments/environment'
import { ConfigerService }    from './configer.service'
import { Project }            from './configer.entity';

@Component({
  selector: 'project-list',
  templateUrl: './project-list.component.html'
})
export class ProjectListComponent implements OnInit {

  projects: Project[];

  constructor(private configerService: ConfigerService) {}

  ngOnInit(): void {
    this.configerService.getAllProjects().subscribe(data => {
      this.projects = data;
    });
  }
}
