package io.art.model.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import java.lang.annotation.*;

@Target(TYPE)
@Retention(SOURCE)
public @interface Module {
}
