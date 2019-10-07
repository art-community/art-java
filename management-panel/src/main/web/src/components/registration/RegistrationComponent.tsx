import * as React from 'react';
import {useState} from 'react';
import {Box, Button, Container, Grid, TextField, Typography,} from '@material-ui/core';
import {useHistory} from "react-router";
import {useStore} from 'react-hookstore';
import {PROJECT_PATH, SLASH, USER_STORE} from "../../constants/Constants";
import {normalizeRoutingPath} from "../../normalization/RoutingPathNormalization";
import {registerUser} from "../../api/PlatformApi";

export function RegistrationComponent() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [_, setUser] = useStore(USER_STORE);
    const history = useHistory();
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
                            onClick={() => registerUser({name: name, email: email, password: password}, user => {
                                setUser(user);
                                history.push(normalizeRoutingPath(PROJECT_PATH));
                            })}
                            variant={'contained'}
                            color={'secondary'}>
                        Продолжить
                    </Button>
                </Box>
            </form>
        </Grid>
    </Container>
}