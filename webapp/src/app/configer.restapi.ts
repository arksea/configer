import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { Project, Config } from './configer.model';


@Injectable()
export class ConfigerRestAPI {
    headers: HttpHeaders;

    public constructor(private http: HttpClient) {
        this.headers = new HttpHeaders();
        this.headers.append('Content-Type', 'application/json; charset=UTF-8');
    }

    private handleCatchedError(error, request: string) {
        const msg = request + ' failed: ' + error.message;
        console.error(msg);
        return new BehaviorSubject(error);
    }

    private handleResult(result, request: string) {
        console.debug(request + ' : ' + result);
        return new BehaviorSubject(result);
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

    public createConfig(config: Config): void {
        this.http.post(environment.apiUrl + '/api/v1/configs', config, { headers: this.headers })
            .subscribe(data => { });
    }
    public createProject(project: Project): void {
        this.http.post(environment.apiUrl + '/api/v1/projects', project, { headers: this.headers })
            .subscribe(data => { });
    }
}
