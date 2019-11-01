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
    Typography
} from "@material-ui/core";
import ExpandMore from "@material-ui/icons/ExpandMore";
import {EntityComponent, EntityType} from "./EntityComponent";
import {Theme} from "@material-ui/core/styles/createMuiTheme";
import {RSOCKET_CHIP_STYLE} from "../constants/Constants";
import {RsocketServiceMethodInformation} from "../model/Models";

interface RsocketMethodComponentProps {
    information: RsocketServiceMethodInformation
}

const useStyles = makeStyles((theme: Theme) => createStyles({
    chip: RSOCKET_CHIP_STYLE
}));

export const RsocketMethodComponent = (props: RsocketMethodComponentProps) => {
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