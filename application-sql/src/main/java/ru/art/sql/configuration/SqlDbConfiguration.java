package ru.art.sql.configuration;

import com.zaxxer.hikari.*;
import lombok.*;
import org.apache.tomcat.jdbc.pool.*;
import org.jooq.*;
import org.jooq.conf.*;
import org.jooq.impl.*;
import ru.art.sql.constants.*;
import ru.art.sql.model.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.sql.constants.ConnectionPoolType.*;
import static ru.art.sql.constants.DbProvider.*;
import static ru.art.sql.factory.SqlConnectionPoolsFactory.*;

@Getter
@Builder(toBuilder = true)
public class SqlDbConfiguration {
    @Builder.Default
    private final Configuration jooqConfiguration = new DefaultConfiguration();
    @Builder.Default
    private final ConnectionPoolType connectionPoolType = TOMCAT;
    @Builder.Default
    private final String jdbcUrl = EMPTY_STRING;
    @Builder.Default
    private final String jdbcLogin = EMPTY_STRING;
    @Builder.Default
    private final String jdbcPassword = EMPTY_STRING;
    @Builder.Default
    private final DbProvider dbProvider = POSTGRES;
    @Builder.Default
    private final boolean enableMetrics = true;
    @Builder.Default
    private final HikariConfig hikariPoolConfig = createHikariPoolConfig(DbConnectionProperties.builder()
            .url(jdbcUrl)
            .login(jdbcLogin)
            .password(jdbcPassword)
            .driver(dbProvider)
            .build());
    @Builder.Default
    private final PoolProperties tomcatPoolConfig = createTomcatPoolConfig(DbConnectionProperties.builder()
            .url(jdbcUrl)
            .login(jdbcLogin)
            .password(jdbcPassword)
            .driver(dbProvider)
            .build());
}