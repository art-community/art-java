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