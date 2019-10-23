package ru.art.information.model;

import lombok.*;
import java.util.*;

@Value
@Builder
public class HttpServiceInformation {
    private final String id;
    @Singular("method")
    private final Map<String, HttpServiceMethodInformation> methods;
}
