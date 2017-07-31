import { BrowserModule }        from '@angular/platform-browser';
import { NgModule }             from '@angular/core';
import { FormsModule }          from '@angular/forms'
import { RouterModule, Routes } from '@angular/router'
import { HttpClientModule }     from '@angular/common/http';

import { AppComponent }         from './app.component';
import { ProjectListComponent } from './project-list.component'
import { ProjectComponent }     from './project.component'
import { ConfigComponent }      from './config.component'
import { ConfigerService }      from './configer.service'

const appRoutes: Routes = [
  { path: '',   redirectTo: '/projects', pathMatch: 'full' },
  { path: 'projects',     component: ProjectListComponent },
  { path: 'projects/:id', component: ProjectComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    ProjectListComponent,
    ProjectComponent,
    ConfigComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes,{ enableTracing: true })
  ],
  providers: [ConfigerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
