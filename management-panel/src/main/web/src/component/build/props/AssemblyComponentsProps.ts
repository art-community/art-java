import {Assembly, Project} from "../../../model/Models";

export interface AssembliesComponentProps {
    project: Project,
    onBack: () => void
}


export interface AssemblyCardComponentProps {
    assembly: Assembly
}
