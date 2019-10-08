import * as React from 'react';
import {useEffect, useState} from 'react';
import {Box, Button, Container, Grid, TextField, Typography,} from '@material-ui/core';
import {useHistory} from "react-router-dom";
import {AUTHORIZED_STORE, PROJECT_PATH, REGISTER_PATH, TOKEN_COOKIE} from "../../constants/Constants";
import {useStore} from "react-hookstore";
import {authorize} from "../../api/PlatformApi";
// @ts-ignore
import Cookies from "js-cookie";

export function AuthorizationComponent() {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [authorized, setAuthorized] = useState(false);
    const [authorizedStore, setAuthorizedStore] = useStore(AUTHORIZED_STORE);
    const history = useHistory();

    useEffect(() => {
        if (authorized) {
            setAuthorizedStore(true);
        }
    });

    const handleAuthorize = (response: UserAuthorizationResponse) => {
        Cookies.set(TOKEN_COOKIE, response.token);
        setAuthorized(true);
        history.push(PROJECT_PATH);
    };

    const handleError = () => {
        history.push(REGISTER_PATH);
    };

    const onRegister = () => history.push(REGISTER_PATH);

    const onAuthorize = () => authorize({name: name, password: password}, handleAuthorize, handleError);

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
                                onClick={onAuthorize}
                                variant={'contained'}
                                color={'primary'}>
                                Войти
                            </Button>
                        </Grid>
                        <Grid item xs={6}>
                            <Button
                                fullWidth
                                onClick={onRegister}
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