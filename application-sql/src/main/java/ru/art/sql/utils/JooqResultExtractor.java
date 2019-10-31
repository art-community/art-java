package ru.art.sql.utils;

import org.jooq.Result;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.quotedName;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.core.extension.StringExtensions.emptyIfNull;

public class JooqResultExtractor {
    private static String getString(Result sqlResult, int index, String alias, String fieldName) {
        String sqlStringValue = emptyIfNull(sqlResult.getValue(index, (field(name(quotedName(alias), quotedName(fieldName))))));
        return getOrElse(sqlStringValue, null);
    }

    private static Long getLong(Result sqlResult, int index, String alias, String fieldName) {
        String sqlLongValue = emptyIfNull(sqlResult.getValue(index, (field(name(quotedName(alias), quotedName(fieldName))))));
        return isEmpty(sqlLongValue) ? null : Long.valueOf(sqlLongValue);
    }

    private static Integer getInteger(Result sqlResult, int index, String alias, String fieldName) {
        String sqlIntegerValue = emptyIfNull(sqlResult.getValue(index, (field(name(quotedName(alias), quotedName(fieldName))))));
        return isEmpty(sqlIntegerValue) ? null : Integer.valueOf(sqlIntegerValue);
    }
}
