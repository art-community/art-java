/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import {
    APPLY_CONFIGURATION_URL,
    GET_APPLICATION_CONFIGURATION_URL,
    GET_CONFIGURATION_URL,
    GET_MODULES_URL,
    GET_PROFILES_URL,
    UPLOAD_APPLICATION_CONFIGURATION_URL,
    UPLOAD_CONFIGURATION_URL,
    UPLOAD_MODULE_CONFIGURATION_URL,
    UPLOAD_PROFILE_CONFIGURATION_URL
} from "../constants/ConfiguratorApi";
import axios from 'axios';
import {buildRequest} from "../configuration/HttpRequestConfiguration";


export const getProfiles = (onSuccess) => axios(GET_PROFILES_URL, buildRequest())
    .then(response => response.data)
    .then(data => data.map(element => element.profileId))
    .then(data => onSuccess(data));

export const getModules = (profile, onSuccess) => axios(GET_MODULES_URL, buildRequest())
    .then(response => response.data)
    .then(data => data.filter(element => element.profileId === profile))
    .then(data => data.map(element => element.moduleId))
    .then(data => onSuccess(data));

export const getProfileConfiguration = (profile, onSuccess) => axios(GET_CONFIGURATION_URL, buildRequest({profileId: profile}))
    .then(response => response.data)
    .then(data => onSuccess(data));

export const getApplicationConfiguration = (onSuccess) => axios(GET_APPLICATION_CONFIGURATION_URL, buildRequest())
    .then(response => response.data)
    .then(data => onSuccess(data));

export const getModuleConfiguration = (profile, module, onSuccess) => {
    axios(GET_CONFIGURATION_URL, buildRequest({profileId: profile, moduleId: module}))
        .then(response => response.data)
        .then(data => onSuccess(data))
};

export const uploadApplicationConfiguration = (configuration) => axios(UPLOAD_APPLICATION_CONFIGURATION_URL, buildRequest(configuration));

export const uploadProfileConfiguration = (profileId, configuration) => axios(UPLOAD_PROFILE_CONFIGURATION_URL,
    buildRequest({
        profileId: profileId,
        configuration: configuration
    }));

export const uploadModuleConfiguration = (moduleKey, configuration) => axios(UPLOAD_MODULE_CONFIGURATION_URL, buildRequest({
    moduleKey: moduleKey,
    configuration: configuration
}));

export const uploadConfiguration = (configuration) => axios(UPLOAD_CONFIGURATION_URL, buildRequest(configuration));

export const applyConfiguration = (moduleKey) => axios(APPLY_CONFIGURATION_URL, buildRequest(moduleKey));