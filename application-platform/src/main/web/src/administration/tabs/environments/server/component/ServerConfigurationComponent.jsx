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
import {ServerConfigurationComponentContext} from "../context/ServerConfigurationComponentContext";
import {reactors} from "../reactor/ServerConfigurationComponentReactor";
import {Form, Header, Segment} from "semantic-ui-react";
import {registry} from "../../../../../framework/registry/Registry";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../../../../constants/Constants";
import {EMPTY_STRING} from "../../../../../framework/constants/Constants";

export default class ServerConfigurationComponent extends React.Component {
    state = {server: {}};

    context = registry.serverConfiguration = new ServerConfigurationComponentContext(this);

    componentWillMount = () => this.context ? registry.serverConfiguration : registry.serverConfiguration = new ServerConfigurationComponentContext(this);

    componentDidMount = () => reactors.onMount();

    componentWillUnmount = () => this.context = registry.serverConfiguration = null;


    render = () =>
        <Segment style={{width: '100em'}} compact>
            <Header color={PRIMARY_COLOR} as='h3'>Конфигурация сервера</Header>

            <Header color={SECONDARY_COLOR} as={'h5'}>ID:</Header>
            <Form.Input defaultValue={this.state.server.id || EMPTY_STRING}
                        onChange={(event, input) => reactors.onServerPropertyInput('id', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>IP адрес:</Header>
            <Form.Input defaultValue={this.state.server.ipAddress || EMPTY_STRING}
                        onChange={(event, input) => reactors.onServerPropertyInput('ipAddress', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Доступные порты:</Header>
            <Form.Input defaultValue={this.state.server.availablePorts || EMPTY_STRING}
                        onChange={(event, input) => reactors.onServerPropertyInput('availablePorts', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Количество ядер:</Header>
            <Form.Input defaultValue={this.state.server.cpuCount || EMPTY_STRING}
                        onChange={(event, input) => reactors.onServerPropertyInput('cpuCount', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Объём ОЗУ:</Header>
            <Form.Input defaultValue={this.state.server.ramSize || EMPTY_STRING}
                        onChange={(event, input) => reactors.onServerPropertyInput('ramSize', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Объём ПЗУ:</Header>
            <Form.Input defaultValue={this.state.server.romSize || EMPTY_STRING}
                        onChange={(event, input) => reactors.onServerPropertyInput('romSize', input.value)}/>
        </Segment>
}