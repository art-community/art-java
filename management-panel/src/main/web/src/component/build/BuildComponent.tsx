import * as React from "react";
import {useState} from "react";
import {SelectProjectComponent} from "./SelectProjectComponent";
import {ProjectAssemblesComponent} from "./ProjectAssemblesComponent";

export const BuildComponent = () => {
    const [selectedProject, selectProject] = useState<Project | null>(null);
    if (selectedProject) {
        return <ProjectAssemblesComponent project={selectedProject} onBack={() => selectProject(null)}/>
    }
    return <SelectProjectComponent onSelect={selectProject}/>;
};