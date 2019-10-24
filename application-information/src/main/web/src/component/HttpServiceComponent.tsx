import React from "react";
import {
    Box,
    Chip,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Grid,
    Typography
} from "@material-ui/core";
import {ExpandMore} from "@material-ui/icons";
import {HttpMethodComponent} from "./HttpMethodComponent";

interface HttpServiceComponentProps {
    information: HttpServiceInformation
}

export const HttpServiceComponent = (props: HttpServiceComponentProps) => {
    const httpMethods = () => Array.from(new Map<string, HttpServiceMethodInformation>(Object.entries(props.information
        .methods))
        .values());

    return <ExpansionPanel>
        <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
            <Chip variant={"outlined"} color={"primary"}
                  label={<Typography>HTTP</Typography>}/>
            <Box ml={1}>
                <Typography variant={"h6"} color={"secondary"}>
                    Сервис {props.information.id}
                </Typography>
            </Box>
        </ExpansionPanelSummary>
        <ExpansionPanelDetails>
            <Grid container direction={"column"} spacing={1}>
                {httpMethods().map(method =>
                    <Grid key={method.id} item>
                        <HttpMethodComponent information={method}/>
                    </Grid>)}
            </Grid>
        </ExpansionPanelDetails>
    </ExpansionPanel>;
};
