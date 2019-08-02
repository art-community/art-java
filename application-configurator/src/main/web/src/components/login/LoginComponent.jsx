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

import React from "react";
import {Button, Form, Grid, Header} from "semantic-ui-react";
import {PRIMARY_COLOR, SECONDARY_COLOR} from "../../constants/Constants";

class LoginComponent extends React.Component {
    style = `
  body > div,
  body > div > main,
  body > div > main > div.loginComponent {
    height: 100%;
  }
`;
    render = () =>
        <div className='loginComponent'>
            <style>{this.style}</style>
            <Grid textAlign='center' style={{height: '100%'}} verticalAlign='middle'>
                <Grid.Column style={{maxWidth: 450}}>
                    <Header as='h2' color={SECONDARY_COLOR} textAlign='center'>Войдите в аккаунт конфигуратора</Header>
                    <Form size='large'>
                        <Form.Input
                            fluid
                            icon='user'
                            iconPosition='left'
                            placeholder='Логин'
                            onChange={(event, data) => this.props.onUserChange(data.value)}
                        />
                        <Form.Input
                            fluid
                            icon='lock'
                            iconPosition='left'
                            placeholder='Пароль'
                            type='password'
                            onChange={(event, data) => this.props.onPasswordChange(data.value)}
                        />
                        <Button color={PRIMARY_COLOR} fluid size='large' onClick={() => this.props.onLogin()}>Вход</Button>
                    </Form>
                </Grid.Column>
            </Grid>
        </div>;
}

export default LoginComponent