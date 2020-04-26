package ru.art.generator.compiler.declaration;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import lombok.*;
import ru.art.generator.compiler.model.*;
import static ru.art.generator.compiler.extensions.JavaCompilerExtensions.*;
import static ru.art.generator.compiler.service.MakerService.*;
import static ru.art.generator.compiler.state.CompilationState.*;
import java.util.*;

@Getter
@Builder
public class LambdaDeclaration {
    private final JCTree body;
    @Singular("parameter")
    private final List<TypedParameter> parameters;

    public JCLambda make() {
        com.sun.tools.javac.util.List<JCVariableDecl> lambdaParameters = fromStream(parameters
                .stream()
                .map(parameter -> parameter(parameter.getParameter(), identifier(parameter.getTypeName()))));
        return maker().Lambda(lambdaParameters, body);

    }
}
