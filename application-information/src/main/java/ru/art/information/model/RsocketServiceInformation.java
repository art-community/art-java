package ru.art.information.model;

import lombok.*;
import java.util.*;

@Value
@Builder
public class RsocketServiceInformation {
    private final String id;
    @Singular("method")
    private final Map<String, RsocketServiceMethodInformation> methods;
}
