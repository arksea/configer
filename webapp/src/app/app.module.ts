import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { SchemaFormModule, SchemaValidatorFactory, ZSchemaValidatorFactory } from 'ngx-schema-form';
import { ClarityModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { ConfigerRestAPI } from './configer.restapi';
import { ProjectListComponent } from './project/project-list.component';

const appRoutes: Routes = [
  { path: '',   redirectTo: 'projects', pathMatch: 'full' },
  { path: 'projects',     component: ProjectListComponent },
  // { path: 'projects/:id', component: ProjectComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    ProjectListComponent
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
    { provide: SchemaValidatorFactory, useClass: ZSchemaValidatorFactory }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
