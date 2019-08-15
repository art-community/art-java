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
import {Menu, Tab} from "semantic-ui-react";
import UsersTabComponent from "../tabs/users/component/UsersTabComponent";
import ApplicationsTabComponent from "../tabs/applications/component/ApplicationsTabComponent";
import {SECONDARY_COLOR} from "../../constants/Constants";
import {margin} from "../../framework/styles/StylesFactory";
import ProfilesTabComponent from "../tabs/profiles/component/ProfilesTabComponent";
import EnvironmentsTabComponent from "../tabs/environments/component/EnvironmentsTabComponent";
import NotificationsTabComponent from "../tabs/notifications/component/NotificationsTabComponent";
import ToolsComponent from "../tabs/tools/component/ToolsComponent";


class AdministrationComponent extends React.Component {
    createMenuItem = (name, component) => ({
        menuItem: name,
        render: () => component
    });

    createAdministrationPanes = () =>
        [
            this.createMenuItem('Пользователи', <div style={margin(10)}><UsersTabComponent/></div>),
            this.createMenuItem('Приложения и проекты', <div style={margin(10)}><ApplicationsTabComponent/></div>),
            this.createMenuItem('Профили', <div style={margin(10)}><ProfilesTabComponent/></div>),
            this.createMenuItem('Среды и серверы', <div style={margin(10)}><EnvironmentsTabComponent/></div>),
            this.createMenuItem('Нотификации', <div style={margin(10)}><NotificationsTabComponent/></div>),
            this.createMenuItem('Инструменты', <div style={margin(10)}><ToolsComponent/></div>),
        ];

    render = () =>
        <main>
            <Tab
                menu={<Menu pointing secondary color={SECONDARY_COLOR}/>}
                panes={this.createAdministrationPanes()}/>
        </main>

}

export default withCookies(AdministrationComponent)