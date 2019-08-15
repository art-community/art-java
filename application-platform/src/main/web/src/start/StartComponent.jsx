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

import React from 'react';
import {withCookies} from "react-cookie";
import LoginComponent from "../login/LoginComponent";
import {Route, Router, Switch, withRouter} from "react-router";
import AxiosConfiguration from "../framework/configuration/AxiosConfiguration";
import {COOKIE_MAX_AGE, PLATFORM_PATH, TOKEN_COOKIE} from "../constants/Constants";
import {PLATFORM_ROUTER} from "../constants/Routers";
import ComponentManager from "../manager/ComponentManager";
import {EMPTY_STRING} from "../framework/constants/Constants"

class StartComponent extends React.Component {
    constructor(props) {
        super(props);
        AxiosConfiguration.setupInterceptors(this.props.history, PLATFORM_PATH);
        this.state = {username: EMPTY_STRING, password: EMPTY_STRING, success: true};
    }

    componentDidMount = () => {
    };

    processLogin() {
        this.onLogin({success: true, token: 'TOKEN'})
    }

    onCheckToken(data) {
        this.setState({success: data || false});
    }

    onLogin(data) {
        this.setState({success: data.success});
        this.props.cookies.set(TOKEN_COOKIE, data.token, {maxAge: COOKIE_MAX_AGE});
        this.props.history.push(PLATFORM_ROUTER)
    }

    renderComponentManager = () => <ComponentManager/>;

    renderLoginComponent = () =>
        <LoginComponent
            onUserChange={username => this.setState({username: username})}
            onPasswordChange={password => this.setState({password: password})}
            onLogin={() => this.processLogin()}
        />;

    render = () =>
        <main>
            <Router history={this.props.history}>
                <Switch>
                    <Route path={PLATFORM_ROUTER}
                           render={() => this.state.success ? this.renderComponentManager() : this.renderLoginComponent()}/>
                </Switch>
            </Router>
        </main>
}

export default withRouter(withCookies(StartComponent))