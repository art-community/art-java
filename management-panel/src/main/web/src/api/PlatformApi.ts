import {
    ADD_PROJECT,
    AUTHENTICATE,
    AUTHORIZE,
    DELETE_PROJECT,
    GET_PROJECTS,
    REGISTER_USER
} from "../constants/Constants";
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

export const addProject = (requestData: ProjectRequest, onComplete: (project: Project) => void) => {
    requestResponse(createMethodRequest(ADD_PROJECT, requestData))
        .then(project => onComplete(project))
};

export const deleteProject = (requestData: number, onComplete: () => void) => {
    requestResponse(createMethodRequest(DELETE_PROJECT, requestData))
        .then(() => onComplete())
};

export const getProjects = (onComplete: (projects: Map<number, Project>) => void) => {
    requestResponse(createMethodRequest(GET_PROJECTS))
        .then((projects: Project[]) => onComplete(projects.groupByIgnoreDuplicates(project => project.id)))
};