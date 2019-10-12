import * as React from "react";
import {Box, Button, Grid} from "@material-ui/core";
import {ProjectCardComponent} from "./ProjectCardComponent";

export const ProjectsComponent = () => {
    return <Box m={5}>
        <Box mb={5}>
            <Button color={"primary"} variant={"outlined"}>
                Добавить проект
            </Button>
        </Box>
        <Grid container spacing={5}>
            <Grid item>
                <ProjectCardComponent/>
            </Grid>
            <Grid item>
                <ProjectCardComponent/>
            </Grid>
            <Grid item>
                <ProjectCardComponent/>
            </Grid>
            <Grid item>
                <ProjectCardComponent/>
            </Grid>
        </Grid>
    </Box>
        ;
};
