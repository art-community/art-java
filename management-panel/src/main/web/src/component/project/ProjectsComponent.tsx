import * as React from "react";
import {useEffect, useState} from "react";
import {Box, Button, Grid, useTheme} from "@material-ui/core";
import {ProjectCardComponent, ProjectCardMenuAction} from "./ProjectCardComponent";
import {ProjectAddForm} from "./ProjectAddForm";
import {deleteProject, getProjects} from "../../api/PlatformApi";
import {GridSpacing} from "@material-ui/core/Grid";

enum Mode {
    PROJECTS,
    ADD_PROJECT
}

export const ProjectsComponent = () => {
    const [mode, setMode] = useState(Mode.PROJECTS);
    const theme = useTheme();
    const [projects, setProjects] = useState<Map<number, Project>>(new Map());

    useEffect(() => getProjects(setProjects), []);

    const projectAddForm =
        <Box m={theme.spacing(0.5)}>
            <ProjectAddForm onProjectAdd={(project: Project) => {
                setProjects(projects.addValue(project.id, project));
                showProjectsGrid()
            }}/>
        </Box>;

    const handleAction = (action: ProjectCardMenuAction, project: Project) => {
        switch (+action) {
            case ProjectCardMenuAction.BUILD :
                return;
            case ProjectCardMenuAction.DELETE: {
                deleteProject(project.id, () => setProjects(projects.deleteKey(project.id)));
                return;
            }
        }
    };

    const projectsGrid =
        <Box m={theme.spacing(0.5)}>
            <Box mb={theme.spacing(0.5)}>
                <Button color={"primary"}
                        variant={"outlined"}
                        onClick={() => showProjectAddForm()}>
                    Добавить проект
                </Button>
            </Box>
            <Grid container spacing={theme.spacing(1) as GridSpacing}>
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
