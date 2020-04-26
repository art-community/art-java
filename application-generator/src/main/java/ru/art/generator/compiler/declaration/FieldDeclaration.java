package ru.art.generator.compiler.declaration;

import com.sun.tools.javac.tree.JCTree.*;
import lombok.*;
import java.util.*;

@Getter
@Builder
public class FieldDeclaration {
    @Builder.Default
    private final Integer flags = 0;
    private final String name;
    private final Class<?> type;
    @Singular("typeParameter")
    private final List<Class<?>> typeParameters;
    private final JCExpression initializer;
}
