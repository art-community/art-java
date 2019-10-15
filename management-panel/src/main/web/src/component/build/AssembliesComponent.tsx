import * as React from "react";
import {useEffect, useState} from "react";
import {Box, Button, Grid, IconButton, Typography, useTheme} from "@material-ui/core";
import {ArrowBackOutlined} from "@material-ui/icons";
import {AssemblyCardComponent} from "./AssemblyCardComponent";
import {AssembliesComponentProps} from "./props/AssemblyComponentsProps";
import {getAssemblies} from "../../api/PlatformApi";
import {Assembly} from "../../model/Models";

export const AssembliesComponent = (props: AssembliesComponentProps) => {
    const theme = useTheme();
    const [assemblies, setAssemblies] = useState(new Map<number, Assembly>());
    useEffect(() => getAssemblies(setAssemblies), []);
    return <Grid>
        <Grid item>
            <IconButton onClick={() => props.onBack()}>
                <ArrowBackOutlined color={"secondary"}/>
            </IconButton>
        </Grid>
        <Box m={theme.spacing(0.3)}>
            <Box mb={theme.spacing(0.5)}>
                <Grid item>
                    <Typography color={"secondary"} variant="h5" component="h5">
                        Сборки проекта «{props.project.title}‎»
                    </Typography>
                </Grid>
            </Box>
            <Grid container spacing={3}>
                {assemblies
                    .mapValuesToArray(assembly => assembly)
                    .sort((current, next) => current.state > next.state ? 1 : -1)
                    .map(assembly => <Grid item><AssemblyCardComponent assembly={assembly}/></Grid>)}
            </Grid>
            <Box mt={theme.spacing(0.5)}>
                <Grid item>
                    <Button variant={"outlined"} color={"primary"}>Собрать</Button>
                </Grid>
            </Box>
        </Box>
    </Grid>
};