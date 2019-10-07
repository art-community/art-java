package ru.art.platform.service

import com.auth0.jwt.JWT.*
import com.auth0.jwt.algorithms.Algorithm.*
import ru.art.entity.PrimitivesFactory.*
import ru.art.platform.api.mapping.UserMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.TOKEN
import ru.art.platform.constants.CommonConstants.USER
import ru.art.platform.constants.ErrorMessages.TOKEN_DOES_NOT_EXISTS
import ru.art.platform.constants.ErrorMessages.USER_DOES_NOT_EXISTS
import ru.art.platform.constants.UserConstants.SECRET
import ru.art.platform.exception.*
import ru.art.platform.factory.TokenFactory.createToken
import ru.art.tarantool.constants.TarantoolModuleConstants.*
import ru.art.tarantool.dao.TarantoolDao.*


object UserService {
    fun registerUser(request: UserRegistrationRequest): User = toUser.map(tarantool(PLATFORM)
            .insert(USER, fromUser.map(User.builder()
                    .token(createToken(request.name, request.password))
                    .email(request.email)
                    .name(request.name)
                    .build())))

    fun getUser(token: String): User {
        authenticate(token)
        return toUser.map(tarantool(PLATFORM)
                .getByIndex(USER, TOKEN, setOf(token))
                .orElseThrow { PlatformException(USER_DOES_NOT_EXISTS) })
    }

    fun authorize(request: UserAuthorizationRequest) = createToken(request.name, request.password).apply { tarantool(PLATFORM).put(TOKEN, stringPrimitive(this)) }

    fun authenticate(token: String) {
        require(HMAC256(SECRET))
                .withIssuer(PLATFORM)
                .build()
                .verify(tarantool(PLATFORM).getByIndex(TOKEN, TOKEN, setOf(token))
                        .map { entity -> entity.getString(VALUE) }
                        .orElseThrow { PlatformException(TOKEN_DOES_NOT_EXISTS) })
    }
}