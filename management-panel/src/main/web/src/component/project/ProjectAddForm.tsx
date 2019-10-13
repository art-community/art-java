import * as React from "react";
import {useState} from "react";
import {Box, Button, Grid, Popper, TextField, Typography, useTheme} from "@material-ui/core";
import {addProject} from "../../api/PlatformApi";
import {useStore} from "react-hookstore";
import {AUTHORIZED_STORE} from "../../constants/Constants";

interface ProjectAddFormProps {
    onProjectAdd: (project: Project) => void
}

export const ProjectAddForm = (props: ProjectAddFormProps) => {
    const [name, setName] = useState("");
    const [url, setUrl] = useState("");
    const [showProjectExistsError, setShowProjectExistsError] = useState(false);
    const [projectExistsErrorMessageAnchor, setProjectExistsErrorMessageAnchor] = useState<null | HTMLElement>(null);
    const theme = useTheme();
    return <form noValidate>
        <Typography color={"secondary"} variant="h5" component="h5">
            Новый проект
        </Typography>
        <TextField
            variant={'outlined'}
            margin={'normal'}
            required
            label={'Имя проекта'}
            onChange={event => {
                setShowProjectExistsError(false)
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
                setShowProjectExistsError(false)
                setUrl(event.target.value)
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
                        addProject({name: name, url: url}, props.onProjectAdd, () => setShowProjectExistsError(true))
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
    </form>
};