package ru.art.sql.state;

import com.zaxxer.hikari.*;
import io.dropwizard.db.*;
import lombok.*;
import lombok.experimental.*;
import ru.art.core.module.*;

@Getter
@Setter
@Accessors(fluent = true)
public class SqlModuleState implements ModuleState {
    private volatile HikariDataSource hikariDataSource;
    private volatile ManagedPooledDataSource tomcatDataSource;
}
