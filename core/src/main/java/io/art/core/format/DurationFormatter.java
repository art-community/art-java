package io.art.core.format;

import lombok.experimental.*;
import static io.art.core.constants.DurationConstants.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Objects.*;
import static java.util.concurrent.TimeUnit.*;
import java.time.*;

@UtilityClass
public class DurationFormatter {
    public static String format(Duration duration) {
        if (isNull(duration)) return EMPTY_STRING;
        long nano = duration.toNanos();
        return (double) nano / (double) MILLISECONDS.toNanos(1) + MILLIS_LETTERS;
    }
}
