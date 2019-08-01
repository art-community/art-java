package ru.adk.sql.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbProvider {
    POSTGRES("org.postgresql.Driver"),
    ORACLE("oracle.jdbc.OracleDriver");

    private final String driverClassName;
}
