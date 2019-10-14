import * as React from "react";
import {useEffect, useState} from "react";
import {Box, Button, Grid, useTheme} from "@material-ui/core";
import {ProjectCardComponent} from "./ProjectCardComponent";
import {ProjectAddFormComponent} from "./ProjectAddFormComponent";
import {deleteProject, getProjects} from "../../api/PlatformApi";
import {GridSpacing} from "@material-ui/core/Grid";
import {ProjectCardMenuAction} from "./props/ProjectComponentsProps";
import {useHistory} from "react-router";
import {AUTHORIZE_PATH, TOKEN_COOKIE} from "../../constants/Constants";
// @ts-ignore
import Cookies from "js-cookie";

enum Mode {
    PROJECTS,
    ADD_PROJECT
}

export const ProjectsComponent = () => {
    const [mode, setMode] = useState(Mode.PROJECTS);
    const theme = useTheme();
    const [projects, setProjects] = useState<Map<number, Project>>(new Map<number, Project>());
    const history = useHistory();
    useEffect(() => getProjects(setProjects, () => {
        Cookies.remove(TOKEN_COOKIE);
        history.push(AUTHORIZE_PATH)
    }), []);

    const handleAction = (action: ProjectCardMenuAction, project: Project) => {
        switch (+action) {
            case ProjectCardMenuAction.BUILD :
                return;
            case ProjectCardMenuAction.DELETE: {
                deleteProject(project.id);
                setProjects(projects.deleteKey(project.id));
                return;
            }
        }
    };

    const projectAddForm = <ProjectAddFormComponent
        onProjectAdd={(project: Project) => {
            setProjects(projects.addValue(project.id, project));
            showProjectsGrid()
        }}
        onBack={() => showProjectsGrid()}
    />;

    const createProjectCards = () => projects.mapValuesToArray(project =>
        <Grid key={project.name} item>
            <ProjectCardComponent project={project} onAction={action => handleAction(action, project)}/>
        </Grid>
    );

    const projectsGrid = <Box m={theme.spacing(0.5)}>
        <Box mb={theme.spacing(0.5)}>
            <Button color={"primary"}
                    variant={"outlined"}
                    onClick={() => showProjectAddForm()}>
                Добавить проект
            </Button>
        </Box>
        <Grid container spacing={theme.spacing(1) as GridSpacing}>
            {createProjectCards()}
        </Grid>
    </Box>;

    const showProjectAddForm = () => setMode(Mode.ADD_PROJECT);
    const showProjectsGrid = () => setMode(Mode.PROJECTS);

    return mode == Mode.PROJECTS ? projectsGrid : projectAddForm
};
