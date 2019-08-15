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