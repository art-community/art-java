package io.art.sql.configuration;

import com.zaxxer.hikari.*;
import lombok.*;
import org.apache.tomcat.jdbc.pool.*;
import org.jooq.*;
import org.jooq.impl.*;
import io.art.sql.constants.*;
import io.art.sql.model.*;
import static io.art.sql.factory.SqlDbDefaultsFactory.createDefaultHikariPoolConfig;
import static io.art.sql.factory.SqlDbDefaultsFactory.createDefaultTomcatPoolConfig;
import static io.art.sql.constants.ConnectionPoolType.*;

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
    private final boolean enableTracing = false;
    @Builder.Default
    private final HikariConfig hikariPoolConfig = createDefaultHikariPoolConfig(DbConnectionProperties.builder().build());
    @Builder.Default
    private final PoolProperties tomcatPoolConfig = createDefaultTomcatPoolConfig(DbConnectionProperties.builder().build());
}
