export class Project {
    id: number;
    name: string;
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

