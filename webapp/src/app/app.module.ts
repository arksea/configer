import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { SchemaFormModule, SchemaValidatorFactory, ZSchemaValidatorFactory } from 'ngx-schema-form';
import { ClarityModule } from '@clr/angular';


import { ConfigerRestAPI } from './configer.restapi';
import { ProjectService } from './project/project.service';
import { ProjectTreeComponent } from './project/project-tree.component';
import { ProjectComponent } from './project/project.component';
import { ConfigService } from './config/config.service';
import { ConfigComponent } from './config/config.component';
import { ConfigFormComponent } from './config/config-form.component';
import { SchemaFormComponent } from './schema/schema-form.component';

const appRoutes: Routes = [
  // { path: '',   redirectTo: 'projects', pathMatch: 'full' },
  { path: 'projects/:id', component: ProjectComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    ProjectComponent,
    ProjectTreeComponent,
    ConfigComponent,
    ConfigFormComponent,
    SchemaFormComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, { enableTracing: true }),
    FormsModule,
    SchemaFormModule,
    ClarityModule,
    BrowserAnimationsModule
  ],
  providers: [
    ConfigerRestAPI,
    ProjectService,
    ConfigService,
    { provide: SchemaValidatorFactory, useClass: ZSchemaValidatorFactory },
    { provide: LocationStrategy, useClass: HashLocationStrategy } // 自动在路由路径前添加#号，部署到Tomcat需要做此转换
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
