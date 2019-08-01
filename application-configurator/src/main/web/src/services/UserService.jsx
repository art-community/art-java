import {CHECK_TOKEN_URL, LOGIN_URL} from "../constants/ConfiguratorApi";
import axios from 'axios';
import {buildRequest} from "../configuration/HttpRequestConfiguration";

export const login = (user, onSuccess, onError) => axios(LOGIN_URL, buildRequest(user))
    .then(response => response.data)
    .then(data => onSuccess(data))
    .catch(error => onError(error));

export const checkToken = (token, onSuccess, onError) => axios(CHECK_TOKEN_URL, buildRequest(token))
    .then(response => response.data)
    .then(data => onSuccess(data))
    .catch(error => onError(error));