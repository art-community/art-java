import * as ReactDOM from "react-dom";
import * as React from "react";
import {MAIN_COMPONENT} from "./constants/Constants";
import 'semantic-ui-css/semantic.min.css'
import {Route, Switch} from "react-router";
import {BrowserRouter} from "react-router-dom";
import {STATE_ROUTER} from "./constants/Routers";
import {CookiesProvider} from "react-cookie";
import MainComponent from "./components/main/MainComponent";

const main = () => ReactDOM.render(
    <CookiesProvider>
        <BrowserRouter>
            <Switch>
                <Route path={STATE_ROUTER} component={MainComponent}/>
            </Switch>
        </BrowserRouter>
    </CookiesProvider>, document.getElementById(MAIN_COMPONENT));

if (module.hot) {
    module.hot.dispose(() => main());
}
main();