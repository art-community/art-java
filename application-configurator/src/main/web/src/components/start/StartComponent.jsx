import React from 'react';
import MainComponent from "../main/MainComponent";
import {COOKIE_MAX_AGE, EMPTY_STRING, TOKEN_COOKIE} from "../../constants/Constants";
import {withCookies} from "react-cookie";
import {checkToken, login} from "../../services/UserService";
import LoginComponent from "../login/LoginComponent";
import {Route, Router, Switch} from "react-router";
import AxiosConfiguration from "../../configuration/AxiosConfiguration";
import {CONFIGURATOR_ROUTER} from "../../constants/Routers";

class StartComponent extends React.Component {
    state = {username: '', password: '', success: true};

    constructor(props, context) {
        super(props, context);
        AxiosConfiguration.setupInterceptors(this.props.history)
    }

    componentDidMount = () => checkToken(this.props.cookies.get(TOKEN_COOKIE), (data) => this.onCheckToken(data), () => this.onCheckToken(false));

    processLogin = () => {
        let user = {username: this.state.username, password: this.state.password};
        login(user, data => this.onLogin(data), () => this.setState({success: false}))
    };

    onCheckToken = (data) => this.setState({success: data === EMPTY_STRING ? false : data});

    onLogin = (data) => {
        this.setState({success: data.success});
        this.props.cookies.set(TOKEN_COOKIE, data.token, {maxAge: COOKIE_MAX_AGE});
        this.props.history.push(CONFIGURATOR_ROUTER)
    };

    renderMainComponent = () => <MainComponent/>;

    renderLoginComponent = () =>
        <LoginComponent
            onUserChange={(username) => this.setState({username: username})}
            onPasswordChange={(password) => this.setState({password: password})}
            onLogin={() => this.processLogin()}
        />;

    render = () =>
        <main>
            <Router history={this.props.history}>
                <Switch>
                    <Route path={CONFIGURATOR_ROUTER}
                           render={() => this.state.success ? this.renderMainComponent() : this.renderLoginComponent()}/>
                </Switch>
            </Router>
        </main>
}

export default withCookies(StartComponent)