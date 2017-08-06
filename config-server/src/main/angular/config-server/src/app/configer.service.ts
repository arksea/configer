import { Injectable }   from '@angular/core';
import { HttpClient,HttpHeaders }   from '@angular/common/http';
import { environment }  from 'environments/environment';
import { Observable }   from 'rxjs/Observable';
import { Project, Config, ConfigDoc } from './configer.entity';


@Injectable()
export class ConfigerService {
    headers: HttpHeaders;

    public constructor(private http: HttpClient) {
        this.headers = new HttpHeaders();
        this.headers.append('Content-Type', 'application/json; charset=UTF-8');
    }

    public getAllProjects(): Observable<Project[]> {
        return this.http.get(environment.apiUrl+'/api/v1/projects');
    }

    public getProject(prjId: number): Observable<Project> {
      return this.http.get(environment.apiUrl+'/api/v1/projects/'+prjId);
    }

    public getProjectConfigs(prjId: number): Observable<Config[]> {
        return this.http.get(environment.apiUrl+'/api/v1/projects/'+prjId+'/configs');
    }

    public updateConfigDescription(cfgId: number, configDesc: string) : void {
        console.log('cfgId='+cfgId+',desc'+configDesc);
        this.http.put(environment.apiUrl+'/api/v1/configs/'+cfgId+'/description',
                      configDesc,{headers: this.headers}).subscribe(data => {
                        //todo: 错误处理
                      });
    }

    public updateConfigDoc(cfgId: number, docId: number, configDoc: string) : void {
        console.log('cfgId='+cfgId+',docId='+docId+',doc='+configDoc);
        this.http.put(environment.apiUrl+'/api/v1/configs/'+cfgId+'/docs/'+docId,
                       configDoc,{headers: this.headers}).subscribe(data => {});
    }

    public createConfig(config: Config) : void {
        this.http.post(environment.apiUrl+"/api/v1/configs", config, {headers: this.headers})
                 .subscribe(data => {});
    }
    public createProject(project: Project) : void {
        this.http.post(environment.apiUrl+"/api/v1/projects", project, {headers: this.headers})
                 .subscribe(data => {});
    }
}
