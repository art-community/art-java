interface UserRegistrationRequest {
    name: string
    email: string
    password: string
}

interface UserAuthorizationRequest {
    name: string
    password: string
}

interface User {
    id: number
    name: string
    email: string
}

interface UserRegistrationResponse {
    user: User
    token: string
}

interface UserAuthorizationResponse {
    user: User
    token: string
}

interface Project {
    id: number
    name: string
    gitUrl: string
    jiraUrl: string
}

interface ProjectRequest {
    name: string
    gitUrl: string
    jiraUrl?: string
}