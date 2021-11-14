package io.art.configurator.test;

import io.art.configurator.source.*;
import io.art.core.builder.*;
import io.art.core.configuration.*;
import io.art.core.source.*;
import org.junit.jupiter.api.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.initializer.Initializer.*;
import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ConfigurationFormatsTest {
    @Test
    public void testEnvironment() {
        Map<String, String> environment = MapBuilder.mapBuilder("KEY", "value")
                .with("ARRAY_0", "value-0")
                .with("ARRAY_1", "value-1")
                .with("ARRAY_2_TEST", "value-2")
                .with("ARRAY_3_TEST_0", "value-3")
                .with("NESTED_INNER_VALUE", "value")
                .with("NESTED_INNER_VALUE_ARRAY_0", "value")
                .withAll(System.getenv())
                .build();

        initialize(ContextConfiguration.builder()
                .environment(immutableMapOf(environment))
                .build());
        EnvironmentConfigurationSource source = new EnvironmentConfigurationSource(EMPTY_STRING);
        assertEquals("value", source.getString("KEY"));
        assertEquals("value-0", source.getNested("ARRAY").asArray().get(0).asString());
        assertEquals("value-1", source.getNested("ARRAY").asArray().get(1).asString());
        assertEquals("value-2", source.getNested("ARRAY").asArray().get(2).getString("TEST"));
        assertEquals("value-3", source.getNested("ARRAY").asArray().get(3).getStringArray("TEST").get(0));
        assertEquals("value", source.getNested("NESTED_INNER").getString("VALUE"));
        assertEquals("value", source.getNested("NESTED_INNER").getNested("VALUE").getStringArray("ARRAY").get(0));
        shutdown();
    }

    @Test
    public void testProperties() {
        initialize();
        setProperty("key", "value");
        setProperty("array.0", "value-0");
        setProperty("array.1", "value-1");
        setProperty("array.2.test", "value-2");
        setProperty("array.3.test.0", "value-3");
        setProperty("nested.inner.value", "value");
        setProperty("nested.inner.value.array.0", "value");
        PropertiesConfigurationSource source = new PropertiesConfigurationSource(EMPTY_STRING, immutableMapOf(getProperties()));
        assertEquals("value", source.getString("key"));
        assertEquals("value-0", source.getNested("array").asArray().get(0).asString());
        assertEquals("value-1", source.getNested("array").asArray().get(1).asString());
        assertEquals("value-2", source.getNested("array").asArray().get(2).getString("test"));
        assertEquals("value-3", source.getNested("array").asArray().get(3).getStringArray("test").get(0));
        assertEquals("value", source.getNested("nested.inner").getString("value"));
        assertEquals("value", source.getNested("nested.inner").getNested("value").getStringArray("array").get(0));
        shutdown();
    }

    @Test
    public void testYaml() {
        initialize();
        ConfigurationSourceParameters parameters = ConfigurationSourceParameters.builder()
                .type(RESOURCES_FILE)
                .inputStream(() -> ConfigurationFormatsTest.class.getClassLoader().getResourceAsStream("test.yml"))
                .build();
        YamlConfigurationSource source = new YamlConfigurationSource(parameters);
        assertEquals("value", source.getString("key"));
        assertEquals("value-0", source.getNested("array").asArray().get(0).asString());
        assertEquals("value-1", source.getNested("array").asArray().get(1).asString());
        assertEquals("value-2", source.getNested("array").asArray().get(2).getString("test"));
        assertEquals("value", source.getNested("nested.inner").getString("value"));
        shutdown();
    }
}
