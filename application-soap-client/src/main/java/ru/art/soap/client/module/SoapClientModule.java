package ru.art.soap.client.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.soap.client.configuration.SoapClientModuleConfiguration;
import ru.art.soap.client.configuration.SoapClientModuleConfiguration.SoapClientModuleDefaultConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.soap.client.constants.SoapClientModuleConstants.SOAP_CLIENT_MODULE_ID;

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
