package ru.art.service.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.art.service.model.ServiceMethodCommand;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.StringConstants.DOT;
import static ru.art.core.constants.StringConstants.NEW_LINE;
import static ru.art.service.constants.ServiceExceptionsMessages.SERVICE_EXECUTION_EXCEPTION_MESSAGE;
import static ru.art.service.constants.ServiceExceptionsMessages.SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ServiceExecutionException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public ServiceExecutionException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ServiceExecutionException(ServiceMethodCommand command, String errorCode, String errorMessage) {
        super(format(SERVICE_EXECUTION_EXCEPTION_MESSAGE, command.getServiceId(), command.getMethodId(), errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ServiceExecutionException(ServiceMethodCommand command, String errorCode, Throwable e) {
        super(formatErrorMessage(command, errorCode, e), e);
        this.errorCode = errorCode;
        this.errorMessage = e.getMessage();
    }

    private static String formatErrorMessage(ServiceMethodCommand command, String errorCode, Throwable e) {
        return format(SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE, command.getServiceId(), command.getMethodId(),
                errorCode, e.getMessage(), getStackTrace(e));
    }

    private static String getStackTrace(Throwable e) {
        StringBuilder errorMessage = new StringBuilder();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            errorMessage.append(stackTraceElement.getClassName()).append(DOT)
                    .append(stackTraceElement.getMethodName()).append(DOT)
                    .append(stackTraceElement.getLineNumber()).append(NEW_LINE);
        }
        return errorMessage.toString();
    }
}
