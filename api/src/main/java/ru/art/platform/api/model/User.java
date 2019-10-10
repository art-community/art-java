package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class User {
    private final Long id;
    private final String name;
    private final String password;
    private final String email;
    private final String token;
}
