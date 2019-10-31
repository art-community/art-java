/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from "react";
import {
    Box,
    Chip,
    createStyles,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    Grid,
    makeStyles,
    Theme,
    Typography
} from "@material-ui/core";
import ExpandMore from "@material-ui/icons/ExpandMore";
import {HttpMethodComponent} from "./HttpMethodComponent";
import {HTTP_CHIP_STYLE} from "../constants/Constants";
import {HttpServiceInformation, HttpServiceMethodInformation} from "../model/Models";

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
