package ru.art.http.server.body.descriptor;

import ru.art.http.server.module.HttpServerModule;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.http.server.constants.HttpServerExceptionMessages.REQUEST_BODY_READING_EXCEPTION;
import static ru.art.http.server.constants.HttpServerExceptionMessages.REQUEST_BODY_WRITING_EXCEPTION;
import static ru.art.logging.LoggingModule.loggingModule;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

public interface HttpBodyDescriptor {
    static byte[] readRequestBody(HttpServletRequest request) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[HttpServerModule.httpServerModule().getRequestBodyBufferSize()];
        try {
            ServletInputStream inputStream = request.getInputStream();
            for (int n = inputStream.read(buf); n != -1; n = inputStream.read(buf)) {
                os.write(buf, 0, n);
            }
        } catch (Exception e) {
            loggingModule()
                    .getLogger(HttpBodyDescriptor.class)
                    .error(REQUEST_BODY_READING_EXCEPTION, e);
            return EMPTY_BYTES;
        }
        return os.toByteArray();
    }

    static void writeResponseBody(HttpServletResponse response, byte[] body) {
        try {
            response.getOutputStream().write(body);
        } catch (Exception e) {
            loggingModule()
                    .getLogger(HttpBodyDescriptor.class)
                    .error(REQUEST_BODY_WRITING_EXCEPTION, e);
        }
    }
}
