import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Project, Config, RestResult, LoginInfo, SignupInfo, ResultCode, ProjectUser } from './configer.model';
import { AppNotifyDialogService } from './app-notify-dialog.service';

@Injectable()
export class ConfigerRestAPI {
    headers: HttpHeaders;

    public constructor(private http: HttpClient, private notify: AppNotifyDialogService, private router: Router) {
        this.headers = new HttpHeaders();
        this.headers.append('Content-Type', 'application/json; charset=UTF-8');
    }

    public getAllProjects(): Observable<RestResult<Project[]>> {
        const request = 'Request project list';
        const url = environment.apiUrl + '/api/v1/projects';
        return this.httpGet(url, request);
    }

    public getProject(prjId: number): Observable<RestResult<Project>> {
        const request = 'Request project';
        return this.httpGet(environment.apiUrl + '/api/v1/projects/' + prjId, request);
    }

    public getProjectConfigs(prjId: number): Observable<RestResult<Config[]>> {
        const request = 'Request project configs';
        return this.httpGet(environment.apiUrl + '/api/v1/projects/' + prjId + '/configs', request);
    }

    public getProjectUsers(prjId: number): Observable<RestResult<ProjectUser[]>> {
        const request = 'Request project users';
        return this.httpGet(environment.apiUrl + '/api/v1/projects/' + prjId + '/users', request);
    }

    public updateProjectUser(prjId: number, user: ProjectUser): Observable<RestResult<string>> {
        const request = 'Update project user auth';
        return this.httpPut(environment.apiUrl + '/api/v1/projects/' + prjId + '/users/' + user.userId, user, request);
    }

    public addProjectUser(prjId: number, user: ProjectUser): Observable<RestResult<string>> {
        const request = 'Add project user auth';
        return this.httpPost(environment.apiUrl + '/api/v1/projects/' + prjId + '/users', user, request);
    }

    public delProjectUser(prjId: number, userId: number): Observable<RestResult<string>> {
        const request = 'Add project user auth';
        return this.httpDelete(environment.apiUrl + '/api/v1/projects/' + prjId + '/users/' + userId, request);
    }

    public updateConfigDescription(cfgId: number, configDesc: string): Observable<RestResult<string>> {
        const request = 'update config description';
        console.log(request + ': cfgId=' + cfgId + ',desc' + configDesc);
        const url = environment.apiUrl + '/api/v1/configs/' + cfgId + '/description';
        return this.httpPut(url, configDesc, request);
    }

    public updateConfigDoc(cfgId: number, docId: number, configDoc: string): Observable<RestResult<string>> {
        const request = 'update config doc';
        console.log(request + ': cfgId=' + cfgId + ',docId=' + docId + ',doc=' + configDoc);
        const url = environment.apiUrl + '/api/v1/configs/' + cfgId + '/docs/' + docId;
        return this.httpPut(url, configDoc, request);
    }

    public updateConfigSchema(cfgId: number, docId: number, configSchema: string): Observable<RestResult<string>> {
        const request = 'update config schema';
        console.log(request + ': cfgId=' + cfgId + ',docId=' + docId + ',schema=' + configSchema);
        const url = environment.apiUrl + '/api/v1/configs/' + cfgId + '/schema/' + docId;
        return this.httpPut(url, configSchema, request);
    }

    public createConfig(config: Config):  Observable<RestResult<Config>> {
        const request = 'create new config';
        const url = environment.apiUrl + '/api/v1/configs';
        return this.httpPost(url, config, request);
    }

    public createProject(project: Project): Observable<RestResult<number>> {
        const request = 'create new project';
        const url = environment.apiUrl + '/api/v1/projects';
        return this.httpPost(url, project, request);
    }

    public deleteConfig(cfgId: number): Observable<RestResult<string>> {
        const request = 'delete config';
        const url = environment.apiUrl + '/api/v1/configs/' + cfgId;
        return this.httpDelete(url, request);
    }

    public deleteProject(prjId: number): Observable<RestResult<string>> {
        const request = 'delete project';
        const url = environment.apiUrl + '/api/v1/projects/' + prjId;
        return this.httpDelete(url, request);
    }

    // -------------------------------------------------------------------
    // account
    public userLogin(info: LoginInfo): Observable<RestResult<string>> {
        const request = 'user login';
        const url = environment.apiUrl + '/api/v1/login';
        return this.httpPost(url, info , request);
    }

    public userSignup(info: SignupInfo): Observable<RestResult<string>> {
        const request = 'user login';
        const url = environment.apiUrl + '/api/v1/signup';
        return this.httpPost(url, info , request);
    }

    // -------------------------------------------------------------------
    // http mathods
    private httpPostWithOptions(url: string, body: any, requestMessage: string, options: object) {
        return this.http.post(url, body, options).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private httpPost(url: string, body: any, requestMessage: string) {
        return this.http.post(url, body, { headers: this.headers, withCredentials: true }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private httpPut(url: string, body: any, requestMessage: string) {
        return this.http.put(url, body, { headers: this.headers, withCredentials: true }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private httpGet(url: string, requestMessage: string) {
        return this.http.get(url, { headers: this.headers, withCredentials: true }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private httpDelete(url: string, requestMessage: string) {
        return this.http.delete(url, { headers: this.headers, withCredentials: true }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private handleCatchedError(errorRespond, request: string) {
        console.debug(errorRespond);
        this.notify.openWidthDescription('Catched Error', request + ' failed', errorRespond.error.error);
        return new BehaviorSubject(errorRespond.error);
    }

    private handleRestResult(result, request: string) {
        if (result.code === 0) {
            console.debug(request + ' : ' + result);
        } else if (result.code === ResultCode.TOKEN_EXPIRED) {
            this.router.navigate(['/login']);
        } else {
            this.notify.openWidthDescription('Reault Error', request + ' failed', result.error);
        }
        return new BehaviorSubject(result);
    }
}
