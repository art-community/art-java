package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class UserRegistrationResponse {
    private final User user;
    private final String token;
}
