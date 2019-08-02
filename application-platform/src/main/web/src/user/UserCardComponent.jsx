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