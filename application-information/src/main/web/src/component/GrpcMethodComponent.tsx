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
import {purple500} from "material-ui/styles/colors";
import {Theme} from "@material-ui/core/styles/createMuiTheme";
import {GRPC_CHIP_STYLE, HTTP_CHIP_STYLE} from "../constants/Constants";
import {GrpcServiceMethodInformation} from "../model/Models";

interface GrpcMethodComponentProps {
    information: GrpcServiceMethodInformation
}

const useStyles = makeStyles((theme: Theme) => createStyles({
    chip: GRPC_CHIP_STYLE
}));

export const GrpcMethodComponent = (props: GrpcMethodComponentProps) => {
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