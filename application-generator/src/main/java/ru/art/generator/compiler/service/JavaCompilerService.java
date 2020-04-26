package ru.art.generator.compiler.service;

import com.sun.tools.javac.processing.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import lombok.*;
import ru.art.generator.compiler.decorator.*;
import ru.art.generator.compiler.model.*;
import static com.sun.source.tree.Tree.Kind.*;
import static java.util.Arrays.*;
import static ru.art.generator.compiler.extensions.JavaCompilerExtensions.*;
import static ru.art.generator.compiler.service.MakerService.*;
import static ru.art.generator.compiler.state.CompilationState.*;
import javax.lang.model.element.*;
import java.lang.annotation.*;
import java.util.function.*;

@Getter
public class JavaCompilerService {
    public final JavacProcessingEnvironment processingEnvironment;
    public final JavacRoundEnvironment roundEnvironment;
    public final Context context;

    public JavaCompilerService(JavacProcessingEnvironment processingEnvironment, JavacRoundEnvironment roundEnvironment) {
        this.processingEnvironment = processingEnvironment;
        this.roundEnvironment = roundEnvironment;
        this.context = processingEnvironment.getContext();
        elements(processingEnvironment.getElementUtils());
        maker(TreeMaker.instance(context));
    }

    public JavaCompilerService forAnnotatedClasses(Class<? extends Annotation> annotation, BiConsumer<JCClassDecl, JavaCompilerService> action) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(annotation)) {
            JCTree elementTree = elements().getTree(element);
            if (elementTree.getKind() != CLASS) {
                continue;
            }
            action.accept((JCClassDecl) elementTree, this);
        }
        return this;
    }

    public JCLambda lambda(JCTree body, TypedParameter... parameters) {
        List<JCVariableDecl> lambdaParameters = fromStream(stream(parameters).map(parameter -> parameter(parameter.getParameter(), typeIdentifier(parameter.getType()))));
        return maker().Lambda(lambdaParameters, body);
    }


    public static JavaCompilerService javaCompilerService(JavacProcessingEnvironment processingEnvironment, JavacRoundEnvironment roundEnvironment) {
        return new JavaCompilerService(processingEnvironment, roundEnvironment);
    }
}
