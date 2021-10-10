package io.art.configurator.test;

import io.art.configurator.source.*;
import io.art.core.factory.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;

public class ConfigurationFormatsTest {
    @BeforeAll
    public static void setup() {
        initialize();
    }

    @AfterAll
    public static void cleanup() {
        shutdown();
    }


    @Test
    public void testEnvironment() {
    }

    @Test
    public void testProperties() {
        System.setProperty("key", "value");
        System.setProperty("array.0", "value-0");
        System.setProperty("array.1", "value-1");
        System.setProperty("nested.inner.value", "value");
        PropertiesConfigurationSource source = new PropertiesConfigurationSource("", MapFactory.immutableMapOf(System.getProperties()));
        System.out.println(source.getString("key"));
        System.out.println(source.getStringArray("array"));
        System.out.println(source.getNested("nested.inner.value").asString());
    }

    @Test
    public void testYaml() {

    }
}
