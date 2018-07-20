import { Component, ViewChild, OnInit } from '@angular/core';
import {  ActivatedRoute } from '@angular/router';
import { ProjectComponent } from './project.component';
@Component({
    selector : 'app-projects-container',
    templateUrl: './projects-container.component.html'
})
export class ProjectContainerComponent implements OnInit {
    @ViewChild(ProjectComponent) currentProject: ProjectComponent;
    projectId: number;

  constructor(
    private route: ActivatedRoute
  ) { }

    ngOnInit(): void {
        this.projectId = parseInt(this.route.snapshot.paramMap.get('id'), 10);
      }
}
