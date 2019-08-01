import * as React from "react";
import {Header, Message, Tab} from "semantic-ui-react";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../constants/Constants";
import ProfileTab from "../profile/ProfileTab";
import {getProfiles} from "../../services/NetworkService";

export default class ClusterTab extends React.Component {
    state = {profiles: []};

    constructor(props) {
        super(props);
        this.createProfilePanes = this.createProfilePanes.bind(this);
        this.state = {profiles: []};
    }

    componentDidMount() {
        getProfiles(profiles => this.setState({profiles: profiles}))
    }

    createProfilePanes = () => this.state.profiles.map(profile => ({
        menuItem: profile,
        render: () => <ProfileTab profile={profile}/>
    }));

    render = () =>
        <div>
            <Message compact color={PRIMARY_COLOR} hidden={this.state.profiles.length === 0}>
                <Header as={'h4'}>Выбери профиль</Header>
            </Message>

            <Message compact color={SECONDARY_COLOR} hidden={this.state.profiles.length !== 0}>
                <Header as={'h4'}>Профили отсутствуют</Header>
            </Message>

            <Tab hidden={this.state.profiles.length === 0}
                 menu={{secondary: true, pointing: true}}
                 panes={this.createProfilePanes}/>
        </div>
}