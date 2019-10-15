import {
    Avatar,
    Card,
    CardContent,
    CardHeader,
    createStyles,
    Grid,
    IconButton,
    Link,
    makeStyles,
    Menu,
    MenuItem,
    Theme,
    Typography
} from "@material-ui/core";
import {BuildOutlined, MoreVert} from "@material-ui/icons";
import * as React from "react";
import {useState} from "react";
import {ProjectCardComponentProps, ProjectCardMenuAction} from "./props/ProjectComponentsProps";
import {TECHNOLOGY_IMAGES} from "../../constants/Constants";

const useStyles = makeStyles((theme: Theme) => createStyles({
    projectAvatar: {
        backgroundColor: theme.palette.secondary.main,
    },
    technologyAvatar: {
        width: 40,
        height: 40,
        "box-shadow": `0 0 7px ${theme.palette.secondary.main}`,
        padding: 7,
        margin: 7,
        border: `1px solid ${theme.palette.secondary.main}`
    },
    chip: {
        margin: theme.spacing(0.5)
    },
    link: {
        margin: theme.spacing(1),
    }
}));


export const ProjectCardComponent = (props: ProjectCardComponentProps) => {
    const [menuAnchor, setMenuAnchor] = useState<null | HTMLElement>(null);
    const open = Boolean(menuAnchor);
    const styles = useStyles();

    const createProjectCards = () => {
        if (!props.project.technologies || props.project.technologies.length == 0) {
            return <></>
        }
        return new Set<string>(props.project.technologies)
            .mapToArray(technology => TECHNOLOGY_IMAGES.get(technology))
            .map(image =>
                <Grid key={image} item>
                    <Avatar className={styles.technologyAvatar} src={image}/>
                </Grid>);
    };

    return <Card>
        <CardHeader
            avatar={
                <Avatar className={styles.projectAvatar}>
                    {props.project.title[0]}
                </Avatar>
            }
            action={
                <div>
                    <IconButton color={"primary"} onClick={(event: React.MouseEvent<HTMLElement>) => {
                        setMenuAnchor(event.currentTarget);
                    }}>
                        <MoreVert/>
                    </IconButton>
                    <Menu anchorEl={menuAnchor}
                          getContentAnchorEl={null}
                          anchorOrigin={{
                              horizontal: 'right',
                              vertical: 'bottom',
                          }}
                          transformOrigin={{
                              horizontal: 'left',
                              vertical: 'top',
                          }}
                          onClose={() => setMenuAnchor(null)}
                          open={open}
                          keepMounted>
                        <MenuItem onClick={() => {
                            setMenuAnchor(null);
                            props.onAction(ProjectCardMenuAction.BUILD);
                        }}>
                            <BuildOutlined color={"primary"}/>
                        </MenuItem>
                    </Menu>
                </div>
            }
            title={
                <Typography color={"primary"}>
                    {props.project.title}
                </Typography>
            }
            subheader={
                <Typography color="textSecondary" variant="body2">
                    <Link href={props.project.gitUrl}>
                        Посмотреть в git репо
                    </Link>
                </Typography>
            }>
        </CardHeader>
        <CardContent>
            <Typography color="secondary" variant="body2">
                <Link href={props.project.jiraUrl} className={styles.link}>
                    Посмотреть в JIRA
                </Link>
            </Typography>
            <Grid container spacing={1}>
                {createProjectCards()}
            </Grid>
        </CardContent>
    </Card>
};