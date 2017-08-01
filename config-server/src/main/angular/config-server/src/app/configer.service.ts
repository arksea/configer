import { Injectable }   from '@angular/core';
import { HttpClient,HttpHeaders }   from '@angular/common/http';
import { environment }  from 'environments/environment';
import { Observable }   from 'rxjs/Observable';
import { Project, Config, ConfigDoc } from './configer.entity';


@Injectable()
export class ConfigerService {
    constructor(private http: HttpClient) {}
    getAllProjects(): Observable<Project[]> {
        return this.http.get(environment.apiUrl+'/api/v1/projects');
    }
    getProjectConfigs(prjId: number): Observable<Config[]> {
        return this.http.get(environment.apiUrl+'/api/v1/projects/'+prjId);
    }
    saveConfigDoc(docId: number, configDoc: string) : void {
        console.log('id='+docId+',doc='+configDoc);
        let headers = new HttpHeaders();
        headers.append('Content-Type', 'application/json; charset=UTF-8');
        this.http.post(environment.apiUrl+'/api/v1/configs/doc/'+docId,
                       configDoc,{headers: headers}).subscribe(data => {});
    }
}
