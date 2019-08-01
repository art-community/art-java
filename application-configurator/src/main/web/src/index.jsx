import "./components/main/MainComponent.css"
import * as ReactDOM from "react-dom";
import * as React from "react";
import {MAIN_COMPONENT} from "./constants/Constants";
import 'semantic-ui-css/semantic.min.css'
import {Route, Switch, withRouter} from "react-router";
import StartComponent from "./components/start/StartComponent";
import {BrowserRouter} from "react-router-dom";
import {CONFIGURATOR_ROUTER} from "./constants/Routers";
import {CookiesProvider} from "react-cookie";

const main = () => ReactDOM.render(
    <CookiesProvider>
        <BrowserRouter>
            <Switch>
                <Route path={CONFIGURATOR_ROUTER} component={withRouter(StartComponent)}/>
            </Switch>
        </BrowserRouter>
    </CookiesProvider>, document.getElementById(MAIN_COMPONENT));
if (module.hot) {
    module.hot.dispose(() => main());
}
main();