package ru.art.generator.compiler.processor;

import com.google.auto.service.*;
import com.sun.tools.javac.model.*;
import com.sun.tools.javac.processing.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import ru.art.generator.compiler.annotation.*;
import static com.sun.source.tree.Tree.Kind.*;
import static com.sun.tools.javac.code.Flags.*;
import static javax.lang.model.SourceVersion.*;
import static ru.art.core.factory.CollectionsFactory.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import java.util.*;

@AutoService(Processor.class)
public class MapperProcessor extends AbstractProcessor {
    private JavacProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = (JavacProcessingEnvironment) processingEnvironment;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return setOf(Mapper.class.getName());
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
        Context context = processingEnvironment.getContext();
        TreeMaker maker = TreeMaker.instance(context);
        JavacElements elementUtils = (JavacElements) processingEnv.getElementUtils();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Mapper.class)) {
            JCTree elementTree = elementUtils.getTree(element);
            if (elementTree.getKind() != CLASS) {
                continue;
            }
            maker.at(elementTree.pos);
            JCTree.JCClassDecl modelClass = (JCTree.JCClassDecl) elementTree;
            ListBuffer<JCTree> modelClassDefinitions = new ListBuffer<>();
            modelClassDefinitions.addAll(modelClass.defs);
            modelClassDefinitions.append(maker.VarDef(
                    maker.Modifiers(FINAL), elementUtils.getName("test"),
                    maker.Ident(elementUtils.getName(String.class.getSimpleName())),
                    maker.Literal("Test")
            ));
            modelClass.defs = modelClassDefinitions.toList();
        }
        return false;
    }
}
