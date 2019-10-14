package ru.art.platform.service

import com.auth0.jwt.JWT.*
import com.auth0.jwt.algorithms.Algorithm.*
import ru.art.core.factory.CollectionsFactory.*
import ru.art.entity.PrimitivesFactory.*
import ru.art.logging.LoggingModule.*
import ru.art.platform.api.mapping.UserMapper.*
import ru.art.platform.api.model.*
import ru.art.platform.constants.CommonConstants.NAME_PASSWORD
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
    fun registerUser(request: UserRegistrationRequest): UserRegistrationResponse = UserRegistrationResponse.builder()
            .user(toUser.map(tarantool(PLATFORM)
                    .insert(USER, fromUser.map(User.builder()
                            .name(request.name)
                            .password(request.password)
                            .email(request.email)
                            .build()))))
            .token(tarantool(PLATFORM).put(TOKEN, stringPrimitive(createToken(request.name, request.password))).getString(VALUE))
            .build()

    fun authorize(request: UserAuthorizationRequest): UserAuthorizationResponse = UserAuthorizationResponse.builder()
            .user(toUser.map(tarantool(PLATFORM)
                    .getByIndex(USER, NAME_PASSWORD, fixedArrayOf(request.name, request.password))
                    .orElseThrow { PlatformException(USER_DOES_NOT_EXISTS) }))
            .token(tarantool(PLATFORM).put(TOKEN, stringPrimitive(createToken(request.name, request.password))).getString(VALUE))
            .build()

    fun authenticate(token: String): Boolean = try {
        require(HMAC256(SECRET))
                .withIssuer(PLATFORM)
                .build()
                .verify(tarantool(PLATFORM).getByIndex(TOKEN, TOKEN, setOf(token))
                        .map { entity -> entity.getString(VALUE) }
                        .orElseThrow { PlatformException(TOKEN_DOES_NOT_EXISTS) })
        true
    } catch (e: Throwable) {
        loggingModule().getLogger(UserService::class.java).error(e);
        false
    }
}