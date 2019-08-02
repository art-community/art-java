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
import {
    getApplicationConfiguration,
    getProfiles,
    uploadApplicationConfiguration
} from "../../services/ConfiguratorService";
import ProfileTab from "../profiles/ProfileTab";
import ConfigurationEditor from "../configuration/ConfigurationEditor";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../constants/Constants";

export default class ApplicationTab extends React.Component {
    state = {profiles: [], configuration: {}};

    constructor(props) {
        super(props);
        this.createProfilePanes = this.createProfilePanes.bind(this);
        this.uploadConfiguration = this.uploadConfiguration.bind(this);
    }

    componentDidMount() {
        this.update();
    }

    update = () => {
        getProfiles((profiles) => this.setState({profiles: profiles}));
        getApplicationConfiguration(configuration => this.setState({configuration: configuration}))
    };

    createProfilePanes = () => this.state.profiles.map(profile => ({
        menuItem: profile,
        render: () => <ProfileTab profile={profile}/>
    }));

    uploadConfiguration = (configuration) => uploadApplicationConfiguration(configuration);

    render = () =>
        <div>
            <Segment>
                <ConfigurationEditor onSave={(configuration) => this.uploadConfiguration(configuration)}
                                     withoutApply={true} entity={"Приложение"}
                                     configuration={this.state.configuration}/>
            </Segment>

            <Message compact color={PRIMARY_COLOR} hidden={this.state.profiles.length === 0}>
                <Header as={'h4'}>Выбери профиль</Header>
            </Message>

            <Message compact color={SECONDARY_COLOR} hidden={this.state.profiles.length !== 0}>
                <Header as={'h4'}>Профили отсутствуют</Header>
            </Message>

            <Tab hidden={this.state.profiles.length === 0}
                 menu={{secondary: true, pointing: true}}
                 panes={this.createProfilePanes()}/>
        </div>
}