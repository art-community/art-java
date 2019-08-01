package ru.art.soap.server.mapper;

import ru.art.entity.XmlEntity;
import ru.art.soap.server.model.SoapFault;
import ru.art.soap.server.model.SoapRequest;
import ru.art.soap.server.model.SoapResponse;
import static ru.art.entity.XmlEntity.xmlEntityBuilder;
import static ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import static ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import static ru.art.soap.server.constans.SoapServerModuleConstants.*;

public interface SoapMapper {
    XmlEntityToModelMapper<SoapRequest> soapRequestToModelMapper = SoapMapper::buildRequest;
    XmlEntityFromModelMapper<SoapResponse> soapResponseFromModelMapper = response -> xmlEntityBuilder()
            .tag(ENVELOPE)
            .prefix(PREFIX)
            .namespace(NAMESPACE)
            .namespaceField(PREFIX, NAMESPACE)
            .child(xmlEntityBuilder()
                    .tag(HEADER)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .create())
            .child(xmlEntityBuilder()
                    .tag(BODY)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .child(response.getXmlEntity())
                    .create())
            .create();
    XmlEntityFromModelMapper<SoapFault> soapResponseFaultMapper = response -> xmlEntityBuilder()
            .tag(FAULT)
            .prefix(PREFIX)
            .namespace(NAMESPACE)
            .namespaceField(PREFIX, NAMESPACE)
            .child(xmlEntityBuilder()
                    .tag(CODE)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .child(xmlEntityBuilder()
                            .tag(VALUE)
                            .prefix(PREFIX)
                            .namespace(NAMESPACE)
                            .namespaceField(PREFIX, NAMESPACE)
                            .value(response.getCodeValue())
                            .create())
                    .create())
            .child(xmlEntityBuilder()
                    .tag(REASON)
                    .prefix(PREFIX)
                    .namespace(NAMESPACE)
                    .namespaceField(PREFIX, NAMESPACE)
                    .child(xmlEntityBuilder()
                            .tag(TEXT)
                            .prefix(PREFIX)
                            .namespace(NAMESPACE)
                            .namespaceField(PREFIX, NAMESPACE)
                            .value(response.getReasonText())
                            .create())
                    .create())
            .create();

    static SoapRequest buildRequest(XmlEntity xmlEntity) {
        xmlEntity = xmlEntity.find(BODY).getChildren().get(0);
        return SoapRequest.builder()
                .operationId(xmlEntity.getTag())
                .entity(xmlEntity)
                .build();
    }
}
