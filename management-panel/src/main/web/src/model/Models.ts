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
    name: string
    url: string
}