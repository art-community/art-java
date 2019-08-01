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