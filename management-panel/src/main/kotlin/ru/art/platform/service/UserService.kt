package ru.art.platform.service

import com.auth0.jwt.JWT.*
import com.auth0.jwt.algorithms.Algorithm.*
import ru.art.entity.Entity.*
import ru.art.platform.api.mapping.UserMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.TOKEN
import ru.art.platform.constants.CommonConstants.USER
import ru.art.platform.constants.UserConstants.PASSWORD
import ru.art.platform.constants.UserConstants.SECRET
import ru.art.platform.constants.UserConstants.USERNAME
import ru.art.platform.exception.*
import ru.art.tarantool.constants.TarantoolModuleConstants.*
import ru.art.tarantool.dao.TarantoolDao.*


object UserService {
    fun registerUser(request: UserRegistrationRequest): User = toUser.map(tarantool(PLATFORM)
            .insert(USER, fromUser.map(User.builder()
                    .token(create()
                            .withIssuer(PLATFORM)
                            .withClaim(USERNAME, request.name)
                            .withClaim(PASSWORD, request.password)
                            .sign(HMAC256(SECRET)))
                    .email(request.email)
                    .name(request.name)
                    .build())))

    fun getUser(token: String): User {
        checkToken(token)
        return toUser.map(tarantool(PLATFORM)
                .getByIndex(USER, TOKEN, setOf(token))
                .orElseThrow { PlatformException("User does not exists") })
    }

    fun login(request: UserAuthorizationRequest): User {
        val token = create()
                .withIssuer(PLATFORM)
                .withClaim(USERNAME, request.name)
                .withClaim(PASSWORD, request.password)
                .sign(HMAC256(SECRET))
        return getUser(token).apply {
            tarantool(PLATFORM)
                    .manualId()
                    .put(TOKEN, entityBuilder().stringField(ID_FIELD, token).build())
        }
    }

    fun checkToken(token: String): Boolean {
        require(HMAC256(SECRET))
                .withIssuer(PLATFORM)
                .build()
                .verify(tarantool(PLATFORM).get(TOKEN, setOf(token))
                        .map { entity -> entity.getString(ID_FIELD) }
                        .orElseThrow { PlatformException("Token does not exists") })
        return true
    }
}