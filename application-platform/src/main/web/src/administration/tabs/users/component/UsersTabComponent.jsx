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
import {Button, Item, Segment} from "semantic-ui-react";
import {reactors} from "../reactor/UsersTabComponentReactor";
import {UsersTabComponentContext} from "../context/UsersTabComponentContext";
import {PRIMARY_COLOR} from "../../../../constants/Constants";
import {margin, marginTop} from "../../../../framework/styles/StylesFactory";
import {registry} from "../../../../framework/registry/Registry";

class UsersTabComponent extends React.Component {
    state = {users: []};

    componentContext = registry.usersTab = new UsersTabComponentContext(this);

    componentDidMount = () => reactors.onMount();

    createUserItem = (user, index) =>
        <Item key={`${user?.name}_${index}`}>
            <Item.Image size='tiny' src={user?.avatarUrl}/>
            <Item.Content verticalAlign='middle'>
                <Item.Header as='a'>{user?.name}</Item.Header>
                <Button size={'small'}
                        style={margin(15)}
                        basic
                        icon='delete'
                        color='red'
                        onClick={() => reactors.onDeleteUser(user)}/>
            </Item.Content>
        </Item>;

    render = () =>
        <main>
            <Segment>
                <Item.Group divided>
                    {this.state.users.map(this.createUserItem)}
                </Item.Group>
            </Segment>
            <Button size={'small'}
                    basic
                    icon='plus'
                    color='green'
                    onClick={reactors.onAddUser}/>Добавить пользователя
            <br/>
            <Button style={marginTop(5)}
                    color={PRIMARY_COLOR}
                    onClick={() => reactors.onSave(this.state.users)}>Сохранить</Button>
        </main>
}

export default UsersTabComponent