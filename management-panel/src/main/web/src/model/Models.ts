interface UserRegistrationRequest {
    name: String
    email: String
    password: String
}

interface UserRequest {
    name?: String | null
    password?: String | null
    token?: String | null
}

interface UserAuthorizationRequest {
    name: String
    password: String
}

interface User {
    id: number
    name: String
    email: String
    token: String
}