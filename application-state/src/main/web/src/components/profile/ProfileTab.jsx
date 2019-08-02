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
import {SECONDARY_COLOR} from "../../constants/Constants";
import ModuleEndpointTab from "../module/ModuleEdpointTab";
import {getModules} from "../../services/NetworkService";

export default class ProfileTab extends React.Component {
    state = {profile: "", modules: [], activeTab: 0};

    constructor(props) {
        super(props);
        this.update = this.update.bind(this);
        this.createModulePanes = this.createModulePanes.bind(this);
        this.state = {profile: props.profile, modules: []};
    }

    componentDidMount() {
        this.update(this.props);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.update(nextProps);
    }

    update = (newProps) => {
        this.setState({profile: newProps.profile, modules: []});
        getModules(newProps.profile, modules => this.setState({modules: modules}))
    };

    createModulePanes = () => this.state.modules.map(moduleId => ({
        menuItem: moduleId,
        render: () => <ModuleEndpointTab modulePath={moduleId} profile={this.state.profile}/>
    }));

    render = () =>
        <div>
            <Message compact color={SECONDARY_COLOR} hidden={this.state.modules.length === 0}>
                <Header as={'h4'}>Выбери группу модулей</Header>
            </Message>
            <Message compact color={SECONDARY_COLOR} hidden={this.state.modules.length !== 0}>
                <Header as={'h4'}>Модульные группы отсутствуют</Header>
            </Message>
            <Segment hidden={this.state.modules.length === 0}>
                <Tab activeIndex={this.state.activeTab}
                     onTabChange={(event, data) => this.setState({activeTab: data.activeIndex})}
                     menu={{fluid: true, vertical: true, tabular: true}}
                     panes={this.createModulePanes()}/>
            </Segment>
        </div>
}