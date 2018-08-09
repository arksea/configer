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
import { AppHeaderComponent } from './app-header.component';
import { AppNotifyDialogComponent } from './app-notify-dialog.component';
import { AppNotifyDialogService } from './app-notify-dialog.service';
import { AccountService } from './account/account.service';
import { AuthGuard } from './account/auth-guard';
import { SignInComponent } from './account/sign-in.component';
import { SignUpComponent } from './account/sign-up.component';
import { ConfigerRestAPI } from './configer.restapi';
import { ProjectContainerComponent } from './project/projects-container.component';
import { ProjectService } from './project/project.service';
import { ProjectTreeComponent } from './project/project-tree.component';
import { ProjectComponent } from './project/project.component';
import { ProjectNewFormComponent } from './project/project-new-form.component';
import { ProjectUserAuthFormComponent } from './project/project-user-auth-form.component';
import { ProjectUserSelectFormComponent } from './project/project-user-select-form.component';
import { ProjectUserNameFormComponent } from './project/project-user-name-form.component';
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
  { path: '',   redirectTo: 'projects', pathMatch: 'full' },
  { path: 'login',   component: SignInComponent },
  { path: 'signup',   component: SignUpComponent },
  { path: 'projects', component: ProjectContainerComponent, canActivate: [AuthGuard]},
  { path: 'projects/:id', component: ProjectContainerComponent, canActivate: [AuthGuard]}
];

@NgModule({
  declarations: [
    AppComponent,
    AppHeaderComponent,
    AppNotifyDialogComponent,
    SignInComponent,
    SignUpComponent,
    ProjectContainerComponent,
    ProjectComponent,
    ProjectTreeComponent,
    ProjectNewFormComponent,
    ProjectUserAuthFormComponent,
    ProjectUserSelectFormComponent,
    ProjectUserNameFormComponent,
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
    AccountService,
    AuthGuard,
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
