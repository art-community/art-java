import * as React from "react";
import {useState} from "react";
import {Box, Button, Grid} from "@material-ui/core";
import {ProjectCardComponent, ProjectCardMenuAction} from "./ProjectCardComponent";
import {ProjectAddForm} from "./ProjectAddForm";

enum Mode {
    PROJECTS,
    ADD_PROJECT
}

export const ProjectsComponent = () => {
    const [mode, setMode] = useState(Mode.PROJECTS);
    const [projects, setProjects] = useState<Map<string, Project>>(new Map());

    const projectAddForm =
        <Box m={5}>
            <ProjectAddForm onProjectAdd={(project: Project) => {
                setProjects(projects.addValue(project.name, project));
                showProjectsGrid()
            }}/>
        </Box>;

    const handleAction = (action: ProjectCardMenuAction, project: Project) => {
        switch (+action) {
            case ProjectCardMenuAction.BUILD :
                return;
            case ProjectCardMenuAction.DELETE: {
                setProjects(projects.deleteKey(project.name));
                return;
            }
        }
    };

    const projectsGrid =
        <Box m={5}>
            <Box mb={5}>
                <Button color={"primary"}
                        variant={"outlined"}
                        onClick={() => showProjectAddForm()}>
                    Добавить проект
                </Button>
            </Box>
            <Grid container spacing={5}>
                {projects.mapValuesToArray(project =>
                    <Grid key={project.name} item>
                        <ProjectCardComponent project={project} onAction={action => handleAction(action, project)}/>
                    </Grid>
                )}
            </Grid>
        </Box>;

    const showProjectAddForm = () => {
        setMode(Mode.ADD_PROJECT)
    };

    const showProjectsGrid = () => {
        setMode(Mode.PROJECTS)
    };

    return mode == Mode.PROJECTS ? projectsGrid : projectAddForm
};
