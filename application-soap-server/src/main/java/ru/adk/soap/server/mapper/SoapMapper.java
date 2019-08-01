package ru.adk.soap.server.mapper;

import ru.adk.entity.XmlEntity;
import ru.adk.soap.server.model.SoapFault;
import ru.adk.soap.server.model.SoapRequest;
import ru.adk.soap.server.model.SoapResponse;
import static ru.adk.entity.XmlEntity.xmlEntityBuilder;
import static ru.adk.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import static ru.adk.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import static ru.adk.soap.server.constans.SoapServerModuleConstants.*;

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
