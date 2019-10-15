import * as React from "react";
import {useState} from "react";
import {SelectProjectComponent} from "./SelectProjectComponent";
import { AssembliesComponent } from "./AssembliesComponent";
import {Project} from "../../model/Models";

export const BuildComponent = () => {
    const [selectedProject, selectProject] = useState<Project | null>(null);
    if (selectedProject) {
        return <AssembliesComponent project={selectedProject} onBack={() => selectProject(null)}/>
    }
    return <SelectProjectComponent onSelect={selectProject}/>;
};