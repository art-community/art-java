package ru.adk.core.extension;

import ru.adk.core.exception.InternalRuntimeException;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static ru.adk.core.context.Context.contextConfiguration;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public interface DateExtensions {
    static Date parse(String template, String dateString) {
        return parse(template, dateString, contextConfiguration().getLocale());
    }

    static Date parse(String template, String dateString, Locale locale) {
        try {
            return new SimpleDateFormat(template, locale).parse(dateString);
        } catch (ParseException e) {
            throw new InternalRuntimeException(e);
        }
    }

    static Date tryParse(String dateTimeString, SimpleDateFormat format) {
        return tryParse(dateTimeString, format, null);
    }

    static Date tryParse(String dateTimeString, SimpleDateFormat format, Date orElse) {
        try {
            return format.parse(dateTimeString);
        } catch (Exception e) {
            return orElse;
        }
    }

    static Optional<Date> parseSafety(String template, String dateString, Locale locale) {
        try {
            return of(new SimpleDateFormat(template, locale).parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return empty();
        }
    }


    static String format(String template, Date date) {
        return format(template, date, contextConfiguration().getLocale());
    }

    static String tryFormat(Date dateTime, SimpleDateFormat format, String orElse) {
        try {
            return format.format(dateTime);
        } catch (Exception e) {
            return orElse;
        }
    }

    static String tryFormat(Date dateTime, SimpleDateFormat format) {
        return tryFormat(dateTime, format, null);
    }

    static String format(String template, Date date, Locale locale) {
        return new SimpleDateFormat(template, locale).format(date);
    }
}
