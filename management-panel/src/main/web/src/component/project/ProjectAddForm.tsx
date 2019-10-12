import * as React from "react";
import {useEffect, useState} from "react";
import {
    Box,
    Button,
    Checkbox, Chip,
    Divider,
    FormControl,
    FormControlLabel,
    FormLabel,
    TextField,
    Typography
} from "@material-ui/core";
import {getTechnologies} from "../../api/PlatformApi";
import {TECHNOLOGIES} from "../../constants/Constants";

interface ProjectAddFormProps {
    onProjectAdd: (project: Project) => void
}

export const ProjectAddForm = (props: ProjectAddFormProps) => {
    const [name, setName] = useState("");
    const [url, setUrl] = useState("");
    const [technologies, setTechnologies] = useState<Set<string>>(new Set());
    const [selectedTechnologies, setSelectedTechnologies] = useState<Set<string>>(new Set());
    useEffect(() => getTechnologies(setTechnologies), []);
    const createTechnologyControl = (id: string) =>
        <FormControlLabel
            key={id}
            control={
                <Checkbox
                    key={id}
                    value={id}
                    onChange={event => {
                        event.target.checked
                            ? selectedTechnologies.add(event.target.value)
                            : selectedTechnologies.delete(event.target.value)
                    }}/>}
            label={TECHNOLOGIES[id]}>
        </FormControlLabel>;

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
        <Divider/>
        <FormControl
            margin={'normal'}
            fullWidth
            variant="outlined">
            <FormLabel><Typography color={"secondary"}>Технологии</Typography></FormLabel>
            {Array.from(technologies).map(createTechnologyControl)}
        </FormControl>
        <Box mt={'normal'}>
            <Button fullWidth
                    variant={'outlined'}
                    color={'primary'}
                    onClick={() => props.onProjectAdd({name: name, url: url, technologies: selectedTechnologies})}>
                Добавить
            </Button>
        </Box>
    </form>
};