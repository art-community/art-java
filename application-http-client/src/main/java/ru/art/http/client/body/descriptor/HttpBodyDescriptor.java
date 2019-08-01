package ru.art.http.client.body.descriptor;

import org.apache.http.HttpEntity;
import ru.art.http.client.module.HttpClientModule;
import static java.util.Objects.isNull;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.http.client.constants.HttpClientExceptionMessages.REQUEST_BODY_READING_EXCEPTION;
import static ru.art.logging.LoggingModule.loggingModule;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface HttpBodyDescriptor {
    static byte[] readResponseBody(HttpEntity responseEntity) {
        if (isNull(responseEntity)) return EMPTY_BYTES;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[HttpClientModule.httpClientModule().getResponseBodyBufferSize()];
        try {
            InputStream inputStream = responseEntity.getContent();
            for (int n = inputStream.read(buf); n != -1; n = inputStream.read(buf)) {
                os.write(buf, 0, n);
            }
        } catch (Exception e) {
            loggingModule().getLogger(HttpBodyDescriptor.class).error(REQUEST_BODY_READING_EXCEPTION, e);
            return EMPTY_BYTES;
        }
        return os.toByteArray();
    }
}
