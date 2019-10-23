package ru.art.information.model;

import lombok.*;
import java.util.*;

@Value
@Builder
public class GrpcInformation {
    private final String url;
    @Singular("service")
    private final Map<String, GrpcServiceInformation> services;
}
