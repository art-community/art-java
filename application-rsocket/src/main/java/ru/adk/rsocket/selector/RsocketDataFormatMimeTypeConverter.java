package ru.adk.rsocket.selector;

import lombok.NoArgsConstructor;
import static io.rsocket.metadata.WellKnownMimeType.*;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.rsocket.constants.RsocketModuleConstants.BINARY_MIME_TYPE;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat.*;
import static ru.adk.rsocket.module.RsocketModule.rsocketModule;

@NoArgsConstructor(access = PRIVATE)
public class RsocketDataFormatMimeTypeConverter {
    public static RsocketDataFormat fromMimeType(String mimeType) {
        if (APPLICATION_JSON.getString().equalsIgnoreCase(mimeType)) return JSON;
        if (APPLICATION_PROTOBUF.getString().equalsIgnoreCase(mimeType)) return PROTOBUF;
        if (APPLICATION_XML.getString().equalsIgnoreCase(mimeType)) return XML;
        if (TEXT_XML.getString().equalsIgnoreCase(mimeType)) return XML;
        return rsocketModule().getDefaultDataFormat();
    }

    public static String toMimeType(RsocketDataFormat dataFormat) {
        switch (getOrElse(dataFormat, rsocketModule().getDefaultDataFormat())) {
            case PROTOBUF:
                return APPLICATION_PROTOBUF.getString();
            case JSON:
                return APPLICATION_JSON.getString();
            case XML:
                return APPLICATION_XML.getString();
        }
        return BINARY_MIME_TYPE;
    }
}
