import {
    ADD_PROJECT,
    AUTHENTICATE,
    AUTHORIZE,
    DELETE_PROJECT,
    GET_PROJECTS,
    REGISTER_USER
} from "../constants/Constants";
import {createMethodRequest, fireAndForget, requestResponse, requestStream} from "./PlatformClient";
import {
    Assembly,
    Project,
    ProjectRequest,
    UserAuthorizationRequest,
    UserAuthorizationResponse,
    UserRegistrationRequest,
    UserRegistrationResponse
} from "../model/Models";
import 'moment/locale/ru'

export const registerUser = (requestData: UserRegistrationRequest, onComplete: (user: UserRegistrationResponse) => void) => {
    requestResponse(createMethodRequest(REGISTER_USER, requestData), onComplete);
};

export const authorize = (requestData: UserAuthorizationRequest, onComplete: (user: UserAuthorizationResponse) => void, onError: () => void) => {
    requestResponse(createMethodRequest(AUTHORIZE, requestData), onComplete, onError)
};

export const authenticate = (requestData: string, onComplete: () => void, onError: () => void) => {
    requestResponse(createMethodRequest(AUTHENTICATE, requestData), onComplete, onError)
};

export const addProject = (requestData: ProjectRequest, onProjectUpdate: (project: Project) => void, onError: () => void) => {
    requestStream(createMethodRequest(ADD_PROJECT, requestData), onProjectUpdate, onError)
};

export const deleteProject = (requestData: number) => {
    fireAndForget(createMethodRequest(DELETE_PROJECT, requestData))
};

export const getProjects = (onComplete: (projects: Map<number, Project>) => void, onError: () => void) => {
    requestResponse(createMethodRequest(GET_PROJECTS),
        (projects: Project[]) => onComplete(projects.groupByIgnoreDuplicates(project => project.id)),
        onError);
};

export const getAssemblies = (onComplete: (assemblies: Map<number, Assembly>) => void) => {

};

export const buildProject = () => {

};