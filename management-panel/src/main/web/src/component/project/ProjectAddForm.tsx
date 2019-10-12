import * as React from "react";
import {useState} from "react";
import {Box, Button, TextField, Typography} from "@material-ui/core";

interface ProjectAddFormProps {
    onProjectAdd: (project: Project) => void
}

export const ProjectAddForm = (props: ProjectAddFormProps) => {
    const [name, setName] = useState("");
    const [url, setUrl] = useState("");
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
        <Box mt={'normal'}>
            <Button fullWidth
                    variant={'outlined'}
                    color={'primary'}
                    onClick={() => props.onProjectAdd({name: name, url: url})}>
                Добавить
            </Button>
        </Box>
    </form>
};