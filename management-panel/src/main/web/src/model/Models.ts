import {Moment} from "moment";

export interface UserRegistrationRequest {
    name: string
    email: string
    password: string
}

export interface UserAuthorizationRequest {
    name: string
    password: string
}

export interface User {
    id: number
    name: string
    email: string
}

export interface UserRegistrationResponse {
    user: User
    token: string
}

export interface UserAuthorizationResponse {
    user: User
    token: string
}

export interface Project {
    id: number
    title: string
    gitUrl: string
    jiraUrl: string
    technologies: string[]
    state: string
}

export interface ProjectRequest {
    title: string
    gitUrl: string
    jiraUrl?: string
}

export interface Assembly {
    id: number
    title: string
    branch?: string
    commit?: string
    tag?: string
    startDateTime: Moment
    endDateTime?: Moment
    state: AssemblyState
}

export interface ServiceResponse {
    serviceMethodCommand: ServiceMethodCommand
    responseData: any
    serviceExecutionException: ServiceExecutionException
}

export interface ServiceExecutionException {
    errorCode: string
    errorMessage: string
}

export interface ServiceMethodCommand {
    serviceId: string
    methodId: string
}

export interface BuildRequest {
    projectId: number
}

export enum AssemblyState {
    FAILED,
    BUILDING,
    DONE
}