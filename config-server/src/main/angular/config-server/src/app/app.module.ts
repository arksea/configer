import { BrowserModule }        from '@angular/platform-browser';
import { NgModule }             from '@angular/core';
import { FormsModule }          from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule }     from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy} from '@angular/common';

import { AppComponent }         from './app.component';
import { ProjectListComponent } from './project-list.component';
import { ProjectComponent }     from './project.component';
import { ProjectFormComponent } from './project-form.component';
import { ConfigFormComponent }  from './config-form.component';
import { ConfigComponent }      from './config.component';
import { ConfigerService }      from './configer.service';

const appRoutes: Routes = [
  { path: '',   redirectTo: 'projects', pathMatch: 'full' },
  { path: 'projects',     component: ProjectListComponent },
  { path: 'projects/:id', component: ProjectComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    ProjectListComponent,
    ProjectComponent,
    ProjectFormComponent,
    ConfigComponent,
    ConfigFormComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes,{ enableTracing: true }),
    FormsModule,
    HttpClientModule
  ],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}, //自动在路由路径前添加#号，部署到Tomcat需要做此转换
              ConfigerService],
  bootstrap: [AppComponent]
})
export class AppModule { }

