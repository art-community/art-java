import {
    GET_CLUSTER_PROFILE_URL, GET_PROFILES_URL,
} from "../constants/StateApi";
import axios from 'axios';
import {buildRequest} from "../configuration/HttpRequestConfiguration";


export const getProfiles = (onSuccess) => axios(GET_PROFILES_URL, buildRequest('GET'))
    .then(response => response.data)
    .then(data => onSuccess(data));

export const getClusterProfile = (profile, onSuccess) => axios(GET_CLUSTER_PROFILE_URL(profile), buildRequest('GET'))
    .then(response => response.data)
    .then(data => onSuccess(data));

export const getModules = (profile, onSuccess) => axios(GET_CLUSTER_PROFILE_URL(profile), buildRequest('GET'))
    .then(response => response.data)
    .then(response => response["moduleEndpointStates"])
    .then(response => Object.keys(response))
    .then(data => onSuccess(data));

export const getEndpoints = (profile, modulePath, onSuccess) => axios(GET_CLUSTER_PROFILE_URL(profile), buildRequest('GET'))
    .then(response => response.data)
    .then(response => response["moduleEndpointStates"])
    .then(response => response[modulePath])
    .then(response => response["endpoints"])
    .then(data => onSuccess(data));
