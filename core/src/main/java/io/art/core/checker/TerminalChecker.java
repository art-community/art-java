package io.art.core.checker;

import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.SystemConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.determiner.SystemDeterminer.*;

@UtilityClass
public class TerminalChecker {
    public static boolean terminalSupportColors() {
        ImmutableMap<String, String> environment = context().configuration().getEnvironment();
        if (isWindows()) {
            return WINDOWS_TERMINAL_ENVIRONMENT
                    .stream()
                    .anyMatch(environment::containsKey);
        }

        return let(environment.get(TERM_VARIABLE), value -> value.contains(XTERM_PATTERN), false);
    }
}
