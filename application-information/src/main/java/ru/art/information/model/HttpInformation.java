package ru.art.information.model;

import lombok.*;
import java.util.*;

@Value
@Builder
public class HttpInformation {
    @Singular("service")
    private final Map<String, HttpServiceInformation> services;
}
