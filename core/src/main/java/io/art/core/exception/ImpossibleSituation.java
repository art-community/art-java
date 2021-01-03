package io.art.core.exception;

import static io.art.core.constants.ExceptionMessages.IMPOSSIBLE_MESSAGE;

public class ImpossibleSituation extends IllegalStateException {
    public ImpossibleSituation() {
        super(IMPOSSIBLE_MESSAGE);
    }
}
