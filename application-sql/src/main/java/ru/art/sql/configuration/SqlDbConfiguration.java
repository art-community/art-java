package ru.art.sql.configuration;

import com.zaxxer.hikari.*;
import lombok.*;
import org.apache.tomcat.jdbc.pool.*;
import org.jooq.*;
import org.jooq.impl.*;
import ru.art.sql.constants.*;
import ru.art.sql.model.*;
import static ru.art.sql.constants.ConnectionPoolType.*;
import static ru.art.sql.factory.SqlConnectionPoolsFactory.*;

@Getter
@Builder(toBuilder = true)
public class SqlDbConfiguration {
    @Builder.Default
    private final Configuration jooqConfiguration = new DefaultConfiguration();
    @Builder.Default
    private final ConnectionPoolType connectionPoolType = TOMCAT;
    @Builder.Default
    private final boolean enableMetrics = true;
    @Builder.Default
    private final HikariConfig hikariPoolConfig = createHikariPoolConfig(DbConnectionProperties.builder().build());
    @Builder.Default
    private final PoolProperties tomcatPoolConfig = createTomcatPoolConfig(DbConnectionProperties.builder().build());
}