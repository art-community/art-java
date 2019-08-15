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
import {registry} from "../../../../framework/registry/Registry";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../../../constants/Constants";
import {Form, Header, Segment} from "semantic-ui-react";
import {EMPTY_STRING, EMPTY_TAG} from "../../../../framework/constants/Constants";
import {reactors} from "../reactor/NotificationsTabComponentReactor";
import {NotificationsTabComponentContext} from "../context/NotificationsTabComponentContext";
import isEmpty from "licia/isEmpty";
import {fromSelectOption, toSelectOption} from "../../../../framework/mapper/SelectOptionMapper";
import {margin} from "../../../../framework/styles/StylesFactory";

export default class NotificationsTabComponent extends React.Component {
    state = {
        applications: [],
        selectedApplication: null,
        selectedApplicationNotifications: [],
        projectGroups: [],
        selectedProjectGroup: null,
        selectedProjectGroupNotifications: [],
        projects: [],
        selectedProject: null,
        selectedProjectNotifications: [],
        events: []
    };

    context = registry.notificationsTab = new NotificationsTabComponentContext(this);

    componentWillMount = () => this.context ? registry.notificationsTab : registry.notificationsTab = new NotificationsTabComponentContext(this);

    componentDidMount = () => reactors.onMount();

    componentWillUnmount = () => this.context = registry.notificationsTab = null;


    createApplicationSelector = () =>
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите приложение:</Header>
            <Form.Group inline>
                <Form.Select selection
                             options={this.state.applications.map(toSelectOption)}
                             value={this.state.selectedApplication}
                             onChange={(event, option) => reactors.onSelectApplication(fromSelectOption(option))}/>
            </Form.Group>
        </div>;

    createProjectsGroupSelector = () => isEmpty(this.state.selectedApplication) ? EMPTY_TAG :
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите группу проектов:</Header>
            <Form.Group inline>
                <Form.Select selection
                             value={this.state.selectedProjectGroup}
                             options={this.state.projectGroups.map(toSelectOption)}
                             onChange={(event, option) => reactors.onSelectProjectGroup(fromSelectOption(option))}/>
            </Form.Group>
        </div>;

    createProjectSelector = () => isEmpty(this.state.selectedProjectGroup) ? EMPTY_TAG :
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите проект:</Header>
            <Form.Group inline>
                <Form.Select selection
                             value={this.state.selectedProject}
                             options={this.state.projects.map(toSelectOption)}
                             onChange={(event, option) => reactors.onSelectProject(fromSelectOption(option))}/>
            </Form.Group>
        </div>;


    createApplicationNotificationsView = () => isEmpty(this.state.selectedApplication) ? EMPTY_TAG :
        <div>
            <Segment style={{width: '100em'}} compact>
                <Header color={PRIMARY_COLOR} as='h3'>
                    Нотификации для приложения {registry.notificationsTab.store.selectedApplication}
                </Header>
                {this.state.selectedApplicationNotifications.map((notification, index) => this.createNotificationView(notification, index, reactors.onApplicationNotificationPropertyInput, () => reactors.onDeleteApplicationNotification(index)))}
            </Segment>
            {this.createAddNotificationButton(reactors.onAddApplicationNotification)}
        </div>;

    createProjectsGroupNotificationsView = () => isEmpty(this.state.selectedProjectGroup) ? EMPTY_TAG :
        <div>
            <Segment style={{width: '100em'}} compact>
                <Header color={PRIMARY_COLOR} as='h3'>
                    Нотификации для группы проектов {registry.notificationsTab.store.selectedProjectGroup}
                </Header>
                {this.state.selectedProjectGroupNotifications.map((notification, index) => this.createNotificationView(notification, index, reactors.onProjectGroupNotificationPropertyInput, () => reactors.onDeleteProjectGroupNotification(index)))}
            </Segment>
            {this.createAddNotificationButton(reactors.onAddProjectGroupNotification)}
        </div>;

    createProjectNotificationsView = () => isEmpty(this.state.selectedProject) ? EMPTY_TAG :
        <div>
            <Segment style={{width: '100em'}} compact>
                <Header color={PRIMARY_COLOR} as='h3'>
                    Нотификации для Проекта {registry.notificationsTab.store.selectedProject}
                </Header>
                {this.state.selectedProjectNotifications.map((notification, index) => this.createNotificationView(notification, index, reactors.onProjectNotificationPropertyInput, () => reactors.onDeleteProjectNotification(index)))}
            </Segment>
            {this.createAddNotificationButton(reactors.onAddProjectNotification)}
        </div>;


    createNotificationView = (notification, index, onInput, onDelete) =>
        <Segment style={{width: '100em'}} compact>
            <Header color={SECONDARY_COLOR} as={'h5'}>ID:</Header>
            <Form.Input defaultValue={notification.id || EMPTY_STRING}
                        onChange={(event, input) => onInput(notification.id, 'id', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Название:</Header>
            <Form.Input defaultValue={notification.name || EMPTY_STRING}
                        onChange={(event, input) => onInput(notification.id, 'name', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Название чата:</Header>
            <Form.Input defaultValue={notification.chatName || EMPTY_STRING}
                        onChange={(event, input) => onInput(notification.id, 'chatName', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>URL чата:</Header>
            <Form.Input defaultValue={notification.chatUrl || EMPTY_STRING}
                        onChange={(event, input) => onInput(notification.id, 'chatUrl', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>URL чат-хука:</Header>
            <Form.Input defaultValue={notification.chatHookUrl || EMPTY_STRING}
                        onChange={(event, input) => onInput(notification.id, 'chatHookUrl', input.value)}/>
            <Header color={SECONDARY_COLOR} as={'h5'}>События:</Header>
            <Form.Dropdown fluid
                           selection
                           multiple
                           options={this.state.events.map(value => ({value: value, text: value}))}
                           defaultValue={notification.events}
                           onChange={(event, input) => onInput(index, 'events', input.value)}/>
            {this.createDeleteNotificationButton(onDelete)}
        </Segment>;

    createAddNotificationButton = (onClick) =>
        <Form.Group inline>
            <Form.Button basic
                         icon={'plus'}
                         color={SECONDARY_COLOR}
                         onClick={onClick}/>Добавить нотификацию
        </Form.Group>;


    createDeleteNotificationButton = (onClick) =>
        <Form.Group inline>
            <Form.Button
                basic
                icon={'delete'}
                color={SECONDARY_COLOR}
                onClick={onClick}/>Удалить нотификацию
        </Form.Group>;


    render = () =>
        <main>
            <Form size={'tiny'}>
                {this.createApplicationSelector()}
                {this.createApplicationNotificationsView()}
                {this.createProjectsGroupSelector()}
                {this.createProjectsGroupNotificationsView()}
                {this.createProjectSelector()}
                {this.createProjectNotificationsView()}
                {this.state.selectedApplication ||
                this.state.selectedProjectGroup ||
                this.state.selectedProject ? <Form.Button style={margin(5)}
                                                          color={PRIMARY_COLOR}
                                                          onClick={reactors.onSave}>Сохранить</Form.Button> : EMPTY_TAG}
            </Form>
        </main>
}