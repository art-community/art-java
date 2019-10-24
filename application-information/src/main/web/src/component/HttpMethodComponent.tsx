import React, {BaseSyntheticEvent} from "react";
import {
    Box,
    Chip,
    createStyles,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Grid,
    Link,
    makeStyles,
    Typography
} from "@material-ui/core";
import {ExpandMore} from "@material-ui/icons";
import {EntityComponent, EntityType} from "./EntityComponent";
import {Theme} from "@material-ui/core/styles/createMuiTheme";
import {HTTP_CHIP_STYLE} from "../constants/Constants";
import {HttpServiceMethodInformation} from "../model/Models";

interface HttpMethodComponentProps {
    information: HttpServiceMethodInformation
}

const useStyles = makeStyles((theme: Theme) => createStyles({
    chip: HTTP_CHIP_STYLE
}));

export const HttpMethodComponent = (props: HttpMethodComponentProps) => {
    const preventDefault = (event: BaseSyntheticEvent) => event.preventDefault();
    const styles = useStyles();
    return <ExpansionPanel>
        <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
            <Chip variant={"outlined"} className={styles.chip}
                  label={<Typography>Метод</Typography>}/>
            <Box ml={1}>
                <Typography variant={"h6"} color={"secondary"}>
                    {props.information.id}
                </Typography>
            </Box>
        </ExpansionPanelSummary>
        <ExpansionPanelDetails>
            <Grid container direction={"column"} spacing={1}>
                <Grid item container spacing={1}>
                    <Grid item>
                        <Chip color={"primary"}
                              label={<Typography
                                  variant={"subtitle1"}>{props.information.method}</Typography>}/>
                    </Grid>
                    <Grid item>
                        <Link href={props.information.url} onClick={preventDefault}>
                            <Typography variant={"h6"} color={"secondary"}>
                                {props.information.url}
                            </Typography>
                        </Link>
                    </Grid>
                </Grid>
                {props.information.exampleRequest
                    ? <Grid item>
                        <EntityComponent type={EntityType.REQUEST} entity={props.information.exampleRequest}/>
                    </Grid>
                    : <></>}
                {props.information.exampleResponse
                    ? <Grid item>
                        <EntityComponent type={EntityType.RESPONSE} entity={props.information.exampleResponse}/>
                    </Grid>
                    : <></>}
            </Grid>
        </ExpansionPanelDetails>
    </ExpansionPanel>
};