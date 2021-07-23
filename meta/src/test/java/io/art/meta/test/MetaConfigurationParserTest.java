package io.art.meta.test;

import io.art.configuration.yaml.source.*;
import io.art.core.initializer.*;
import io.art.core.source.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.parser.MetaConfigurationParser.*;
import static io.art.meta.test.TestingMetaConfigurationGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.*;

public class MetaConfigurationParserTest {
    @BeforeAll
    public static void setup() {
        ContextInitializer.initialize(meta(MetaMetaTest::new).getFactory());
    }

    @Test
    public void testMetaConfigurationParse() {
        URL resource = MetaConfigurationParserTest.class.getClassLoader().getResource("module.yml");
        assertNotNull(resource);
        ConfigurationSourceParameters parameters = ConfigurationSourceParameters.builder()
                .inputStream(() -> wrapExceptionCall(resource::openStream))
                .path(resource.getPath())
                .section(EMPTY_STRING)
                .build();
        TestingMetaConfiguration model = parse(TestingMetaConfiguration.class, new YamlConfigurationSource(parameters));
        model.assertEquals(generateTestingConfiguration());
    }
}
