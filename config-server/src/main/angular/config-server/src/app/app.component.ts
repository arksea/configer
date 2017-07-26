import { Component,OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export class Project {
  id: number;
  name: string;
  description: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  projects: object;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get('/api/v1/projects').subscribe(data => {
      this.projects = data;
    });
  }
}
