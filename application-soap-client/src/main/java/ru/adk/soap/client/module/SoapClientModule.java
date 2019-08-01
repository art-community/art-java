package ru.adk.soap.client.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.soap.client.configuration.SoapClientModuleConfiguration;
import ru.adk.soap.client.configuration.SoapClientModuleConfiguration.SoapClientModuleDefaultConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.soap.client.constants.SoapClientModuleConstants.SOAP_CLIENT_MODULE_ID;

@Getter
public class SoapClientModule implements Module<SoapClientModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final SoapClientModuleConfiguration soapClientModule = context().getModule(SOAP_CLIENT_MODULE_ID, new SoapClientModule());
    private final String id = SOAP_CLIENT_MODULE_ID;
    private final SoapClientModuleConfiguration defaultConfiguration = new SoapClientModuleDefaultConfiguration();

    public static SoapClientModuleConfiguration soapClientModule() {
        return getSoapClientModule();
    }
}
