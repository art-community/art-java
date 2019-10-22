package ru.art.information.model;

import lombok.*;
import java.util.*;

@Value
@Builder
public class RsocketInformation {
    private final String webSocketUrl;
    private final String tcpUrl;
    @Singular("service")
    private final Map<String, RsocketServiceInformation> services;
}
