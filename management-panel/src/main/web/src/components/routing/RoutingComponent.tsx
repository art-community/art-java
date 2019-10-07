import * as React from "react";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import {AuthorizationComponent} from "../authorization/AuthorizationComponent";
import {BUILD_PATH, LOGIN_PATH, PROJECT_PATH, REGISTER_PATH, SLASH, USER_STORE} from "../../constants/Constants";
import {ThemeProvider} from "@material-ui/styles";
import {CssBaseline} from "@material-ui/core";
import {DEFAULT_THEME} from "../../theme/Theme";
import {RegistrationComponent} from "../registration/RegistrationComponent";
import {useStore} from "react-hookstore";
import {BuildComponent} from "../build/BuildComponent";
import {ProjectsComponent} from "../project/ProjectsComponent";
// @ts-ignore
import {Redirect, Switch} from "react-router";

export const RoutingComponent = () => {
    const [loggedIn] = useStore(USER_STORE);
    return <ThemeProvider theme={DEFAULT_THEME}>
        <CssBaseline/>
        <Router>
            <Switch>
                <Route exact path={LOGIN_PATH} component={AuthorizationComponent}/>
                <Route exact path={REGISTER_PATH} component={RegistrationComponent}/>
                <Route path={SLASH}>{loggedIn ?
                    <Redirect to={PROJECT_PATH}/> :
                    <Redirect to={LOGIN_PATH}/>}
                </Route>
                <Route exact path={PROJECT_PATH} component={ProjectsComponent}/>
                <Route exact path={BUILD_PATH} component={BuildComponent}/>
            </Switch>
        </Router>
    </ThemeProvider>
};