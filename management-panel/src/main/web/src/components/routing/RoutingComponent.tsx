import * as React from "react";
import {BrowserRouter, BrowserRouter as Router, Route} from 'react-router-dom';
import {
    AUTHORIZE_PATH,
    AUTHORIZED_STORE,
    BUILD_PATH,
    PROJECT_PATH,
    REGISTER_PATH,
    SLASH,
    TOKEN_COOKIE
} from "../../constants/Constants";
import {ThemeProvider} from "@material-ui/styles";
import {CssBaseline} from "@material-ui/core";
import {DEFAULT_THEME} from "../../theme/Theme";
import {Redirect, Switch} from "react-router";
// @ts-ignore
import Cookies from "js-cookie"
import {ProjectsComponent} from "../project/ProjectsComponent";
import {BuildComponent} from "../build/BuildComponent";
import {AuthorizationComponent} from "../authorization/AuthorizationComponent";
import {RegistrationComponent} from "../registration/RegistrationComponent";
import {authenticate} from "../../api/PlatformApi";
import {useStore} from "react-hookstore";

export const RoutingComponent = () => {
    const [authorized, setAuthorized] = useStore(AUTHORIZED_STORE);
    let token = Cookies.get(TOKEN_COOKIE);
    const routePrivateComponent = (component: any) => {
        if (token && authorized) {
            return component
        }
        if (!token) {
            return <Redirect to={AUTHORIZE_PATH}/>
        }
        authenticate(token, () => setAuthorized(true), () => setAuthorized(false));
    };

    const routePublicComponent = (component: any) => {
        if (token && authorized) {
            return <Redirect to={PROJECT_PATH}/>
        }
        return component
    };

    return <ThemeProvider theme={DEFAULT_THEME}>
        <CssBaseline/>
        <BrowserRouter>
            <Switch>
                <Route exact path={BUILD_PATH}>
                    {routePrivateComponent(<BuildComponent/>)}
                </Route>
                <Route exact path={PROJECT_PATH}>
                    {routePrivateComponent(<ProjectsComponent/>)}
                </Route>
                <Route exact path={AUTHORIZE_PATH}>
                    {routePublicComponent(<AuthorizationComponent/>)}
                </Route>
                <Route exact path={REGISTER_PATH}>
                    {routePublicComponent(<RegistrationComponent/>)}
                </Route>
                <Route path={SLASH}>
                    <Redirect to={PROJECT_PATH}/>
                </Route>
            </Switch>
        </BrowserRouter>
    </ThemeProvider>
};