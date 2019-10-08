interface UserRegistrationRequest {
    name: String
    email: String
    password: String
}

interface UserAuthorizationRequest {
    name: String
    password: String
}

interface User {
    id: number
    name: String
    email: String
}

interface UserRegistrationResponse {
    user: User
    token: String
}

interface UserAuthorizationResponse {
    user: User
    token: String
}