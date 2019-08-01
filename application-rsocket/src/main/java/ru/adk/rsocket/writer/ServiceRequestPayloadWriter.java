package ru.adk.rsocket.writer;

import io.rsocket.Payload;
import ru.adk.entity.Value;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.rsocket.constants.RsocketModuleConstants.*;
import static ru.adk.rsocket.writer.RsocketPayloadWriter.writePayload;

public interface ServiceRequestPayloadWriter {
    static Payload writeServiceRequestPayload(String serviceId, String methodId, RsocketDataFormat dataFormat) {
        return writePayload(entityBuilder().stringField(SERVICE_ID, serviceId).stringField(METHOD_ID, methodId).build(), dataFormat);
    }

    static Payload writeServiceRequestPayload(String serviceId, String methodId, Value requestData, RsocketDataFormat dataFormat) {
        return writePayload(entityBuilder().stringField(SERVICE_ID, serviceId).stringField(METHOD_ID, methodId).valueField(REQUEST_DATA, requestData).build(), dataFormat);
    }
}
