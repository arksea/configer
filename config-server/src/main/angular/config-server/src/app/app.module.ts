import { BrowserModule }        from '@angular/platform-browser';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule }     from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy} from '@angular/common';
import { FormsModule,ReactiveFormsModule }          from '@angular/forms';
import {
  MdAutocompleteModule,
  MdButtonModule,
  MdButtonToggleModule,
  MdCardModule,
  MdCheckboxModule,
  MdChipsModule,
  MdCoreModule,
  MdDatepickerModule,
  MdDialogModule,
  MdExpansionModule,
  MdGridListModule,
  MdIconModule,
  MdInputModule,
  MdListModule,
  MdMenuModule,
  MdNativeDateModule,
  MdPaginatorModule,
  MdProgressBarModule,
  MdProgressSpinnerModule,
  MdRadioModule,
  MdRippleModule,
  MdSelectModule,
  MdSidenavModule,
  MdSliderModule,
  MdSlideToggleModule,
  MdSnackBarModule,
  MdSortModule,
  MdTableModule,
  MdTabsModule,
  MdToolbarModule,
  MdTooltipModule
} from '@angular/material';
import {CdkTableModule} from '@angular/cdk';

import { AppComponent }         from './app.component';
import { AppNavbarComponent }   from './app-navbar.component';
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
  exports: [
    CdkTableModule,
    MdAutocompleteModule,
    MdButtonModule,
    MdButtonToggleModule,
    MdCardModule,
    MdCheckboxModule,
    MdChipsModule,
    MdCoreModule,
    MdDatepickerModule,
    MdDialogModule,
    MdExpansionModule,
    MdGridListModule,
    MdIconModule,
    MdInputModule,
    MdListModule,
    MdMenuModule,
    MdNativeDateModule,
    MdPaginatorModule,
    MdProgressBarModule,
    MdProgressSpinnerModule,
    MdRadioModule,
    MdRippleModule,
    MdSelectModule,
    MdSidenavModule,
    MdSliderModule,
    MdSlideToggleModule,
    MdSnackBarModule,
    MdSortModule,
    MdTableModule,
    MdTabsModule,
    MdToolbarModule,
    MdTooltipModule,
  ]
})
export class PlunkerMaterialModule {}

@NgModule({
  declarations: [
    AppComponent,
    AppNavbarComponent,
    ProjectListComponent,
    ProjectComponent,
    ProjectFormComponent,
    ConfigComponent,
    ConfigFormComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes,{ enableTracing: true }),
    FormsModule,
    HttpClientModule,
    PlunkerMaterialModule,
    MdNativeDateModule,
    ReactiveFormsModule
  ],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}, //自动在路由路径前添加#号，部署到Tomcat需要做此转换
              ConfigerService],
  bootstrap: [AppComponent]
})
export class AppModule { }

