package io.art.core.exception;

import static io.art.core.constants.ExceptionMessages.IMPOSSIBLE_MESSAGE;

public class ImpossibleSituationException extends IllegalStateException {
    public ImpossibleSituationException() {
        super(IMPOSSIBLE_MESSAGE);
    }
}
