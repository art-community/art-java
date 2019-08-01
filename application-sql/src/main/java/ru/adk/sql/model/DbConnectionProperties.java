package ru.adk.sql.model;

import lombok.Builder;
import lombok.Getter;
import ru.adk.sql.constants.DbProvider;

@Getter
@Builder
public class DbConnectionProperties {
    private final String login;
    private final String password;
    private final String url;
    private final DbProvider driver;
}