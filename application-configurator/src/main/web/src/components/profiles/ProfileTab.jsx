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

import * as React from "react";
import {Header, Message, Segment, Tab} from "semantic-ui-react";
import ConfigurationEditor from "../configuration/ConfigurationEditor";
import {
    applyConfiguration,
    getModules,
    getProfileConfiguration,
    uploadProfileConfiguration
} from "../../services/ConfiguratorService";
import ModuleTab from "../modules/ModuleTab";
import {SECONDARY_COLOR} from "../../constants/Constants";

export default class ProfileTab extends React.Component {
    state = {configuration: {}, modules: [], profile: "", activeTab: 0};

    constructor(props) {
        super(props);
        this.update = this.update.bind(this);
        this.createModulePanes = this.createModulePanes.bind(this);
        this.state = {configuration: {}, modules: [], profile: props.profile};
    }

    componentDidMount() {
        this.update(this.props);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.update(nextProps);
    }

    update = (newProps) => {
        this.setState({profile: newProps.profile});
        getProfileConfiguration(newProps.profile, (configuration) => this.setState({
            configuration: configuration,
            activeTab: 0
        }));
        getModules(newProps.profile, (modules) => this.setState({modules: modules, activeTab: 0}))
    };

    createModulePanes = () => this.state.modules.map(moduleId => ({
        menuItem: moduleId,
        render: () => <ModuleTab moduleId={moduleId} profile={this.state.profile}/>
    }));

    uploadConfiguration = (configuration) => uploadProfileConfiguration(this.state.profile, configuration);

    render = () =>
        <div>
            <Segment>
                <ConfigurationEditor entity={this.state.profile}
                                     configuration={this.state.configuration}
                                     onSave={(configuration) => this.uploadConfiguration(configuration)}
                                     onApply={() => applyConfiguration({profileId: this.state.profile})}
                />
            </Segment>
            <Message compact color={SECONDARY_COLOR} hidden={this.state.modules.length === 0}>
                <Header as={'h4'}>Выбери модуль</Header>
            </Message>
            <Message compact color={SECONDARY_COLOR} hidden={this.state.modules.length !== 0}>
                <Header as={'h4'}>Модули отсутствуют</Header>
            </Message>
            <Segment hidden={this.state.modules.length === 0}>
                <Tab activeIndex={this.state.activeTab}
                     onTabChange={(event, data) => this.setState({activeTab: data.activeIndex})}
                     menu={{fluid: true, vertical: true, tabular: true}}
                     panes={this.createModulePanes()}/>
            </Segment>
        </div>
}