package io.art.model.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.*;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Modeler {
}
