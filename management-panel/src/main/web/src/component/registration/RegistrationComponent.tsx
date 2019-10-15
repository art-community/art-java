import * as React from 'react';
import {useState} from 'react';
import {Box, Button, Container, Grid, TextField, Typography, useTheme,} from '@material-ui/core';
import {useHistory} from "react-router";
import {PROJECT_PATH, TOKEN_COOKIE} from "../../constants/Constants";
import {registerUser} from "../../api/PlatformApi";
import Cookies from "js-cookie";

export const RegistrationComponent = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const theme = useTheme();
    const history = useHistory();

    const onRegister = () => registerUser({name: name, email: email, password: password}, response => {
        Cookies.set(TOKEN_COOKIE, response.token);
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
                           label={'Имя пользователя'}
                           fullWidth
                           autoFocus
                           value={name}
                           onChange={event => setName(event.target.value)}
                />
                <TextField
                    variant={'outlined'}
                    margin={'normal'}
                    required
                    label={'Электронная почта'}
                    fullWidth
                    value={email}
                    onChange={event => setEmail(event.target.value)}
                />
                <TextField
                    variant={'outlined'}
                    margin={'normal'}
                    required
                    label={'Пароль'}
                    type={'password'}
                    fullWidth
                    value={password}
                    onChange={event => setPassword(event.target.value)}
                />
                <Box mt={theme.spacing(0.5)}>
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