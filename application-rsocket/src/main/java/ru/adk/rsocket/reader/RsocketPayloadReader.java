package ru.adk.rsocket.reader;

import io.rsocket.Payload;
import lombok.NoArgsConstructor;
import ru.adk.entity.Value;
import ru.adk.rsocket.exception.RsocketException;
import ru.adk.rsocket.exception.RsocketServerException;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.wrapper.ExceptionWrapper.wrap;
import static ru.adk.json.descriptor.JsonEntityReader.readJson;
import static ru.adk.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue.parseFrom;
import static ru.adk.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_DATA_FORMAT;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.adk.rsocket.module.RsocketModule.rsocketModule;
import static ru.adk.xml.descriptor.XmlEntityReader.readXml;


@NoArgsConstructor(access = PRIVATE)
public class RsocketPayloadReader {
    public static Value readPayload(Payload payload, RsocketDataFormat dataFormat) {
        switch (dataFormat) {
            case PROTOBUF:
                return readProtobuf(wrap(() -> parseFrom(payload.getData()), RsocketServerException::new));
            case JSON:
                return readJson(wrap(payload::getDataUtf8, RsocketServerException::new));
            case XML:
                return readXml(wrap(payload::getDataUtf8, RsocketServerException::new));
        }
        throw new RsocketException(format(UNSUPPORTED_DATA_FORMAT, rsocketModule().getDefaultDataFormat()));
    }
}
