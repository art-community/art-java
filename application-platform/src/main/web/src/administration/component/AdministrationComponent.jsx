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