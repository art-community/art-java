/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {GET_CLUSTER_PROFILE_URL, GET_PROFILES_URL,} from "../constants/StateApi";
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
