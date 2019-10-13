import * as React from "react";
import {useState} from "react";
import {Box, Button, Grid, IconButton, Popper, TextField, Typography, useTheme} from "@material-ui/core";
import {addProject} from "../../api/PlatformApi";
import {ArrowBackOutlined} from "@material-ui/icons";
import {ProjectComponentsProps} from "./props/ProjectComponentsProps";


export const ProjectAddFormComponent = (props: ProjectComponentsProps) => {
    const [name, setName] = useState("");
    const [gitUrl, setGitUrl] = useState("");
    const [jiraUrl, setJiraUrl] = useState("");
    const [showProjectExistsError, setShowProjectExistsError] = useState(false);
    const [projectExistsErrorMessageAnchor, setProjectExistsErrorMessageAnchor] = useState<null | HTMLElement>(null);
    const theme = useTheme();
    return <Grid direction={"column"}>
        <Grid item>
            <IconButton onClick={() => props.onBack()}>
                <ArrowBackOutlined color={"secondary"}/>
            </IconButton>
        </Grid>
        <Grid item>
            <form noValidate>
                <Box m={theme.spacing(0.5)}>
                    <Typography color={"secondary"} variant="h5" component="h5">
                        Новый проект
                    </Typography>
                    <TextField
                        variant={'outlined'}
                        margin={'normal'}
                        required
                        label={'Имя проекта'}
                        onChange={event => {
                            setShowProjectExistsError(false);
                            setName(event.target.value)
                        }}
                        fullWidth
                    />
                    <TextField
                        variant={'outlined'}
                        margin={'normal'}
                        required
                        label={'URL git репозитория'}
                        onChange={event => {
                            setShowProjectExistsError(false);
                            setGitUrl(event.target.value)
                        }}
                        fullWidth
                    />
                    <TextField
                        variant={'outlined'}
                        margin={'normal'}
                        label={'URL JIRA проекта'}
                        onChange={event => {
                            setShowProjectExistsError(false);
                            setJiraUrl(event.target.value)
                        }}
                        fullWidth
                    />
                    <Box mt={theme.spacing(0.5)}>
                        <Button fullWidth
                                ref={ref => setProjectExistsErrorMessageAnchor(ref)}
                                variant={'outlined'}
                                color={'primary'}
                                onClick={() => {
                                    setShowProjectExistsError(false);
                                    addProject({
                                            name: name,
                                            gitUrl: gitUrl,
                                            jiraUrl: jiraUrl
                                        },
                                        props.onProjectAdd,
                                        () => setShowProjectExistsError(true))
                                }}>
                            Добавить
                        </Button>
                        <Popper open={showProjectExistsError && Boolean(projectExistsErrorMessageAnchor)}
                                placement={"top"}
                                anchorEl={projectExistsErrorMessageAnchor}>
                            <Typography color={"error"}>
                                Проект «{name}‎» уже существует
                            </Typography>
                        </Popper>
                    </Box>
                </Box>
            </form>
        </Grid>
    </Grid>
};