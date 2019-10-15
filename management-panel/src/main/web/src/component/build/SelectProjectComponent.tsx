import * as React from "react";
import {useEffect, useState} from "react";
import {
    Avatar,
    Box,
    createStyles, Grid,
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
import {AUTHORIZE_PATH, TOKEN_COOKIE} from "../../constants/Constants";
import {useHistory} from "react-router";
// @ts-ignore
import Cookies from "js-cookie";
import {Project} from "../../model/Models";

const useStyles = makeStyles((theme: Theme) => createStyles({
    projectAvatar: {
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
    const history = useHistory();
    useEffect(() => getProjects(setProjects, () => {
        Cookies.remove(TOKEN_COOKIE);
        history.push(AUTHORIZE_PATH)
    }), []);

    return <Box m={theme.spacing(0.5)}>
        <Typography color={"secondary"} variant="h5" component="h5">
            Выберите проект
        </Typography>
        <List>
            {projects.mapValuesToArray(project =>
                <ListItem
                    key={project.id}
                    button
                    onClick={() => props.onSelect(project)}>
                    <ListItemAvatar>
                        <Avatar className={styles.projectAvatar}>{project.title[0]}
                        </Avatar>
                    </ListItemAvatar>
                    <ListItemText>
                        <Typography color={"primary"}>{project.title}</Typography>
                    </ListItemText>
                </ListItem>)}
        </List>
    </Box>;
};