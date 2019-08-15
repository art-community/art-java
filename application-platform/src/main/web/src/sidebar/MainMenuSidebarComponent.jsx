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
import './MainMenuSidebarComponent.css'
import {withRouter} from "react-router";
import {Button, Icon, Menu, Sidebar} from "semantic-ui-react";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../constants/Constants";
import {PLATFORM_ROUTER} from "../constants/Routers";
import {MENU_ACTIONS} from "../constants/MenuActions"

class MainMenuSidebarComponent extends React.Component {
    state = {visible: false};

    hide = () => this.setState({visible: false});

    show = () => this.setState({visible: true});

    createMenuItem = (name, icon, router) =>
        <Menu.Item
            key={name}
            as='a'
            onClick={() => this.props.history.push(router)}>
            <Icon color={SECONDARY_COLOR}
                  name={icon}/>
            <span>{name}</span>
        </Menu.Item>;

    render = () =>
        <div>
            <Button
                className={'sideBarButton'}
                basic
                color={PRIMARY_COLOR}
                icon={'terminal'}
                onMouseEnter={this.show}>
            </Button>
            {`ADK ${this.props.action.name}`}
            <Sidebar
                as={Menu}
                animation='push'
                icon='labeled'
                onHide={this.hide}
                vertical
                visible={this.state.visible}
                width='wide'
                onMouseLeave={this.hide}>
                {this.createMenuItem('На главную', 'desktop', PLATFORM_ROUTER)}
                {MENU_ACTIONS
                    .filter(action => action.name !== this.props.action.name)
                    .map(action => this.createMenuItem(action.name, action.icon, action.router))}
            </Sidebar>
            {this.props.pusher}
        </div>
}

export default withRouter(MainMenuSidebarComponent)