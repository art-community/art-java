import * as React from "react";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import {LoginComponent} from "../login/LoginComponent";
import {
    BUILD_PATH,
    LOGIN_PATH,
    PLATFORM_PATH, PROJECT_PATH,
    REGISTER_PATH, SLASH,
    TOKEN_COOKIE,
    USER_STORE
} from "../../constants/Constants";
import {ThemeProvider} from "@material-ui/styles";
import {CssBaseline} from "@material-ui/core";
import {DEFAULT_THEME} from "../../theme/Theme";
import {RegistrationComponent} from "../registration/RegistrationComponent";
import {useStore} from "react-hookstore";
import {normalizeRoutingPath} from "../../normalization/RoutingPathNormalization";
import {BuildComponent} from "../build/BuildComponent";
import {ProjectsComponent} from "../project/ProjectsComponent";
// @ts-ignore
import {useCookie} from "@use-hook/use-cookie";
import {Redirect, Switch} from "react-router";

export const RoutingComponent = () => {
    const [user] = useStore(USER_STORE);
    const [token] = useCookie(TOKEN_COOKIE);
    let loggedIn = token || user;
    return <ThemeProvider theme={DEFAULT_THEME}>
        <CssBaseline/>
        <Router>
            <Switch>
                <Route exact path={normalizeRoutingPath(LOGIN_PATH)} component={LoginComponent}/>
                <Route exact path={normalizeRoutingPath(REGISTER_PATH)} component={RegistrationComponent}/>
                <Route path={SLASH}>{loggedIn ?
                    <Redirect to={normalizeRoutingPath(PROJECT_PATH)}/> :
                    <Redirect to={normalizeRoutingPath(LOGIN_PATH)}/>}
                </Route>
                <Route exact path={normalizeRoutingPath(PROJECT_PATH)} component={ProjectsComponent}/>
                <Route exact path={normalizeRoutingPath(BUILD_PATH)} component={BuildComponent}/>
            </Switch>
        </Router>
    </ThemeProvider>
};