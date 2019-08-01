import * as ReactDOM from "react-dom";
import * as React from "react";
import 'semantic-ui-css/semantic.min.css'
import {Route, Switch} from "react-router";
import {BrowserRouter} from "react-router-dom";
import {CookiesProvider} from "react-cookie";
import {MAIN_COMPONENT} from "./framework/constants/Constants";
import {PLATFORM_ROUTER} from "./constants/Routers";
import StartComponent from "./start/StartComponent";

const main = () => ReactDOM.render(
    <CookiesProvider>
        <BrowserRouter>
            <Switch>
                <Route path={PLATFORM_ROUTER} component={StartComponent}/>
            </Switch>
        </BrowserRouter>
    </CookiesProvider>, document.getElementById(MAIN_COMPONENT));

if (module.hot) {
    module.hot.dispose(main);
}
main();