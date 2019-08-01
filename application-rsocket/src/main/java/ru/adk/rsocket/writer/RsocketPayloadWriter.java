package ru.adk.rsocket.writer;

import io.rsocket.Payload;
import lombok.NoArgsConstructor;
import ru.adk.entity.Value;
import ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.adk.rsocket.exception.RsocketException;
import static io.rsocket.util.DefaultPayload.create;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.entity.Value.asXmlEntity;
import static ru.adk.json.descriptor.JsonEntityWriter.writeJson;
import static ru.adk.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import static ru.adk.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_DATA_FORMAT;
import static ru.adk.rsocket.module.RsocketModule.rsocketModule;
import static ru.adk.xml.descriptor.XmlEntityWriter.writeXml;

@NoArgsConstructor(access = PRIVATE)
public class RsocketPayloadWriter {
    public static Payload writePayload(Value value, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return create(writeProtobuf(value).toByteArray());
            case JSON:
                return create(writeJson(value));
            case XML:
                return create(writeXml(asXmlEntity(value)));
        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDefaultDataFormat()));
    }
}
