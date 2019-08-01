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