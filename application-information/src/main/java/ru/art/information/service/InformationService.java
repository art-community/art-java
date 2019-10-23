package ru.art.information.service;

import lombok.experimental.*;
import ru.art.grpc.server.exception.*;
import ru.art.http.server.exception.*;
import ru.art.information.model.*;
import static java.util.Objects.*;
import static ru.art.core.context.Context.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.grpc.server.module.GrpcServerModule.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.information.collector.GrpcInformationCollector.*;
import static ru.art.information.collector.HttpInformationCollector.*;
import static ru.art.information.collector.RsocketInformationCollector.*;
import static ru.art.information.constants.InformationModuleConstants.*;
import static ru.art.information.module.InformationModule.*;

@UtilityClass
public class InformationService {
    public static InformationResponse getInformation() {
        return InformationResponse.builder()
                .httpInformation(collectHttpInformation())
                .grpcInformation(collectGrpcInformation())
                .rsocketInformation(collectRsocketInformation())
                .build();
    }

    public static boolean getStatus() {
        if (isNull(informationModule().getStatusSupplier())) {
            if (context().hasModule(HTTP_SERVER_MODULE_ID) && !httpServerModuleState().getServer().isWorking()) {
                throw new HttpServerException(HTTP_SERVER_WAS_NOT_INITIALIZED);
            }
            if (context().hasModule(GRPC_SERVER_MODULE_ID) && !grpcServerModuleState().getServer().isWorking()) {
                throw new GrpcServerException(GRPC_SERVER_WAS_NOT_INITIALIZED);
            }
            return true;
        }
        return informationModule().getStatusSupplier().get();
    }
}
