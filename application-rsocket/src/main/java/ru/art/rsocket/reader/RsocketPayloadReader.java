package ru.art.rsocket.reader;

import io.rsocket.Payload;
import lombok.NoArgsConstructor;
import ru.art.entity.Value;
import ru.art.rsocket.exception.RsocketException;
import ru.art.rsocket.exception.RsocketServerException;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.wrapper.ExceptionWrapper.wrap;
import static ru.art.json.descriptor.JsonEntityReader.readJson;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue.parseFrom;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_DATA_FORMAT;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
import static ru.art.xml.descriptor.XmlEntityReader.readXml;


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
