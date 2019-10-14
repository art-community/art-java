import * as React from 'react';
import {useState} from 'react';
import {Box, Button, Container, Grid, Popper, TextField, Typography,} from '@material-ui/core';
import {TOKEN_COOKIE} from "../../constants/Constants";
import {authorize} from "../../api/PlatformApi";
// @ts-ignore
import Cookies from "js-cookie";

interface AuthorizationComponentProps {
    onAuthorize: (token: string) => void
}

export const AuthorizationComponent = (props: AuthorizationComponentProps) => {
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [authorizationFailed, setAuthorizationFailed] = useState(false);
    const [notAuthorizedMessageAnchor, setNotAuthorizedMessageAnchor] = useState<null | HTMLElement>(null);

    const handleAuthorize = (response: UserAuthorizationResponse) => props.onAuthorize(response.token);

    const handleError = () => setAuthorizationFailed(true);

//    const onRegister = () => history.push(REGISTER_PATH);

    const onAuthorize = () => authorize({name: name, password: password}, handleAuthorize, handleError);

    return <Container component={'main'} maxWidth={'xs'}>
        <Grid alignItems="center" style={{minHeight: '100vh'}} container>
            <form>
                <Typography variant="h5" align={"center"} component="h5">
                    Добро пожаловать в ART платформу
                </Typography>
                <TextField
                    variant={'outlined'}
                    margin={'normal'}
                    required
                    label={'Имя пользователя'}
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
                    label={'Пароль'}
                    autoComplete={'password'}
                    type={'password'}
                    value={password}
                    onChange={event => setPassword(event.target.value)}
                />
                <Box marginTop={3}>
                    <Grid container spacing={3}>
                        <Grid item xs={12}>
                            {<Button
                                ref={ref => setNotAuthorizedMessageAnchor(ref)}
                                fullWidth
                                onClick={onAuthorize}
                                variant={'contained'}
                                color={'primary'}>
                                Войти
                            </Button>}
                            <Popper open={authorizationFailed && Boolean(notAuthorizedMessageAnchor)}
                                    placement={"top"}
                                    anchorEl={notAuthorizedMessageAnchor}>
                                <Typography color={"error"}>
                                    Пользователь не найден
                                </Typography>
                            </Popper>
                        </Grid>
                        {/*<Grid item xs={6}>*/}
                        {/*    <Button*/}
                        {/*        fullWidth*/}
                        {/*        onClick={onRegister}*/}
                        {/*        variant={'contained'}*/}
                        {/*        color={'secondary'}>*/}
                        {/*        Регистрация*/}
                        {/*    </Button>*/}
                        {/*</Grid>*/}
                    </Grid>
                </Box>
            </form>
        </Grid>
    </Container>
};