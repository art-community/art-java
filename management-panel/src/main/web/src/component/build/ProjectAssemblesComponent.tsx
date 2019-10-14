import * as React from "react";
import {
    Avatar,
    Box,
    Card, CardContent,
    CardHeader, Chip,
    createStyles,
    Grid,
    IconButton, List, ListItem,
    makeStyles, Menu, MenuItem,
    Theme,
    Typography,
    useTheme
} from "@material-ui/core";
import {
    ArrowBackOutlined,
    BuildOutlined, CloseOutlined,
    DeleteOutlined, DoneOutline, DoneOutlined,
    MoreVert,
    RefreshOutlined,
    SubjectOutlined
} from "@material-ui/icons";
import {ProjectCardMenuAction} from "../project/props/ProjectComponentsProps";
import {useState} from "react";

interface ProjectAssemblesComponentProps {
    project: Project,
    onBack: () => void
}

const useStyles = makeStyles((theme: Theme) => createStyles({
        avatar: {
            backgroundColor: theme.palette.secondary.main,
        },
        card: {
            width: '100%'
        }
    }),
);


export const ProjectAssemblesComponent = (props: ProjectAssemblesComponentProps) => {
    const theme = useTheme();
    const [menuAnchor, setMenuAnchor] = useState<null | HTMLElement>(null);
    const open = Boolean(menuAnchor);
    const styles = useStyles();
    return <Grid>
        <Grid item>
            <IconButton onClick={() => props.onBack()}>
                <ArrowBackOutlined color={"secondary"}/>
            </IconButton>
        </Grid>
        <Box m={theme.spacing(0.3)}>
            <Box mb={theme.spacing(0.5)}>
                <Grid item>
                    <Typography color={"primary"}>
                        Сборки проекта «{props.project.name}‎»
                    </Typography>
                </Grid>
            </Box>
            <Grid container direction={"column"} spacing={2}>
                <Grid  item>
                    <Card>
                        <CardHeader
                            avatar={
                                <DoneOutlined htmlColor={"green"}/>
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
                                        }}>
                                            <RefreshOutlined color={"primary"}/>
                                        </MenuItem>
                                        <MenuItem onClick={() => {
                                            setMenuAnchor(null);
                                        }}>
                                            <SubjectOutlined color={"primary"}/>
                                        </MenuItem>
                                    </Menu>
                                </div>
                            }
                            title={
                                <Typography color={"primary"}>
                                    Сборка 1
                                </Typography>
                            }
                        />
                        <CardContent>
                            <Grid container direction={"column"} spacing={2}>
                                <Grid container item>
                                    <Grid justify={"flex-start"} item xs>
                                        <Typography color={"primary"}>
                                            Время:
                                        </Typography>
                                    </Grid>
                                    <Grid justify={"flex-end"} item xs>
                                        <Typography color={"secondary"}>
                                            Вчера в 12:00
                                        </Typography>
                                    </Grid>
                                </Grid>
                                <Grid container item>
                                    <Grid justify={"flex-start"} item xs>
                                        <Typography color={"primary"}>
                                            Коммит:
                                        </Typography>
                                    </Grid>
                                    <Grid justify={"flex-end"} item xs>
                                        <Typography color={"secondary"}>
                                            awdawd@awdaw
                                        </Typography>
                                    </Grid>
                                </Grid>
                                <Grid container item>
                                    <Grid justify={"flex-start"} item xs>
                                        <Typography color={"primary"}>
                                            Тег
                                        </Typography>
                                    </Grid>
                                    <Grid justify={"flex-end"} item xs>
                                        <Typography color={"secondary"}>
                                            1.0.0
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item>
                    <Card>
                        <CardHeader
                            avatar={
                                <CloseOutlined htmlColor={"red"}/>
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
                                        }}>
                                            <RefreshOutlined color={"primary"}/>
                                        </MenuItem>
                                        <MenuItem onClick={() => {
                                            setMenuAnchor(null);
                                        }}>
                                            <SubjectOutlined color={"primary"}/>
                                        </MenuItem>
                                    </Menu>
                                </div>
                            }
                            title={
                                <Typography color={"primary"}>
                                    Сборка 1
                                </Typography>
                            }
                        />
                        <CardContent className={styles.cardContent}>
                            <Grid container direction={"column"} spacing={2}>
                                <Grid container item>
                                    <Grid justify={"flex-start"} item xs>
                                        <Typography color={"primary"}>
                                            Время:
                                        </Typography>
                                    </Grid>
                                    <Grid justify={"flex-end"} item xs>
                                        <Typography color={"secondary"}>
                                            Вчера в 12:00
                                        </Typography>
                                    </Grid>
                                </Grid>
                                <Grid container item>
                                    <Grid justify={"flex-start"} item xs>
                                        <Typography color={"primary"}>
                                            Коммит:
                                        </Typography>
                                    </Grid>
                                    <Grid justify={"flex-end"} item xs>
                                        <Typography color={"secondary"}>
                                            awdawd@awdaw
                                        </Typography>
                                    </Grid>
                                </Grid>
                                <Grid container item>
                                    <Grid justify={"flex-start"} item xs>
                                        <Typography color={"primary"}>
                                            Тег
                                        </Typography>
                                    </Grid>
                                    <Grid justify={"flex-end"} item xs>
                                        <Typography color={"secondary"}>
                                            1.0.0
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </Box>
    </Grid>
};