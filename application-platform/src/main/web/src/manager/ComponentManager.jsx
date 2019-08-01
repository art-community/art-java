import * as React from "react";
import {Route, Router, Switch, withRouter} from "react-router";
import {withCookies} from "react-cookie";
import AxiosConfiguration from "../framework/configuration/AxiosConfiguration";
import {PLATFORM_PATH} from "../constants/Constants";
import MainMenuSidebarComponent from "../sidebar/MainMenuSidebarComponent";
import {
    ADMINISTRATION_ACTION,
    BALANCING_MANAGEMENT_ACTION,
    CLUSTER_MANAGEMENT_ACTION,
    CONFIGURATION_MANAGEMENT_ACTION,
    PROJECT_MANAGEMENT_ACTION,
    USER_CARD_ACTION
} from "../constants/MenuActions";
import {PLATFORM_ROUTER} from "../constants/Routers";
import MainComponent from "../main/MainComponent";

class ComponentManager extends React.Component {
    handleItemClick = (e, {name}) => this.setState({activeItem: name});

    constructor(props) {
        super(props);
        AxiosConfiguration.setupInterceptors(this.props.history, PLATFORM_PATH)
    }

    createMenuComponent = (action) => <MainMenuSidebarComponent history={this.props.history}
                                                                action={action}
                                                                pusher={action.component()}/>;
    render = () =>
        <main>
            <Router history={this.props.history}>
                <Switch>
                    <Route path={PROJECT_MANAGEMENT_ACTION.router}
                           render={() => this.createMenuComponent(PROJECT_MANAGEMENT_ACTION)}/>
                    <Route path={CONFIGURATION_MANAGEMENT_ACTION.router}
                           render={() => this.createMenuComponent(CONFIGURATION_MANAGEMENT_ACTION)}/>
                    <Route path={BALANCING_MANAGEMENT_ACTION.router}
                           render={() => this.createMenuComponent(BALANCING_MANAGEMENT_ACTION)}/>
                    <Route path={CLUSTER_MANAGEMENT_ACTION.router}
                           render={() => this.createMenuComponent(CLUSTER_MANAGEMENT_ACTION)}/>
                    <Route path={ADMINISTRATION_ACTION.router}
                           render={() => this.createMenuComponent(ADMINISTRATION_ACTION)}/>
                    <Route path={USER_CARD_ACTION.router}
                           render={() => this.createMenuComponent(USER_CARD_ACTION)}/>
                    <Route path={PLATFORM_ROUTER} component={MainComponent}/>
                </Switch>
            </Router>
        </main>;
}

export default withRouter(withCookies(ComponentManager))