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

import * as React from "react";
import {useEffect, useState} from "react";
import {Chip, createStyles, Grid, makeStyles, Theme, Typography} from "@material-ui/core";
import {DEFAULT_THEME} from "../theme/Theme";
import {ThemeProvider} from "@material-ui/styles";
import {HttpServiceComponent} from "./HttpServiceComponent";
import {getInformation} from "../api/InformationApi";
import {CHIP_NOT_OK_STYLE, CHIP_OK_STYLE, MODULE_ID} from "../constants/Constants";
import {GrpServiceComponent} from "./GrpcServiceComponent";
import {RsocketServiceComponent} from "./RsocketServiceComponent";
import {
    FALSE_STATUS,
    GrpcServiceInformation,
    HttpServiceInformation,
    InformationResponse,
    RsocketServiceInformation
} from "../model/Models";

const useStyles = makeStyles((theme: Theme) => createStyles({
    chipOk: CHIP_OK_STYLE,
    chipNotOk: CHIP_NOT_OK_STYLE
}));

export const MainComponent = () => {
    const [information, setInformation] = useState({} as InformationResponse);
    const styles = useStyles();
    useEffect(() => getInformation(information => setInformation(information)), []);
    const httpServices = () => {
        if (information && information.httpInformation && information.httpInformation.services) {
            return Array.from(new Map<string, HttpServiceInformation>(Object.entries(information
                .httpInformation
                .services))
                .values())
        }
        return []
    };
    const grpcServices = () => {
        if (information && information.grpcInformation && information.grpcInformation.services) {
            return Array.from(new Map<string, GrpcServiceInformation>(Object.entries(information
                .grpcInformation
                .services))
                .values())
        }
        return []
    };
    const rsocketServices = () => {
        if (information && information.rsocketInformation && information.rsocketInformation.services) {
            return Array.from(new Map<string, RsocketServiceInformation>(Object.entries(information
                .rsocketInformation
                .services))
                .values())
        }
        return []
    };
    return <ThemeProvider theme={DEFAULT_THEME}>
        <Grid container direction={"column"} spacing={1}>
            <Grid item>
                <Grid alignItems={"center"} container spacing={1}>
                    <Grid item>
                        <Typography variant={"h3"} color={"primary"}>
                            Информация о модуле {MODULE_ID}
                        </Typography>
                    </Grid>
                    {(information.statusResponse || FALSE_STATUS).http
                        ? <Grid item>
                            <Chip variant={"outlined"}
                                  className={styles.chipOk}
                                  label={<Typography>HTTP</Typography>}/>
                        </Grid>
                        : <></>}
                    {(information.statusResponse || FALSE_STATUS).grpc
                        ? <Grid item>
                            <Chip variant={"outlined"}
                                  className={styles.chipOk}
                                  label={<Typography>GRPC</Typography>}/>
                        </Grid>
                        : <></>}
                    {(information.statusResponse || FALSE_STATUS).rsocketTcp
                        ? <Grid item>
                            <Chip variant={"outlined"}
                                  className={styles.chipOk}
                                  label={<Typography>RSocket TCP</Typography>}/>
                        </Grid>
                        : <></>}
                    {(information.statusResponse || FALSE_STATUS).rsocketWebSocket
                        ? <Grid item>
                            <Chip variant={"outlined"}
                                  className={styles.chipOk}
                                  label={<Typography>RSocket Web Socket</Typography>}/>
                        </Grid>
                        : <></>}
                </Grid>
            </Grid>
            <Grid item>
                <Grid container direction={"column"} spacing={1}>
                    {httpServices().map(service =>
                        <Grid key={service.id} item>
                            <HttpServiceComponent information={service}/>
                        </Grid>)}
                    {grpcServices().map(service =>
                        <Grid key={service.id} item>
                            <GrpServiceComponent information={information.grpcInformation}
                                                 serviceInformation={service}/>
                        </Grid>)}
                    {rsocketServices().map(service =>
                        <Grid key={service.id} item>
                            <RsocketServiceComponent information={information.rsocketInformation}
                                                     serviceInformation={service}/>
                        </Grid>)}
                </Grid>
            </Grid>
        </Grid>
    </ThemeProvider>
};