package ru.art.service.exception;

import ru.art.service.constants.ServiceExceptionsMessages;
import static java.text.MessageFormat.format;

public class UnknownServiceMethodException extends RuntimeException {
    public UnknownServiceMethodException(String serviceId, String methodId) {
        super(format(ServiceExceptionsMessages.UNKNOWN_SERVICE_METHOD_MESSAGE, serviceId, methodId));
    }
}
