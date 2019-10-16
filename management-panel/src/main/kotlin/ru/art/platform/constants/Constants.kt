package ru.art.platform.constants

import ru.art.core.constants.StringConstants.*
import ru.art.platform.constants.CommonConstants.AGENT

object CommonConstants {
    const val PLATFORM = "platform"
    const val USER = "user"
    const val TOKEN = "token"
    const val NAME = "name"
    const val PASSWORD = "password"
    const val PROJECT = "project"
    const val NAME_PASSWORD = "namePassword"
    const val TOKEN_LIFE_TIME_DAYS = 1
    const val AGENT = "agent"
}

object DbConstants {
    const val USER_TOKEN_INDEX_ID = 1
    const val USER_NAME_FIELD_NUM = 2
    const val USER_PASSWORD_FIELD_NUM = 3
    const val USER_TOKEN_FIELD_NUM = 2

    const val PROJECT_TITLE_INDEX_ID = 1
    const val PROJECT_TITLE_FIELD_NUM = 2

    const val CONTAINER_PROJECT_TITLE_INDEX_ID = 1
    const val CONTAINER_PROJECT_TITLE_FIELD_NUM = 2
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

object LoggingMessages {
    val KILLED_AGENT_CONTAINER = { name: String -> "Killed agent '$name' container" }
    val REMOVED_AGENT_CONTAINER = { name: String -> "Removed agent '$name' container" }
    val CREATED_AGENT_CONTAINER = { name: String, id: String -> "Created agent '$name' container with id = $id" }
    val STARTED_AGENT_CONTAINER = { name: String, id: String -> "Started agent '$name' container with id = $id" }
    val CONTAINER_FOR_AGENT_INITIALIZED = { name: String -> "Container for agent '$name' initialized" }
    val CONTAINER_FOR_AGENT_ALREADY_INITIALIZED = { name: String, id: String -> "Container with id = $id for agent was already '$name' initialized" }
}

object DockerConstants {
    const val DOCKER_URL = "tcp://localhost:2375"
    const val AGENT_IMAGE = "platform/agent:latest"
    val AGENT_CONTAINER_NAME = { projectTitle: String -> "${projectTitle.replace(SPACE, EMPTY_STRING)}.$AGENT" }
    const val AGENT_MIN_PORT = 9000
    const val AGENT_MAX_PORT = 10000
    const val AGENT_CONTAINER_WAITING_TIME_SECONDS = 1L

    enum class ContainerState {
        created,
        restarting,
        running,
        paused,
        exited,
        dead
    }
}