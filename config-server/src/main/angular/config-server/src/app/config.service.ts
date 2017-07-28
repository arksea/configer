import { Injectable }   from '@angular/core';
import { HttpClient }   from '@angular/common/http';
import { environment }  from 'environments/environment';
import { Observable }   from 'rxjs/Observable';
import { Project, Config, ConfigDoc } from './config.entity';

@Injectable()
export class ConfigService {
    constructor(private http: HttpClient) {}
    getAllProjects(): Observable<Project[]> {
        return this.http.get(environment.apiUrl+'/api/v1/projects');
    }
    getProjectConfigs(prjId: number): Observable<Config[]> {
        return this.http.get(environment.apiUrl+'/api/v1/projects/'+prjId);
    }
}
