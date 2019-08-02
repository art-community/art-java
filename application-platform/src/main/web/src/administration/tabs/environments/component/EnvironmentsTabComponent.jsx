/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import React from 'react';
import {registry} from "../../../../framework/registry/Registry";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../../../constants/Constants";
import {Form, Header} from "semantic-ui-react";
import {EnvironmentsTabComponentContext} from "../context/EnvironmentsTabComponentContext";
import {reactors} from "../reactor/EnvironmentsTabComponentReactor";
import {margin} from "../../../../framework/styles/StylesFactory";
import {EMPTY_TAG} from "../../../../framework/constants/Constants";
import ServerConfigurationComponent from "../server/component/ServerConfigurationComponent";
import {fromSelectOption, toSelectOption} from "../../../../framework/mapper/SelectOptionMapper";

export default class EnvironmentsTabComponent extends React.Component {
    state = {
        environments: [],
        clusters: [],
        servers: [],
        selectedEnvironment: null,
        selectedCluster: null,
        selectedServer: null
    };

    context = registry.environmentsTab = new EnvironmentsTabComponentContext(this);

    componentWillMount = () => this.context ? registry.environmentsTab : registry.environmentsTab = new EnvironmentsTabComponentContext(this);

    componentDidMount = () => reactors.onMount();

    componentWillUnmount = () => this.context = registry.environmentsTab = null;

    createEnvironmentSelector = () =>
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите среду:</Header>
            <Form.Group inline>
                <Form.Select selection
                             options={this.state.environments.map(toSelectOption)}
                             value={this.state.selectedEnvironment}
                             onChange={(event, option) => reactors.onSelectEnvironment(fromSelectOption(option))}/>
                <Form.Button basic
                             icon={'delete'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onDeleteEnvironment}/>
                <Form.Input onChange={(event, input) => reactors.onInputNewEnvironment(input.value)}/>
                <Form.Button basic
                             icon={'plus'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onAddEnvironment}/>
            </Form.Group>
        </div>;

    createClusterSelector = () => !this.state.selectedEnvironment ? EMPTY_TAG :
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите кластер:</Header>
            <Form.Group inline>
                <Form.Select selection
                             options={this.state.clusters.map(toSelectOption)}
                             value={this.state.selectedCluster}
                             onChange={(event, option) => reactors.onSelectCluster(fromSelectOption(option))}/>
                <Form.Button basic
                             icon={'delete'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onDeleteCluster}/>
                <Form.Input onChange={(event, input) => reactors.onInputNewCluster(input.value)}/>
                <Form.Button basic
                             icon={'plus'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onAddCluster}/>
            </Form.Group>
        </div>;

    createServersSelector = () => !this.state.selectedCluster ? EMPTY_TAG :
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите сервер:</Header>
            <Form.Group inline>
                <Form.Select selection
                             options={this.state.servers.map(toSelectOption)}
                             value={this.state.selectedServer}
                             onChange={(event, option) => reactors.onSelectServer(fromSelectOption(option))}/>
                <Form.Button basic
                             icon={'delete'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onDeleteServer}/>
                <Form.Input onChange={(event, input) => reactors.onInputNewServer(input.value)}/>
                <Form.Button basic
                             icon={'plus'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onAddServer}/>
            </Form.Group>
        </div>;


    render = () =>
        <main>
            <Form size={'large'}>
                {this.createEnvironmentSelector()}
                {this.createClusterSelector()}
                {this.createServersSelector()}
                {this.state.selectedServer ?
                    <div>
                        <ServerConfigurationComponent/>
                        <Form.Button style={margin(5)}
                                     color={PRIMARY_COLOR}
                                     onClick={reactors.onSave}>Сохранить</Form.Button>
                    </div> : EMPTY_TAG}
            </Form>
        </main>
}