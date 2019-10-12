import {AUTHENTICATE, AUTHORIZE, REGISTER_USER} from "../constants/Constants";
import {createMethodRequest, requestResponse} from "./PlatformClient";

export const registerUser = (requestData: UserRegistrationRequest, onComplete: (user: UserRegistrationResponse) => void) => {
    requestResponse(createMethodRequest(REGISTER_USER, requestData))
        .then(response => onComplete(response));
};

export const authorize = (requestData: UserAuthorizationRequest, onComplete: (user: UserAuthorizationResponse) => void, onError: () => void) => {
    requestResponse(createMethodRequest(AUTHORIZE, requestData))
        .then(response => onComplete(response))
        .catch(() => onError());
};

export const authenticate = (requestData: string, onComplete: () => void, onError: () => void) => {
    requestResponse(createMethodRequest(AUTHENTICATE, requestData)).then(() => onComplete()).catch(() => onError());
};

export const addProject = (requestData: Project, onComplete: () => void) => {

};

export const getProjects = (onComplete: (projects: Set<Project>) => void) => {

};

export const getTechnologies = (onComplete: (strings: Set<string>) => void) => {
    onComplete(new Set(['gradle', 'npm']))
};