package io.art.model.implementation.server;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.value.constants.ValueModuleConstants.*;
import lombok.*;

@Builder
@Getter
public class HttpServerModel {
    private final ImmutableMap<String, HttpServiceModel> services;
    private final String host;
    private final Integer port;
    private final boolean compression;
    private final boolean logging;
    private final int fragmentationMtu;
    private final DataFormat defaultDataFormat;
    private final DataFormat defaultMetaDataFormat;
    private final ServiceMethodIdentifier defaultServiceMethod;
}
