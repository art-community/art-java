package ru.art.platform.constants

object CommonConstants {
    const val PLATFORM = "platform"
    const val USER = "user"
    const val TOKEN = "token"
    const val TOKEN_LIFE_TIME_DAYS = 1
}

object DbConstants {
    const val TOKEN_INDEX_ID = 1
    const val TOKEN_FIELD_NUM = 2
}

object UserConstants {
    const val SECRET = "=5||4F#6N6mnq+5"
    const val NAME = "name"
    const val PASSWORD = "password"
}

object ServiceConstants {
    const val REGISTER_USER = "registerUser"
    const val GET_USER = "getUser"
    const val AUTHORIZE = "authorize"
    const val AUTHENTICATE = "authenticate"
}

object ErrorMessages {
    const val TOKEN_DOES_NOT_EXISTS = "Token does not exists"
    const val USER_DOES_NOT_EXISTS = "User does not exists"
}