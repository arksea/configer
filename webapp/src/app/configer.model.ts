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

export interface ProjectUser {
    query: boolean;
    manage: boolean;
    config: boolean;
    userId: number;
    userName: string;
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

export class ResultCode {
    public static SUCCEED = 0;
    public static FAILED = 1;
    public static TOKEN_EXPIRED = 3;
    public static NO_AUTHORITY = 4;
    public static NOT_EXISTS = 5;
}
