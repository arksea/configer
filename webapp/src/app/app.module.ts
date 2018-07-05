import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FormsModule,
         ReactiveFormsModule  // widget要使用formControl必须导入此模块
} from '@angular/forms';

import { SchemaFormModule, SchemaValidatorFactory, ZSchemaValidatorFactory } from 'ngx-schema-form';
import { ClarityModule } from '@clr/angular';

import { AppComponent } from './app.component';
import { AppNotifyDialogComponent } from './app-notify-dialog.component';
import { AppNotifyDialogService } from './app-notify-dialog.service';
import { ConfigerRestAPI } from './configer.restapi';
import { ProjectService } from './project/project.service';
import { ProjectTreeComponent } from './project/project-tree.component';
import { ProjectComponent } from './project/project.component';
import { ConfigService } from './config/config.service';
import { ConfigFormComponent } from './config/config-form.component';
import { ConfigAddFormComponent } from './config/config-add-form.component';
import { ConfigValueFormComponent } from './config/config-value-form.component';
import { SchemaFormComponent } from './schema/schema-form.component';

import { ArBooleanWidgetComponent } from './widget/ar-boolean-widget.component';
import { ArRadioWidgetComponent } from './widget/ar-radio-widget.component';
import { ArCheckboxWidgetComponent } from './widget/ar-checkbox-widget.component';
import { ArSelectWidgetComponent } from './widget/ar-select-widget.component';
import { ArArrayWidgetComponent } from './widget/ar-array-widget.component';

const appRoutes: Routes = [
  // { path: '',   redirectTo: 'projects', pathMatch: 'full' },
  { path: 'projects/:id', component: ProjectComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    AppNotifyDialogComponent,
    ProjectComponent,
    ProjectTreeComponent,
    ConfigValueFormComponent,
    ConfigFormComponent,
    ConfigAddFormComponent,
    SchemaFormComponent,

    ArBooleanWidgetComponent,
    ArRadioWidgetComponent,
    ArCheckboxWidgetComponent,
    ArSelectWidgetComponent,
    ArArrayWidgetComponent
  ],
  entryComponents: [
    ArBooleanWidgetComponent,
    ArRadioWidgetComponent,
    ArCheckboxWidgetComponent,
    ArSelectWidgetComponent,
    ArArrayWidgetComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, { enableTracing: true }),
    FormsModule,
    SchemaFormModule,
    ClarityModule,
    BrowserAnimationsModule,
    ReactiveFormsModule
  ],
  providers: [
    ConfigerRestAPI,
    ProjectService,
    ConfigService,
    AppNotifyDialogService,
    { provide: SchemaValidatorFactory, useClass: ZSchemaValidatorFactory },
    { provide: LocationStrategy, useClass: HashLocationStrategy } // 自动在路由路径前添加#号，部署到Tomcat需要做此转换
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
