package ru.art.soap.client.communicator;

import lombok.NoArgsConstructor;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.soap.client.communicator.SoapEnvelopWrappingManager.unwrapFromSoapEnvelope;
import static ru.art.soap.client.communicator.SoapEnvelopWrappingManager.wrapToSoapEnvelop;

@NoArgsConstructor(access = PACKAGE)
class SoapEntityMapping {
    static <T> XmlEntityToModelMapper<T> soapResponseToModel(SoapCommunicationConfiguration configuration) {
        return entity -> cast(configuration.getResponseMapper().map(unwrapFromSoapEnvelope(entity)));
    }

    static <T> XmlEntityFromModelMapper<T> soapRequestFromModel(SoapCommunicationConfiguration configuration) {
        return model -> wrapToSoapEnvelop(configuration.getRequestMapper().map(cast(model)), configuration);
    }
}