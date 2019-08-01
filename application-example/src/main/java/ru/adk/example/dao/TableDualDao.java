package ru.adk.example.dao;

import org.jooq.Result;
import static org.jooq.impl.DSL.using;
import static ru.adk.core.extension.StringExtensions.emptyIfNull;
import static ru.adk.example.constants.ExampleAppModuleConstants.SqlConstants.ONE;
import static ru.adk.example.constants.ExampleAppModuleConstants.SqlConstants.TABLE_DUAL;
import static ru.adk.sql.module.SqlModule.sqlModule;

/**
 * Dao is made for exchanging data with database
 */
public class TableDualDao {

    public static String testQuery() {
        Result testQueryValue = using(sqlModule().getJooqConfiguration())
                .select(ONE)
                .from(TABLE_DUAL)
                .maxRows(1)
                .fetch();

        return emptyIfNull(testQueryValue.getValue(0, ONE));
    }
}
