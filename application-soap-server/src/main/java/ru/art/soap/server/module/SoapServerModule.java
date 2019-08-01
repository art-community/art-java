package ru.art.soap.server.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.soap.server.configuration.SoapServerModuleConfiguration;
import ru.art.soap.server.configuration.SoapServerModuleConfiguration.SoapServerModuleDefaultConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.soap.server.constans.SoapServerModuleConstants.SOAP_SERVER_MODULE_ID;

@Getter
public class SoapServerModule implements Module<SoapServerModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static SoapServerModuleConfiguration soapServerModule = context().getModule(SOAP_SERVER_MODULE_ID, new SoapServerModule());
    private final String id = SOAP_SERVER_MODULE_ID;
    private SoapServerModuleConfiguration defaultConfiguration = new SoapServerModuleDefaultConfiguration();

    public static SoapServerModuleConfiguration soapServerModule() {
        return getSoapServerModule();
    }

}
