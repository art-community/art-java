import React from "react";
import {
    Box,
    Chip, createStyles,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Grid, makeStyles, Theme,
    Typography
} from "@material-ui/core";
import {ExpandMore} from "@material-ui/icons";
import {HttpMethodComponent} from "./HttpMethodComponent";
import {HTTP_CHIP_STYLE} from "../constants/Constants";

interface HttpServiceComponentProps {
    information: HttpServiceInformation
}

const useStyles = makeStyles((theme: Theme) => createStyles({
    chip: HTTP_CHIP_STYLE
}));

export const HttpServiceComponent = (props: HttpServiceComponentProps) => {
    const styles = useStyles();
    const httpMethods = () => Array.from(new Map<string, HttpServiceMethodInformation>(Object.entries(props.information
        .methods))
        .values());

    return <ExpansionPanel>
        <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
            <Chip variant={"outlined"} className={styles.chip}
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
