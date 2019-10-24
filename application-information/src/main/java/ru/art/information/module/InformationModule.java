package ru.art.information.module;

import lombok.*;
import ru.art.core.module.*;
import ru.art.information.configuration.*;
import ru.art.information.mapping.*;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.*;
import static ru.art.core.context.Context.*;
import static ru.art.http.server.HttpServer.*;
import static ru.art.http.server.function.HttpServiceFunction.httpPost;
import static ru.art.information.configuration.InformationModuleConfiguration.*;
import static ru.art.information.constants.InformationModuleConstants.*;
import static ru.art.information.specification.InformationServiceSpecification.*;

@Getter
public class InformationModule implements Module<InformationModuleConfiguration, ModuleState> {
    private final String id = INFORMATION_MODULE_ID;
    private final InformationModuleConfiguration defaultConfiguration = new InformationModuleDefaultConfiguration();
    @Getter(lazy = true)
    private final static InformationModuleConfiguration informationModule = context().getModule(INFORMATION_MODULE_ID, InformationModule::new);

    public static InformationModuleConfiguration informationModule() {
        return getInformationModule();
    }

    public static void main(String[] args) {
        useAgileConfigurations();
        registerInformationService();

        httpPost("/myPost")
                .requestMapper(GrpcInformationMapper.toGrpcInformation)
                .responseMapper(GrpcInformationMapper.fromGrpcInformation)
                .produce(() -> null);
        startHttpServer().await();
    }
}
