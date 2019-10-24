import React, {BaseSyntheticEvent} from "react";
import {
    Box,
    Chip,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Grid,
    Link,
    Typography
} from "@material-ui/core";
import {ExpandMore} from "@material-ui/icons";
import {EntityComponent, EntityType} from "./EntityComponent";

interface HttpMethodComponentProps {
    information: HttpServiceMethodInformation
}

export const HttpMethodComponent = (props: HttpMethodComponentProps) => {
    const preventDefault = (event: BaseSyntheticEvent) => event.preventDefault();

    return <ExpansionPanel>
        <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
            <Chip variant={"outlined"} color={"primary"}
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