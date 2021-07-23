package io.art.configurator.test;

import io.art.configuration.yaml.source.*;
import io.art.core.initializer.*;
import io.art.core.source.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.FileConfigurationExtensions.*;
import static io.art.configurator.custom.CustomConfigurationParser.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.test.TestingMetaConfigurationGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.*;

public class CustomConfigurationParserTest {
    @BeforeAll
    public static void setup() {
        ContextInitializer.initialize(meta(MetaMetaTest::new).getFactory());
    }

    @Test
    public void testCustomConfigurationParsing() {
        URL resource = CustomConfigurationParserTest.class.getClassLoader().getResource(DEFAULT_MODULE_CONFIGURATION_FILE + DOT + YML_EXTENSION);
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
