import React from 'react';
import {Form, Header} from "semantic-ui-react";
import {reactors} from "../reactor/ApplicationsTabComponentReactor";
import {ApplicationsTabComponentContext} from "../context/ApplicationsTabComponentContext";
import {registry} from "../../../../framework/registry/Registry";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../../../constants/Constants";
import ProjectConfigurationComponent from "../project/component/ProjectConfigurationComponent";
import {EMPTY_TAG} from "../../../../framework/constants/Constants";
import isEmpty from 'licia/isEmpty'
import {margin} from "../../../../framework/styles/StylesFactory";
import {fromSelectOption, toSelectOption} from "../../../../framework/mapper/SelectOptionMapper";

export default class ApplicationsTabComponent extends React.Component {
    state = {applications: [], projectGroups: [], projects: []};

    context = registry.applicationsTab = new ApplicationsTabComponentContext(this);

    componentWillMount = () => this.context = this.context ? registry.applicationsTab : registry.applicationsTab = new ApplicationsTabComponentContext(this);

    componentDidMount = () => reactors.onMount();

    componentWillUnmount = () => this.context = registry.applicationsTab = null;

    createApplicationSelector = () =>
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите приложение:</Header>
            <Form.Group inline>
                <Form.Select selection
                             options={this.state.applications.map(toSelectOption)}
                             value={this.state.selectedApplication}
                             onChange={(event, option) => reactors.onSelectApplication(fromSelectOption(option))}/>
                <Form.Button basic
                             icon={'delete'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onDeleteApplication}/>
                <Form.Input onChange={(event, input) => reactors.onInputNewApplication(input.value)}/>
                <Form.Button basic
                             icon={'plus'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onAddApplication}/>
            </Form.Group>
        </div>;

    createGroupSelector = () => isEmpty(this.state.selectedApplication) ? EMPTY_TAG :
        <div>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите группу проектов:</Header>
            <Form.Group inline>
                <Form.Select selection
                             value={this.state.selectedProjectGroup}
                             options={this.state.projectGroups.map(toSelectOption)}
                             onChange={(event, option) => reactors.onSelectProjectGroup(fromSelectOption(option))}/>
                <Form.Button basic
                             icon={'delete'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onDeleteProjectGroup}/>
                <Form.Input onChange={(event, input) => reactors.onInputNewProjectGroup(input.value)}/>
                <Form.Button basic
                             icon={'plus'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onAddProjectGroup}/>
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
                <Form.Button basic
                             icon={'delete'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onDeleteProject}/>
                <Form.Input onChange={(event, input) => reactors.onInputNewProject(input.value)}/>
                <Form.Button basic
                             icon={'plus'}
                             color={SECONDARY_COLOR}
                             onClick={reactors.onAddProject}/>
            </Form.Group>
        </div>;

    createProjectConfiguration = () => isEmpty(this.state.selectedProject) ? EMPTY_TAG : <ProjectConfigurationComponent/>;

    render = () =>
        <main>
            <Form size={'large'}>
                {this.createApplicationSelector()}
                {this.createGroupSelector()}
                {this.createProjectSelector()}
                {this.createProjectConfiguration()}
                {this.state.selectedProject ? <Form.Button style={margin(5)}
                                                           color={PRIMARY_COLOR}
                                                           onClick={reactors.onSave}>Сохранить</Form.Button> : EMPTY_TAG}
            </Form>
        </main>
}