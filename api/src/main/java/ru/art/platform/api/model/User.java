package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class User {
    private final Long id;
    private final String token;
    private final String name;
    private final String email;
}
