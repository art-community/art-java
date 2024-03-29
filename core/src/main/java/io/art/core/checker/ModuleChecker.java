package io.art.core.checker;

import lombok.experimental.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;

@UtilityClass
public class ModuleChecker {
    public static boolean withLogging() {
        return context().hasModule(LOGGING_MODULE_ID);
    }

    public static boolean withConfigurator() {
        return context().hasModule(CONFIGURATOR_MODULE_ID);
    }

    public static boolean withMeta() {
        return context().hasModule(META_MODULE_ID);
    }

    public static boolean withTransport() {
        return context().hasModule(TRANSPORT_MODULE_ID);
    }

    public static boolean withJson() {
        return context().hasModule(JSON_MODULE_ID);
    }

    public static boolean withYaml() {
        return context().hasModule(YAML_MODULE_ID);
    }

    public static boolean withMessagePack() {
        return context().hasModule(MESSAGE_PACK_MODULE_ID);
    }

    public static void withLogging(Runnable action) {
        if (withLogging()) action.run();
    }

    public static void withConfigurator(Runnable action) {
        if (withConfigurator()) action.run();
    }

    public static void withMeta(Runnable action) {
        if (withMeta()) action.run();
    }

    public static void withTransport(Runnable action) {
        if (withTransport()) action.run();
    }

    public static void withJson(Runnable action) {
        if (withJson()) action.run();
    }

    public static void withYaml(Runnable action) {
        if (withYaml()) action.run();
    }

    public static void withMessagePack(Runnable action) {
        if (withMessagePack()) action.run();
    }
}
