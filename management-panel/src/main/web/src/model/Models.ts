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

interface User {
    id: number
    name: String
    email: String
    token: String
}