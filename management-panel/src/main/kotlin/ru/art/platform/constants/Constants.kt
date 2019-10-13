package ru.art.platform.constants

object CommonConstants {
    const val PLATFORM = "platform"
    const val USER = "user"
    const val TOKEN = "token"
    const val NAME = "name"
    const val PASSWORD = "password"
    const val PROJECT = "project"
    const val NAME_PASSWORD = "namePassword"
    const val TOKEN_LIFE_TIME_DAYS = 1
}

object DbConstants {
    const val USER_TOKEN_INDEX_ID = 1
    const val USER_NAME_FIELD_NUM = 2
    const val USER_PASSWORD_FIELD_NUM = 3
    const val USER_TOKEN_FIELD_NUM = 2

    const val PROJECT_NAME_INDEX_ID = 1
    const val PROJECT_NAME_FIELD_NUM = 2
}

object UserConstants {
    const val SECRET = "=5||4F#6N6mnq+5"
}

object ServiceConstants {
    const val REGISTER_USER = "registerUser"
    const val AUTHORIZE = "authorize"
    const val AUTHENTICATE = "authenticate"
    const val ADD_PROJECT = "addProject"
    const val GET_PROJECTS = "getProjects"
    const val DELETE_PROJECT = "deleteProject"
}

object ErrorMessages {
    const val TOKEN_DOES_NOT_EXISTS = "Token does not exists"
    const val USER_DOES_NOT_EXISTS = "User does not exists"
    const val INVALID_META_DATA = "Invalid RSocket metadata"
}