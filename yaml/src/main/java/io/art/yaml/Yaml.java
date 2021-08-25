package io.art.yaml;

import io.art.core.annotation.*;
import io.art.yaml.descriptor.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.yaml.module.YamlModule.*;
import static lombok.AccessLevel.*;

@Public
@UtilityClass
public class Yaml {
    private final static Provider provider = new Provider();

    public static Provider yaml() {
        return provider;
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Provider {
        public YamlReader reader() {
            return yamlModule().configuration().getReader();
        }

        public YamlWriter writer() {
            return yamlModule().configuration().getWriter();
        }
    }

}
