import {
    Avatar,
    Card,
    CardHeader,
    createStyles,
    IconButton,
    makeStyles,
    Menu,
    MenuItem,
    Theme,
    Typography
} from "@material-ui/core";
import {BuildOutlined, DeleteOutlined, MoreVert} from "@material-ui/icons";
import * as React from "react";
import {useState} from "react";

const useStyles = makeStyles((theme: Theme) => createStyles({
    avatar: {
        backgroundColor: theme.palette.secondary.main,
    },
    chip: {
        margin: theme.spacing(0.5)
    }
}));


interface ProjectCardComponentProps {
    project: Project,
    onAction: (action: ProjectCardMenuAction) => void
}

export enum ProjectCardMenuAction {
    BUILD,
    DELETE
}

export const ProjectCardComponent = (props: ProjectCardComponentProps) => {
    const [menuAnchor, setMenuAnchor] = useState<null | HTMLElement>(null);
    const open = Boolean(menuAnchor);
    const styles = useStyles();

    const handleClick = (event: React.MouseEvent<HTMLElement>) => {
        setMenuAnchor(event.currentTarget);
    };

    return <Card>
        <CardHeader
            avatar={
                <Avatar className={styles.avatar}>
                    {props.project.name[0]}
                </Avatar>
            }
            action={
                <div>
                    <IconButton color={"primary"} onClick={handleClick}>
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
                        } }>
                            <BuildOutlined color={"primary"}/>
                        </MenuItem>
                        <MenuItem onClick={() => {
                            setMenuAnchor(null);
                            props.onAction(ProjectCardMenuAction.DELETE)
                        }}>
                            <DeleteOutlined color={"primary"}/>
                        </MenuItem>
                    </Menu>
                </div>
            }
            title={
                <Typography color={"primary"}>
                    {props.project.name}
                </Typography>
            }
            subheader={
                <Typography color="textSecondary" variant="body2">
                    {props.project.url}
                </Typography>
            }>
        </CardHeader>
    </Card>
};