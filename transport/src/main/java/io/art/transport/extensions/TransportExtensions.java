package io.art.transport.extensions;

import io.art.core.annotation.*;
import io.art.json.*;
import io.art.yaml.*;
import lombok.experimental.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.meta.Meta.*;
import static io.art.meta.model.TypedObject.*;

@Public
@UtilityClass
public class TransportExtensions {
    public static String toPrettyString(Object data) {
        return withMeta() && findDeclaration(data.getClass()).isPresent()
                ? withYaml() ? Yaml.yaml().writer().writeToString(typed(declaration(data.getClass()).definition(), data))
                : withJson() ? Json.json().writer().writeToString(typed(declaration(data.getClass()).definition(), data))
                : data.toString() : data.toString();
    }
}
