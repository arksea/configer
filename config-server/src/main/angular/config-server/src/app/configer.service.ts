import { Injectable }   from '@angular/core';
import { HttpClient }   from '@angular/common/http';
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
}

