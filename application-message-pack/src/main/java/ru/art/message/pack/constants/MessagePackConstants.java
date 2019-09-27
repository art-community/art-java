package ru.art.message.pack.constants;

import ru.art.core.mime.*;
import static java.nio.charset.StandardCharsets.*;
import static ru.art.core.mime.MimeType.*;

public interface MessagePackConstants {
    MimeType APPLICATION_MESSAGE_PACK = mimeType("application", "message-pack");
    MimeType APPLICATION_MESSAGE_PACK_UTF_8 = mimeType("application", "message-pack", UTF_8);
}
