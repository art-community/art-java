import {
    ADD_PROJECT,
    AUTHENTICATE,
    AUTHORIZE,
    DELETE_PROJECT,
    GET_PROJECTS,
    REGISTER_USER, RU
} from "../constants/Constants";
import {createMethodRequest, fireAndForget, requestResponse} from "./PlatformClient";
import {
    Assembly,
    AssemblyState,
    Project,
    ProjectRequest,
    UserAuthorizationRequest,
    UserAuthorizationResponse, UserRegistrationRequest, UserRegistrationResponse
} from "../model/Models";
import moment from "moment"
import 'moment/locale/ru'

export const registerUser = (requestData: UserRegistrationRequest, onComplete: (user: UserRegistrationResponse) => void) => {
    requestResponse(createMethodRequest(REGISTER_USER, requestData))
        .then(onComplete);
};

export const authorize = (requestData: UserAuthorizationRequest, onComplete: (user: UserAuthorizationResponse) => void, onError: () => void) => {
    requestResponse(createMethodRequest(AUTHORIZE, requestData))
        .then(onComplete)
        .catch(onError);
};

export const authenticate = (requestData: string, onComplete: (authorized: boolean) => void, onError: () => void) => {
    requestResponse(createMethodRequest(AUTHENTICATE, requestData))
        .then(onComplete)
        .catch(onError)
};

export const addProject = (requestData: ProjectRequest, onComplete: (project: Project) => void, onError: () => void) => {
    requestResponse(createMethodRequest(ADD_PROJECT, requestData))
        .then(onComplete)
        .catch(onError)
};

export const deleteProject = (requestData: number) => {
    fireAndForget(createMethodRequest(DELETE_PROJECT, requestData))
};

export const getProjects = (onComplete: (projects: Map<number, Project>) => void, onError: () => void) => {
    requestResponse(createMethodRequest(GET_PROJECTS))
        .then((projects: Project[]) => onComplete(projects.groupByIgnoreDuplicates(project => project.id)))
        .catch(console.error)
        .catch(onError)
};

export const getAssemblies = (onComplete: (assemblies: Map<number, Assembly>) => void) => {
    const map = new Map<number, Assembly>();
    map.set(1, {
        id: 1,
        title: "Сборка 1",
        branch: "master",
        commit: "3523axzc32q65",
        tag: "1.0.0",
        startDateTime: moment().locale(RU),
        state: AssemblyState.BUILDING
    });
    map.set(2, {
        id: 2,
        title: "Сборка 2",
        branch: "master",
        commit: "3523axzc32q65",
        tag: "1.0.0",
        startDateTime: moment().locale(RU),
        state: AssemblyState.FAILED
    });
    map.set(3, {
        id: 3,
        title: "Сборка 3",
        branch: "master",
        commit: "3523axzc32q65",
        tag: "1.0.0",
        startDateTime: moment().locale(RU),
        state: AssemblyState.DONE
    });
    map.set(4, {
        id: 4,
        title: "Сборка 1",
        branch: "master",
        commit: "3523axzc32q65",
        tag: "1.0.0",
        startDateTime: moment().locale(RU),
        state: AssemblyState.BUILDING
    });
    map.set(5, {
        id: 5,
        title: "Сборка 2",
        branch: "master",
        commit: "3523axzc32q65",
        tag: "1.0.0",
        startDateTime: moment().locale(RU),
        state: AssemblyState.FAILED
    });
    map.set(6, {
        id: 6,
        title: "Сборка 3",
        branch: "master",
        commit: "3523axzc32q65",
        tag: "1.0.0",
        startDateTime: moment().locale(RU),
        state: AssemblyState.DONE
    });
    onComplete(map)
};