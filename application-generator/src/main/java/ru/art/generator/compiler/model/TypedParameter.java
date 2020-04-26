package ru.art.generator.compiler.model;

import lombok.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;

@Getter
@Builder
public class TypedParameter {
    private final String packageName;
    private final String typeName;
    private final String parameter;

    public String getFullTypeName() {
        return isEmpty(packageName) ? typeName : packageName + DOT + typeName;
    }
}
