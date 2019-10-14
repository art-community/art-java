import * as React from "react";
import {
    Box,
    createStyles,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Grid,
    IconButton,
    makeStyles,
    Theme,
    Typography,
    useTheme
} from "@material-ui/core";
import {ArrowBackOutlined, DoneOutlined, ExpandMore, RefreshOutlined} from "@material-ui/icons";

interface ProjectAssemblesComponentProps {
    project: Project,
    onBack: () => void
}

const useStyles = makeStyles((theme: Theme) => createStyles({
        expansionPanel: {
            width: '100%'
        },
        heading: {
            fontSize: theme.typography.pxToRem(15),
            flexBasis: '15%',
            flexShrink: 0,
        },
        iconHeading: {
            marginRight: '100%',
        }
    }),
);


export const ProjectAssemblesComponent = (props: ProjectAssemblesComponentProps) => {
    const theme = useTheme();
    const styles = useStyles();
    return <Grid container direction="column">
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
            <Grid item>
                <div className={styles.expansionPanel}>
                    <ExpansionPanel>
                        <ExpansionPanelSummary expandIcon={<ExpandMore color={"primary"}/>}>
                            <DoneOutlined htmlColor={"green"}/>
                            <Typography className={styles.heading}>Сборка 1</Typography>
                            <Typography className={styles.heading}>Коммит XXX</Typography>
                            <Typography className={styles.heading}>Ветка XXX</Typography>
                            <Typography className={styles.heading}>Тег XXX</Typography>
                            <IconButton className={styles.iconHeading}>
                                <RefreshOutlined color={"secondary"}/>
                            </IconButton>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <Typography>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse malesuada lacus ex,
                                sit amet blandit leo lobortis eget.
                            </Typography>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                    <ExpansionPanel>
                        <ExpansionPanelSummary expandIcon={<ExpandMore color={"primary"}/>}>
                            <DoneOutlined color={"primary"}/>
                            <Typography>
                                Сборка 1
                            </Typography>
                            <IconButton>
                                <RefreshOutlined color={"secondary"}/>
                            </IconButton>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <Typography>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse malesuada lacus ex,
                                sit amet blandit leo lobortis eget.
                            </Typography>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                </div>
            </Grid>
        </Box>
    </Grid>
};