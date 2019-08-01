package ru.art.soap.server.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.soap.server.model.SoapFault;
import static ru.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.UNEXPECTED_ERROR;
import static ru.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.UNEXPECTED_ERROR_TEXT;
import static ru.art.soap.server.mapper.SoapMapper.soapResponseFaultMapper;

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
