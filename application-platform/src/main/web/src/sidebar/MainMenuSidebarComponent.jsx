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