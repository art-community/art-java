import React from 'react';
import {Card, Grid, Header, Message} from "semantic-ui-react";
import {loadCurrentUser} from "../service/UserService";
import {PRIMARY_COLOR} from "../constants/Constants";

export default class UserCardComponent extends React.Component {
    state = {user: {}};

    componentDidMount = () => loadCurrentUser(user => this.setState({user: user}));

    render = () =>
        <main>
            <Grid columns={2} padded>
                <Grid.Row>
                    <Grid.Column width="2">
                        <Card
                            image={this.state.user.avatarUrl}
                            header={this.state.user.name}
                            meta={
                                <div>
                                    <div>Роль: {this.state.user.role}</div>
                                    <div>Почта: {this.state.user.email}</div>
                                </div>
                            }
                            color={PRIMARY_COLOR}
                        />
                    </Grid.Column>
                    <Grid.Column>
                        <Header as={'h3'}>
                            GitLab URL пользователя
                        </Header>
                        <Message size='small'>
                            <Header as={'h5'}>
                                <a href={this.state.user.gitLabUrl}>{this.state.user.gitLabUrl}</a>
                            </Header>
                        </Message>
                    </Grid.Column>
                </Grid.Row>
            </Grid>
        </main>;
}