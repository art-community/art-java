package io.art.meta.test;

import io.art.configuration.yaml.source.*;
import io.art.core.initializer.*;
import io.art.core.source.*;
import io.art.meta.module.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.meta.parser.MetaConfigurationSourceParser.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.*;

public class MetaConfigurationSourceParserTest {
    @BeforeAll
    public static void initialize() {
        ContextInitializer.initialize(() -> new MetaModule(new MetaMetaTest()));
    }

    @Test
    public void testMetaConfigurationParse() {
        URL resource = MetaConfigurationSourceParserTest.class.getClassLoader().getResource("module.yml");
        assertNotNull(resource);
        TestingMetaConfigurationModel model = parse(TestingMetaConfigurationModel.class, new YamlConfigurationSource(ConfigurationSourceParameters.builder()
                .inputStream(() -> wrapExceptionCall(resource::openStream))
                .path(resource.getPath())
                .section(EMPTY_STRING)
                .build()));
        assertEquals(model.f1, 1);
    }
}
