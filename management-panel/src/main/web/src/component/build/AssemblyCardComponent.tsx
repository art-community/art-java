import * as React from "react";
import {useState} from "react";
import {
    Card,
    CardContent,
    CardHeader,
    CircularProgress,
    createStyles,
    Grid,
    IconButton,
    makeStyles,
    Menu,
    MenuItem,
    Theme,
    Typography
} from "@material-ui/core";
import {CloseOutlined, DoneOutlined, MoreVert, RefreshOutlined, SubjectOutlined} from "@material-ui/icons";
import {AssemblyCardComponentProps} from "./props/AssemblyComponentsProps";
import {AssemblyState} from "../../model/Models";
import {DATE_TIME_FORMAT} from "../../constants/Constants";
import {Moment} from "moment";


const useStyles = makeStyles((theme: Theme) => createStyles({
        projectAvatar: {
            backgroundColor: theme.palette.secondary.main,
        },
        card: {
            width: '100%'
        },
        gridCardContent: {},
        icon: {
            width: 35,
            height: 35
        },
        progress: {
            color: theme.palette.primary.main,
            animationDuration: '550ms'
        }
    }),
);

export const AssemblyCardComponent = (props: AssemblyCardComponentProps) => {
    const styles = useStyles();
    const [menuAnchor, setMenuAnchor] = useState<null | HTMLElement>(null);
    const open = Boolean(menuAnchor);

    const createAssembleStringAttribute = (name: string, value?: any) =>
        value ?
            <Grid key={name} container item spacing={1}>
                <Grid item xs>
                    <Typography color={"primary"}>
                        {name}:
                    </Typography>
                </Grid>
                <Grid item xs>
                    <Typography noWrap align={"right"} color={"secondary"}>
                        {value}
                    </Typography>
                </Grid>
            </Grid>
            : <></>;

    const createAssembleDateAttribute = (name: string, value?: Moment) =>
        value ?
            <Grid key={name} container item spacing={1}>
                <Grid item xs>
                    <Typography color={"primary"}>
                        {name}:
                    </Typography>
                </Grid>
                <Grid item xs>
                    <Typography noWrap align={"right"} color={"secondary"}>
                        {value.format(DATE_TIME_FORMAT)}
                    </Typography>
                </Grid>
            </Grid>
            : <></>;

    const createAvatar = () => {
        switch (+props.assembly.state) {
            case AssemblyState.BUILDING:
                return <CircularProgress variant="indeterminate"
                                         disableShrink
                                         size={35}
                                         thickness={4}
                                         className={styles.progress}/>;
            case AssemblyState.DONE:
                return <DoneOutlined className={styles.icon} htmlColor={"green"}/>;

            default:
                return <CloseOutlined className={styles.icon} htmlColor={"red"}/>

        }
    };
    return <Card>
        <CardHeader
            avatar={createAvatar()}
            action={<div>
                <IconButton color={"primary"}
                            onClick={(event: React.MouseEvent<HTMLElement>) => setMenuAnchor(event.currentTarget)}>
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
                    <MenuItem onClick={() => setMenuAnchor(null)}>
                        <RefreshOutlined color={"primary"}/>
                    </MenuItem>
                    <MenuItem onClick={() => setMenuAnchor(null)}>
                        <SubjectOutlined color={"primary"}/>
                    </MenuItem>
                </Menu>
            </div>
            }
            title={<Typography color={"primary"}>{props.assembly.title}</Typography>}
        />
        <CardContent>
            <Grid className={styles.gridCardContent} container direction={"column"} spacing={1}>
                {createAssembleStringAttribute("Ветка", props.assembly.branch)}
                {createAssembleStringAttribute("Коммит", props.assembly.commit)}
                {createAssembleStringAttribute("Тег", props.assembly.tag)}
                {createAssembleDateAttribute("Началась", props.assembly.startDateTime)}
                {createAssembleDateAttribute("Закончилась", props.assembly.endDateTime)}
            </Grid>
        </CardContent>
    </Card>
};