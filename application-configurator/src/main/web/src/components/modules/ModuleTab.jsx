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
import ConfigurationEditor from "../configuration/ConfigurationEditor";
import {
    applyConfiguration,
    getModuleConfiguration,
    uploadModuleConfiguration
} from "../../services/ConfiguratorService";

export default class ModuleTab extends React.Component {
    state = {configuration: {}, profile: "", moduleId: ""};

    constructor(props) {
        super(props);
        this.update = this.update.bind(this);
        this.uploadConfiguration = this.uploadConfiguration.bind(this);
        this.state = {configuration: {}, profile: props.profile, moduleId: props.moduleId};
    }

    componentDidMount() {
        this.update(this.props);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.update(nextProps);
    }

    uploadConfiguration = (configuration) => {
        uploadModuleConfiguration({profileId: this.state.profile, moduleId: this.state.moduleId}, configuration)
    };

    update = (newProps) => {
        this.setState({profile: newProps.profile, moduleId: newProps.moduleId});
        getModuleConfiguration(newProps.profile, newProps.moduleId, (configuration) => this.setState({configuration: configuration}));
    };

    render = () => <ConfigurationEditor onSave={(configuration) => this.uploadConfiguration(configuration)}
                                        onApply={() => applyConfiguration({
                                            profileId: this.state.profile,
                                            moduleId: this.state.moduleId
                                        })}
                                        entity={this.state.moduleId}
                                        configuration={this.state.configuration}/>
}