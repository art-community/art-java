package ru.adk.soap.server.configuration;

import lombok.Getter;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.adk.soap.server.model.SoapFault;
import static ru.adk.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.UNEXPECTED_ERROR;
import static ru.adk.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.UNEXPECTED_ERROR_TEXT;
import static ru.adk.soap.server.mapper.SoapMapper.soapResponseFaultMapper;

public interface SoapServerModuleConfiguration extends ModuleConfiguration {
    <T extends Exception> T getDefaultFaultResponse();

    <T extends Exception> XmlEntityFromModelMapper<T> getDefaultFaultMapper();

    @Getter
    class SoapServerModuleDefaultConfiguration implements SoapServerModuleConfiguration {
        private SoapFault defaultFaultResponse = SoapFault.builder()
                .codeValue(UNEXPECTED_ERROR)
                .reasonText(UNEXPECTED_ERROR_TEXT)
                .build();
        private XmlEntityFromModelMapper<SoapFault> defaultFaultMapper = soapResponseFaultMapper;
    }
}
