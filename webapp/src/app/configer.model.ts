export interface RestResult<T> {
    code: number;
    result?: T;
    error?: string;
    reqid: string;
  }

export class Project {
    id: number;
    name: string;
    profile: string;
    description: string;
}

export class ConfigDoc {
    id: number;
    value: string;
    metadata: string;
}

export class Config {
    id: number;
    project: Project;
    name: string;
    description: string;
    doc: ConfigDoc;
}

export class LoginInfo {
    name: string;
    password: string;
}

export class SignupInfo {
    name: string;
    email: string;
    password: string;
}
