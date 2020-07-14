package io.art.sql.utils;

import org.jooq.*;
import static org.jooq.impl.DSL.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.extension.StringExtensions.*;

public class JooqResultExtractor {
    private static String getString(Result<?> sqlResult, int index, String alias, String fieldName) {
        return emptyIfNull(sqlResult.getValue(index, (field(name(quotedName(alias), quotedName(fieldName))))));
    }

    private static Long getLong(Result<?> sqlResult, int index, String alias, String fieldName) {
        String sqlLongValue = emptyIfNull(sqlResult.getValue(index, (field(name(quotedName(alias), quotedName(fieldName))))));
        return isEmpty(sqlLongValue) ? null : Long.valueOf(sqlLongValue);
    }

    private static Integer getInteger(Result<?> sqlResult, int index, String alias, String fieldName) {
        String sqlIntegerValue = emptyIfNull(sqlResult.getValue(index, (field(name(quotedName(alias), quotedName(fieldName))))));
        return isEmpty(sqlIntegerValue) ? null : Integer.valueOf(sqlIntegerValue);
    }
}
