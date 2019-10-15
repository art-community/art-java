import * as React from "react";
import {useState} from "react";
import {Box, Button, Grid, IconButton, Popper, TextField, Typography, useTheme} from "@material-ui/core";
import {addProject} from "../../api/PlatformApi";
import {ArrowBackOutlined} from "@material-ui/icons";
import {ProjectComponentsProps} from "./props/ProjectComponentsProps";
import {URL_REGEX} from "../../constants/Constants";


export const ProjectAddComponent = (props: ProjectComponentsProps) => {
    const [title, setTitle] = useState("");
    const [gitUrl, setGitUrl] = useState("");
    const [jiraUrl, setJiraUrl] = useState("");
    const [showProjectExistsError, setShowProjectExistsError] = useState(false);
    const [projectExistsErrorMessageAnchor, setProjectExistsErrorMessageAnchor] = useState<null | HTMLElement>(null);
    const theme = useTheme();
    return <Grid>
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
                            setTitle(event.target.value)
                        }}
                        fullWidth
                    />
                    <TextField
                        error={Boolean(gitUrl) && !gitUrl.match(URL_REGEX)}
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
                        error={Boolean(jiraUrl) && !jiraUrl.match(URL_REGEX)}
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
                                disabled={
                                    !title ||
                                    !gitUrl ||
                                    !gitUrl.match(URL_REGEX) ||
                                    (Boolean(jiraUrl) && !jiraUrl.match(URL_REGEX))
                                }
                                ref={ref => setProjectExistsErrorMessageAnchor(ref)}
                                variant={'outlined'}
                                color={'primary'}
                                onClick={() => {
                                    setShowProjectExistsError(false);
                                    addProject({
                                            title: title,
                                            gitUrl: gitUrl,
                                            jiraUrl: jiraUrl
                                        },
                                        props.onProjectUpdate,
                                        () => setShowProjectExistsError(true))
                                }}
                        >
                            Добавить
                        </Button>
                        <Popper open={showProjectExistsError && Boolean(projectExistsErrorMessageAnchor)}
                                placement={"top"}
                                anchorEl={projectExistsErrorMessageAnchor}>
                            <Typography color={"error"}>
                                Проект «{title}‎» уже существует
                            </Typography>
                        </Popper>
                    </Box>
                </Box>
            </form>
        </Grid>
    </Grid>
};