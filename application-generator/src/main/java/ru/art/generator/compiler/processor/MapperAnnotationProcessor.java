package ru.art.generator.compiler.processor;

import com.google.auto.service.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.processing.*;
import com.sun.tools.javac.tree.JCTree.*;
import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.generator.compiler.annotation.*;
import ru.art.generator.compiler.declaration.*;
import ru.art.generator.compiler.decorator.*;
import ru.art.generator.compiler.model.*;
import ru.art.generator.compiler.service.*;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.util.List.*;
import static javax.lang.model.SourceVersion.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.generator.compiler.decorator.ClassDecorator.*;
import static ru.art.generator.compiler.extensions.JavaCompilerExtensions.*;
import static ru.art.generator.compiler.service.JavaCompilerService.*;
import static ru.art.generator.compiler.state.CompilationState.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import java.util.*;

@AutoService(Processor.class)
public class MapperAnnotationProcessor extends AbstractProcessor {
    private JavacProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = (JavacProcessingEnvironment) processingEnvironment;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return setOf(Model.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) {
            return false;
        }
        javaCompilerService(processingEnvironment, (JavacRoundEnvironment) roundEnvironment).forAnnotatedClasses(Model.class, MapperAnnotationProcessor::generateModelFields);
        return false;
    }

    private static void generateModelFields(JCClassDecl modelClass, JavaCompilerService service) {
        Map<String, TypedParameter> fields = getFields(modelClass);
        ClassDecorator decorator = decorator(modelClass);
        decorator.field(FieldDeclaration.builder()
                .name("toModel")
                .flags(PRIVATE | FINAL | STATIC)
                .type(ValueToModelMapper.class)
                .typeParameter(decorator.getCurrentClass())
                .typeParameter(Value.class)
                .initializer(LambdaDeclaration.builder()
                        .parameter(TypedParameter.builder().type(Value.class).parameter("value").build())
                        .body(maker().Block(0L, of(maker().Return(maker().Literal(TypeTag.BOT, null)))))
                        .build()
                        .make())
                .build());

        decorator.decorate();
    }
}
