import React from 'react';
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../../../constants/Constants";
import {Button, Form, Header, Select} from "semantic-ui-react";
import {marginTop} from "../../../../framework/styles/StylesFactory";
import {registry} from "../../../../framework/registry/Registry";
import {ProfilesTabComponentContext} from "../context/ProfilesTabComponentContext";
import {reactors} from "../reactor/ProfilesTabComponentReactor";
import {EMPTY_TAG} from "../../../../framework/constants/Constants";

export default class ProfilesTabComponent extends React.Component {
    state = {applications: [], selectedApplication: null, profiles: [], profileChanges: []};

    context = registry.profilesTab = new ProfilesTabComponentContext(this);

    componentWillMount = () => this.context ? registry.profilesTab : registry.profilesTab = new ProfilesTabComponentContext(this);

    componentDidMount = () => reactors.onMount();

    componentWillUnmount = () => this.context = registry.profilesTab = null;

    creteProfileView = (profile, index) =>
        <Form.Group key={profile + index} inline>
            <Form.Input onChange={(event, data) => reactors.onProfileInput(index, data.value)} defaultValue={profile}/>
            <Form.Button basic
                         icon={'delete'}
                         color={SECONDARY_COLOR}
                         onClick={() => reactors.onDeleteProfile(index)}/>
        </Form.Group>;

    createProfiles = () => !this.state.selectedApplication ? EMPTY_TAG :
        <div>
            {this.state.profiles.map(this.creteProfileView)}
            <Button basic
                    icon={'plus'}
                    color={SECONDARY_COLOR}
                    onClick={reactors.onAddProfile}/>Добавить профиль
            <br/>
            <Button style={marginTop(10)}
                    color={PRIMARY_COLOR}
                    onClick={reactors.onSave}>Сохранить</Button>
        </div>;

    render = () =>
        <main>
            <Header as={'h4'} color={PRIMARY_COLOR}>Выберите приложение:</Header>
            <Form size={'large'}>
                <Form.Field width={2}>
                    <Select selection
                            value={this.state.selectedApplication}
                            options={this.state.applications.map(application => ({
                                value: application,
                                text: application
                            }))}
                            onChange={(event, option) => reactors.onSelectApplication(option.value)}/>
                </Form.Field>
                {this.createProfiles()}
            </Form>
        </main>
}