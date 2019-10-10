import * as React from 'react';
import {useEffect, useState} from 'react';
import {Box, Button, Container, Grid, TextField, Typography,} from '@material-ui/core';
import {useHistory} from "react-router";
import {AUTHORIZED_STORE, PROJECT_PATH, TOKEN_COOKIE} from "../../constants/Constants";
import {registerUser} from "../../api/PlatformApi";
import {useStore} from "react-hookstore";
// @ts-ignore
import Cookies from "js-cookie";

export const RegistrationComponent = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [authorized, setAuthorized] = useState(false);
    const [authorizedStore, setAuthorizedStore] = useStore(AUTHORIZED_STORE);
    const history = useHistory();

    useEffect(() => {
        if (authorized) {
            setAuthorizedStore(true);
        }
    });

    const onRegister = () => registerUser({name: name, email: email, password: password}, response => {
        Cookies.set(TOKEN_COOKIE, response.token);
        setAuthorized(true);
        history.push(PROJECT_PATH);
    });

    return <Container component={'main'} maxWidth={'xs'}>
        <Grid alignItems="center" style={{minHeight: '100vh'}} container>
            <form noValidate>
                <Typography variant="h5" align={"center"} component="h5">
                    Регистрация
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
                    id={'email'}
                    label={'Электронная почта'}
                    name={'email'}
                    autoComplete={'email'}
                    fullWidth
                    value={email}
                    onChange={event => setEmail(event.target.value)}
                />
                <TextField
                    variant={'outlined'}
                    margin={'normal'}
                    required
                    id={'password'}
                    label={'Пароль'}
                    name={'password'}
                    autoComplete={'password'}
                    type={'password'}
                    fullWidth
                    value={password}
                    onChange={event => setPassword(event.target.value)}
                />
                <Box marginTop={3}>
                    <Button fullWidth
                            onClick={onRegister}
                            variant={'contained'}
                            color={'secondary'}>
                        Продолжить
                    </Button>
                </Box>
            </form>
        </Grid>
    </Container>
};