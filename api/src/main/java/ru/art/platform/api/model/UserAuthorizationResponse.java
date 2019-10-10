package ru.art.platform.api.model;

import lombok.*;

@Value
@Builder
public class UserAuthorizationResponse  {
    private final User user;
    private final String token;
}
