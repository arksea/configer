import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Project, Config, RestResult } from './configer.model';
import { AppNotifyDialogService } from './app-notify-dialog.service';

@Injectable()
export class ConfigerRestAPI {
    headers: HttpHeaders;

    public constructor(private http: HttpClient, private notify: AppNotifyDialogService ) {
        this.headers = new HttpHeaders();
        this.headers.append('Content-Type', 'application/json; charset=UTF-8');
    }

    public getAllProjects(): Observable<Project[]> {
        const request = 'Request project list';
        return this.http.get(environment.apiUrl + '/api/v1/projects')
            .pipe(
                tap((r: Project[]) => this.handleResult(r, request)),
                catchError(r => this.handleCatchedError(r, request))
            );
    }

    public getProject(prjId: number): Observable<Project> {
        const request = 'Request project prjId=' + prjId;
        return this.http.get(environment.apiUrl + '/api/v1/projects/' + prjId)
            .pipe(
                tap((r: Project) => this.handleResult(r, request)),
                catchError(r => this.handleCatchedError(r, request))
            );
    }

    public getProjectConfigs(prjId: number): Observable<Config[]> {
        const request = 'Request project configs prjId=' + prjId;
        return this.http.get(environment.apiUrl + '/api/v1/projects/' + prjId + '/configs')
            .pipe(
                tap((r: Config[]) => this.handleResult(r, request)),
                catchError(r => this.handleCatchedError(r, request))
            );
    }

    public updateConfigDescription(cfgId: number, configDesc: string): void {
        console.log('cfgId=' + cfgId + ',desc' + configDesc);
        this.http.put(environment.apiUrl + '/api/v1/configs/' + cfgId + '/description',
            configDesc, { headers: this.headers }).subscribe(data => {
                // todo: 错误处理
            });
    }

    public updateConfigDoc(cfgId: number, docId: number, configDoc: string): void {
        console.log('cfgId=' + cfgId + ',docId=' + docId + ',doc=' + configDoc);
        this.http.put(environment.apiUrl + '/api/v1/configs/' + cfgId + '/docs/' + docId,
            configDoc, { headers: this.headers }).subscribe(data => { });
    }

    public updateConfigSchema(cfgId: number, docId: number, configSchema: string): void {
        console.log('cfgId=' + cfgId + ',docId=' + docId + ',schema=' + configSchema);
        this.http.put(environment.apiUrl + '/api/v1/configs/' + cfgId + '/schema/' + docId,
            configSchema, { headers: this.headers }).subscribe(data => { });
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
        const request = 'delete config';
        const url = environment.apiUrl + '/api/v1/projects/' + prjId;
        return this.httpDelete(url, request);
    }

    // -------------------------------------------------------------------

    private httpPost(url: string, body: any, requestMessage: string) {
        return this.http.post(url, body, { headers: this.headers }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private httpPut(url: string, body: any, requestMessage: string) {
        return this.http.put(url, body, { headers: this.headers }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private httpGet(url: string, requestMessage: string) {
        return this.http.get(url, { headers: this.headers }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private httpDelete(url: string, requestMessage: string) {
        return this.http.delete(url, { headers: this.headers }).pipe(
            tap(r => this.handleRestResult(r, requestMessage)),
            catchError(r => this.handleCatchedError(r, requestMessage))
        );
    }

    private handleCatchedError(error, request: string) {
        this.notify.openWidthDescription('Error', request + ' failed', error);
        return new BehaviorSubject(error);
    }

    private handleResult(result, request: string) {
        console.debug(request + ' : ' + result);
        return new BehaviorSubject(result);
    }

    private handleRestResult(result, request: string) {
        if (result.code === 0) {
            console.debug(request + ' : ' + result);
        } else {
            this.notify.openWidthDescription('Error', request + ' failed', result);
        }
        return new BehaviorSubject(result);
    }
}
