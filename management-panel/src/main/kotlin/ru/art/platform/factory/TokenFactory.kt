package ru.art.platform.factory

import com.auth0.jwt.JWT.*
import com.auth0.jwt.algorithms.Algorithm.*
import ru.art.platform.constants.CommonConstants.NAME
import ru.art.platform.constants.CommonConstants.PASSWORD
import ru.art.platform.constants.CommonConstants.PLATFORM
import ru.art.platform.constants.CommonConstants.TOKEN_LIFE_TIME_DAYS
import ru.art.platform.constants.UserConstants.SECRET
import java.time.Duration.*
import java.util.*

object TokenFactory {
    fun createToken(name: String, password: String): String = create()
            .withIssuer(PLATFORM)
            .withClaim(NAME, name)
            .withClaim(PASSWORD, password)
            .withExpiresAt(Date(Date().time + ofDays(TOKEN_LIFE_TIME_DAYS.toLong()).toMillis()))
            .sign(HMAC256(SECRET))
}