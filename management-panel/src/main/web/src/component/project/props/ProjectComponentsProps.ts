import {Project} from "../../../model/Models";

export interface ProjectComponentsProps {
    onProjectUpdate: (project: Project) => void
    onBack: () => void;
}

export interface ProjectCardComponentProps {
    project: Project,
    onAction: (action: ProjectCardMenuAction) => void
}

export enum ProjectCardMenuAction {
    BUILD,
    DELETE
}

