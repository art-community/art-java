import * as React from 'react';
import {useState} from 'react';
import {Box, Button, Container, Grid, TextField, Typography,} from '@material-ui/core';
import {useHistory} from "react-router-dom";
import {PROJECT_PATH, REGISTER_PATH, SLASH, TOKEN_COOKIE, USER_STORE} from "../../constants/Constants";
import {useStore} from "react-hookstore";
import {authorize} from "../../api/PlatformApi";
// @ts-ignore
import Cookies from "js-cookie";

export function AuthorizationComponent() {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [user, setUser] = useStore(USER_STORE);
    const history = useHistory();
    return <Container component={'main'} maxWidth={'xs'}>
        <Grid alignItems="center" style={{minHeight: '100vh'}} container>
            <form noValidate>
                <Typography variant="h5" align={"center"} component="h5">
                    Добро пожаловать в платформу развертки приложений
                </Typography>
                <TextField variant={'outlined'}
                           margin={'normal'}
                           required
                           id={'name'}
                           label={'Имя пользователя'}
                           name={'name'}
                           autoComplete={'name'}
                           fullWidth
                           autoFocus
                           value={name}
                           onChange={event => setName(event.target.value)}
                />
                <TextField
                    variant={'outlined'}
                    margin={'normal'}
                    required
                    fullWidth
                    id={'password'}
                    label={'Пароль'}
                    name={'password'}
                    autoComplete={'password'}
                    type={'password'}
                    value={password}
                    onChange={event => setPassword(event.target.value)}
                />
                <Box marginTop={3}>
                    <Grid container spacing={3}>
                        <Grid item xs={6}>
                            <Button
                                fullWidth
                                onClick={() => authorize({name: name, password: password}, response => {
                                    setUser(response.user);
                                    Cookies.set(TOKEN_COOKIE, response.token);
                                    history.push(SLASH);
                                }, () => {
                                    history.push(REGISTER_PATH);
                                })}
                                variant={'contained'}
                                color={'primary'}>
                                Войти
                            </Button>
                        </Grid>
                        <Grid item xs={6}>
                            <Button
                                fullWidth
                                onClick={() => history.push(REGISTER_PATH)}
                                variant={'contained'}
                                color={'secondary'}>
                                Регистрация
                            </Button>
                        </Grid>
                    </Grid>
                </Box>
            </form>
        </Grid>
    </Container>
}