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