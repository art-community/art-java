package ru.art.network.manager.exception;

import ru.art.network.manager.constants.NetworkManagerModuleConstants.BalancerMode;
import static java.text.MessageFormat.format;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.ExceptionMessages.UNKNOWN_BALANCER_MODE_MESSAGE;

public class UnknownBalancerModeException extends RuntimeException {
    public UnknownBalancerModeException(BalancerMode mode) {
        super(format(UNKNOWN_BALANCER_MODE_MESSAGE, mode));
    }
}
