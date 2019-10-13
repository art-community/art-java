import * as React from "react";
import {useState} from "react";
import {Box, Button, TextField, Typography, useTheme} from "@material-ui/core";
import {addProject} from "../../api/PlatformApi";

interface ProjectAddFormProps {
    onProjectAdd: (project: Project) => void
}

export const ProjectAddForm = (props: ProjectAddFormProps) => {
    const [name, setName] = useState("");
    const [url, setUrl] = useState("");
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
                setUrl(event.target.value)
            }}
            fullWidth
        />
        <Box mt={theme.spacing(0.5)}>
            <Button fullWidth
                    variant={'outlined'}
                    color={'primary'}
                    onClick={() => addProject({name: name, url: url}, props.onProjectAdd)}>
                Добавить
            </Button>
        </Box>
    </form>
};