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
import {ExpandMore} from "@material-ui/icons";
import {RSOCKET_CHIP_STYLE} from "../constants/Constants";
import {RsocketMethodComponent} from "./RsocketMethodComponent";
import {RsocketInformation, RsocketServiceInformation, RsocketServiceMethodInformation} from "../model/Models";

interface RsocketServiceComponentProps {
    information: RsocketInformation
    serviceInformation: RsocketServiceInformation
}

const useStyles = makeStyles((theme: Theme) => createStyles({
    chip: RSOCKET_CHIP_STYLE
}));

export const RsocketServiceComponent = (props: RsocketServiceComponentProps) => {
    const styles = useStyles();
    const rsocketMethods = () => Array.from(new Map<string, RsocketServiceMethodInformation>(Object.entries(props.serviceInformation.methods)).values());

    return <ExpansionPanel>
        <ExpansionPanelSummary expandIcon={<ExpandMore/>}>
            <Chip variant={"outlined"} className={styles.chip}
                  label={<Typography>RSocket</Typography>}/>
            <Box ml={1}>
                <Typography variant={"h6"} color={"secondary"}>
                    Сервис {props.serviceInformation.id}
                </Typography>
            </Box>
        </ExpansionPanelSummary>
        <ExpansionPanelDetails>
            <Grid container direction={"column"} spacing={1}>
                {props.information.tcpUrl
                    ? <Grid container item>
                        <Chip variant={"outlined"} className={styles.chip}
                              label={<Typography>TCP URL</Typography>}/>
                        <Box ml={1}>
                            <Typography variant={"subtitle1"} color={"secondary"}>
                                {props.information.tcpUrl}
                            </Typography>
                        </Box>
                    </Grid>
                    : <></>
                }
                {props.information.webSocketUrl
                    ? <Grid container item>
                        <Chip variant={"outlined"} className={styles.chip}
                              label={<Typography>Web Socket URL</Typography>}/>
                        <Box ml={1}>
                            <Typography variant={"subtitle1"} color={"secondary"}>
                                {props.information.webSocketUrl}
                            </Typography>
                        </Box>
                    </Grid>
                    : <></>
                }
                {rsocketMethods().map(method =>
                    <Grid key={method.id} item>
                        <RsocketMethodComponent information={method}/>
                    </Grid>)}
            </Grid>
        </ExpansionPanelDetails>
    </ExpansionPanel>;
};
