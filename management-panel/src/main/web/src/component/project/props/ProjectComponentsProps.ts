export interface ProjectComponentsProps {
    onProjectAdd: (project: Project) => void

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

