package ru.art.rsocket.writer;

import io.rsocket.Payload;
import ru.art.entity.Value;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.writer.RsocketPayloadWriter.writePayload;

public interface ServiceRequestPayloadWriter {
    static Payload writeServiceRequestPayload(String serviceId, String methodId, RsocketDataFormat dataFormat) {
        return writePayload(entityBuilder().stringField(SERVICE_ID, serviceId).stringField(METHOD_ID, methodId).build(), dataFormat);
    }

    static Payload writeServiceRequestPayload(String serviceId, String methodId, Value requestData, RsocketDataFormat dataFormat) {
        return writePayload(entityBuilder().stringField(SERVICE_ID, serviceId).stringField(METHOD_ID, methodId).valueField(REQUEST_DATA, requestData).build(), dataFormat);
    }
}
