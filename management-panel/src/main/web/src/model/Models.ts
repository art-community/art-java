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
    technologies: Set<string>
}

export interface ProjectRequest {
    title: string
    gitUrl: string
    jiraUrl?: string
}

export interface Assembly {
    id :number
    title: string
    branch?: string
    commit?: string
    tag?: string
    startDateTime: Moment
    endDateTime?: Moment
    state: AssemblyState
}

export enum AssemblyState {
    FAILED,
    BUILDING,
    DONE
}