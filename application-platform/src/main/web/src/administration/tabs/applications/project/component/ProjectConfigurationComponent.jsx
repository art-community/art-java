import React from 'react';
import {Form, Header, Segment} from "semantic-ui-react";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../../../../constants/Constants";
import {ProjectConfigurationComponentContext} from "../context/ProjectConfigurationComponentContext";
import {EMPTY_STRING, EMPTY_TAG} from "../../../../../framework/constants/Constants";
import {registry} from "../../../../../framework/registry/Registry";
import {reactors} from "../reactor/ProjectConfigurationComponentReactor";
import {
    MODULE_ROLE_OPTIONS,
    PROJECT_TYPE_OPTIONS,
    PROJECT_TYPES
} from "../constants/ProjectConfigurationComponentConstants";

export default class ProjectConfigurationComponent extends React.Component {
    state = {project: {}, environments: [], servers: []};

    context = registry.projectConfiguration = new ProjectConfigurationComponentContext(this);

    componentWillMount = () => this.context ? registry.projectConfiguration : registry.projectConfiguration = new ProjectConfigurationComponentContext(this);

    componentDidMount = () => reactors.onMount();

    componentWillUnmount = () => this.context = registry.projectConfiguration = null;

    createModuleParameters = () =>
        <Segment style={{width: '100em'}} compact>
            <Header color={PRIMARY_COLOR} as='h3'>Параметры модуля</Header>

            <Header color={SECONDARY_COLOR} as={'h5'}>ID:</Header>
            <Form.Input defaultValue={this.state.project.module.id || EMPTY_STRING}
                        onChange={(event, input) => reactors.onProjectPropertyInput('module.id', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Роль:</Header>
            <Form.Dropdown fluid
                           selection
                           options={MODULE_ROLE_OPTIONS}
                           value={this.state.project.module.role}
                           onChange={(event, input) => reactors.onProjectPropertyInput('module.role', input.value, true)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Допустимые среды:</Header>
            <Form.Dropdown fluid
                           selection
                           multiple
                           options={this.state.environments.map(value => ({value: value, text: value}))}
                           defaultValue={this.state.project.module.availableEnvironments}
                           onChange={(event, input) => reactors.onSelectEnvironments(input.value)}/>


            <Header color={SECONDARY_COLOR} as={'h5'}>Допустимые серверы:</Header>
            <Form.Dropdown fluid
                           selection
                           multiple
                           options={this.state.servers.map(value => ({value: value, text: value}))}
                           defaultValue={this.state.project.module.availableServers}
                           onChange={(event, input) => reactors.onProjectPropertyInput('module.availableServers', input.value)}/>

        </Segment>;

    render = () =>
        <Segment style={{width: '100em'}} compact>
            <Header color={PRIMARY_COLOR} as='h3'>Конфигурация проекта</Header>

            <Header color={SECONDARY_COLOR} as={'h5'}>ID:</Header>
            <Form.Input defaultValue={this.state.project.id || EMPTY_STRING}
                        onChange={(event, input) => reactors.onProjectPropertyInput('id', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>GitLab URL:</Header>
            <Form.Input defaultValue={this.state.project.gitLabUrl || EMPTY_STRING}
                        onChange={(event, input) => reactors.onProjectPropertyInput('gitLabUrl', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>GIT URL:</Header>
            <Form.Input defaultValue={this.state.project.gitUrl || EMPTY_STRING}
                        onChange={(event, input) => reactors.onProjectPropertyInput('gitUrl', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Artifactory URL:</Header>
            <Form.Input defaultValue={this.state.project.artifactoryUrl || EMPTY_STRING}
                        onChange={(event, input) => reactors.onProjectPropertyInput('artifactoryUrl', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Группа:</Header>
            <Form.Input defaultValue={this.state.project.group || EMPTY_STRING}
                        onChange={(event, input) => reactors.onProjectPropertyInput('group', input.value)}/>

            <Header color={SECONDARY_COLOR} as={'h5'}>Тип:</Header>
            <Form.Dropdown fluid
                           selection
                           options={PROJECT_TYPE_OPTIONS}
                           value={this.state.project.type}
                           onChange={(event, input) => reactors.onProjectPropertyInput('type', input.value, true)}/>

            {this.state.project.type === PROJECT_TYPES.MODULE.type ? this.createModuleParameters() : EMPTY_TAG}
        </Segment>
}

