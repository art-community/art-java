package ru.art.soap.client.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import static ru.art.soap.client.constants.SoapClientModuleConstants.*;

public interface SoapClientModuleConfiguration extends ModuleConfiguration {
    String getEnvelopePrefix();

    String getEnvelopeNamespace();

    String getBodyPrefix();

    String getBodyNamespace();

    @Getter
    class SoapClientModuleDefaultConfiguration implements SoapClientModuleConfiguration {
        private final String envelopePrefix = SOAP_ENVELOPE_DEFAULT_PREFIX;
        private final String envelopeNamespace = SOAP_ENVELOPE_DEFAULT_NAMESPACE;
        private final String bodyPrefix = SOAP_BODY_DEFAULT_PREFIX;
        private final String bodyNamespace = SOAP_BODY_DEFAULT_NAMESPACE;
    }
}
