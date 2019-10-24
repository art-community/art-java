import * as React from "react";
import {useEffect, useState} from "react";
import {Divider, Grid, Typography} from "@material-ui/core";
import {DEFAULT_THEME} from "../theme/Theme";
import {ThemeProvider} from "@material-ui/styles";
import {HttpServiceComponent} from "./HttpServiceComponent";
import {getInformation} from "../api/InformationApi";
import {MODULE_ID} from "../constants/Constants";
import {GrpServiceComponent} from "./GrpcServiceComponent";
import {RsocketServiceComponent} from "./RsocketServiceComponent";

export const MainComponent = () => {
    const [information, setInformation] = useState({} as InformationResponse);
    useEffect(() => getInformation(information => setInformation(information)), []);
    console.log(information);
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
                <Typography variant={"h3"} color={"primary"}>
                    Информация о модуле {MODULE_ID}
                </Typography>
            </Grid>
            <Grid item>
                <Grid container direction={"column"} spacing={1}>
                    {httpServices().map(service =>
                        <Grid key={service.id} item>
                            <HttpServiceComponent information={service}/>
                        </Grid>)}
                    {grpcServices().map(service =>
                        <Grid key={service.id} item>
                            <GrpServiceComponent information={information.grpcInformation} serviceInformation={service}/>
                        </Grid>)}
                    {rsocketServices().map(service =>
                        <Grid key={service.id} item>
                            <RsocketServiceComponent information={information.rsocketInformation} serviceInformation={service}/>
                        </Grid>)}
                </Grid>
            </Grid>
        </Grid>
    </ThemeProvider>
};