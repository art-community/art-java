package ru.adk.soap.client.communicator;

import lombok.NoArgsConstructor;
import ru.adk.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import static lombok.AccessLevel.PACKAGE;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.soap.client.communicator.SoapEnvelopWrappingManager.unwrapFromSoapEnvelope;
import static ru.adk.soap.client.communicator.SoapEnvelopWrappingManager.wrapToSoapEnvelop;

@NoArgsConstructor(access = PACKAGE)
class SoapEntityMapping {
    static <T> XmlEntityToModelMapper<T> soapResponseToModel(SoapCommunicationConfiguration configuration) {
        return entity -> cast(configuration.getResponseMapper().map(unwrapFromSoapEnvelope(entity)));
    }

    static <T> XmlEntityFromModelMapper<T> soapRequestFromModel(SoapCommunicationConfiguration configuration) {
        return model -> wrapToSoapEnvelop(configuration.getRequestMapper().map(cast(model)), configuration);
    }
}