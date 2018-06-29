import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject, Observable, ObjectUnsubscribedError } from 'rxjs';
import { ConfigerRestAPI } from '../configer.restapi';
import { Config } from '../configer.model';

@Injectable()
export class ConfigService {
    selectedConfigs: Subject<Config[]> = new BehaviorSubject<Config[]>([]);

    constructor(private api: ConfigerRestAPI) {
    }

    public selectProject(id: number): void {
        this.api.getProjectConfigs(id).subscribe(configs => {
            this.selectedConfigs.next(configs);
        });
    }
}

