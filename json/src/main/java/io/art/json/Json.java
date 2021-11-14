package io.art.json;

import io.art.core.annotation.*;
import io.art.json.descriptor.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.json.module.JsonModule.*;
import static lombok.AccessLevel.*;

@Public
@UtilityClass
public class Json {
    private final static Provider provider = new Provider();

    public static Provider json() {
        return provider;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Provider {
        public JsonReader reader() {
            return jsonModule().configuration().getReader();
        }

        public JsonWriter writer() {
            return jsonModule().configuration().getWriter();
        }
    }
}
