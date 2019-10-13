import * as React from "react";
import {
    Avatar,
    Box,
    createStyles,
    List,
    ListItem,
    ListItemAvatar,
    ListItemText,
    makeStyles,
    Theme,
    Typography,
    useTheme
} from "@material-ui/core";
import {getProjects} from "../../api/PlatformApi";
import {useEffect, useState} from "react";

const useStyles = makeStyles((theme: Theme) => createStyles({
    avatar: {
        backgroundColor: theme.palette.secondary.main,
    }
}));

interface SelectProjectComponentProps {
    onSelect: (project: Project) => void
}

export const SelectProjectComponent = (props: SelectProjectComponentProps) => {
    const theme = useTheme();
    const styles = useStyles();
    const [projects, setProjects] = useState(new Map<number, Project>());
    useEffect(() => getProjects(setProjects), []);
    return <Box m={theme.spacing(0.5)}>
        <Typography color={"secondary"}>Выберите проект</Typography>
        <List>
            {projects.mapValuesToArray(project =>
                <ListItem
                    key={project.id}
                    button
                    onClick={() => props.onSelect(project)}>
                    <ListItemAvatar>
                        <Avatar className={styles.avatar}>{project.name[0]}
                        </Avatar>
                    </ListItemAvatar>
                    <ListItemText>
                        <Typography color={"primary"}>{project.name}</Typography>
                    </ListItemText>
                </ListItem>)}
        </List>
    </Box>;
};