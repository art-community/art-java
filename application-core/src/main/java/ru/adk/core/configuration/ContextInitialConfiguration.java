package ru.adk.core.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.adk.core.context.Context;
import ru.adk.core.network.provider.IpAddressProvider;
import ru.adk.core.provider.PreconfiguredModuleProvider;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.Category.FORMAT;
import static java.util.Locale.getDefault;
import static java.util.Optional.ofNullable;
import static ru.adk.core.constants.ContextConstants.DEFAULT_MAIN_MODULE_ID;
import static ru.adk.core.constants.ContextConstants.DEFAULT_MODULE_JAR;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Locale;

public interface ContextInitialConfiguration {
    String getMainModuleId();

    String getModuleJarName();

    Charset getCharset();

    boolean isUnloadModulesOnShutdown();

    String getIpAddress();

    Locale getLocale();

    PreconfiguredModuleProvider getPreconfiguredModulesProvider();

    @Getter
    @NoArgsConstructor
    class ContextInitialDefaultConfiguration implements ContextInitialConfiguration {
        private final Charset charset = UTF_8;
        private final boolean unloadModulesOnShutdown = true;
        private final String mainModuleId = DEFAULT_MAIN_MODULE_ID;
        private final String moduleJarName = DEFAULT_MODULE_JAR;
        private final String ipAddress = IpAddressProvider.getIpAddress();
        private final Locale locale = getDefault(FORMAT);
        private PreconfiguredModuleProvider preconfiguredModulesProvider;
    }

    @Getter
    @RequiredArgsConstructor
    class ApplicationContextConfiguration implements ContextInitialConfiguration {
        private final String mainModuleId;
        private final Charset charset = UTF_8;
        private final boolean unloadModulesOnShutdown = true;
        private final String ipAddress = IpAddressProvider.getIpAddress();
        private final Locale locale = getDefault(FORMAT);
        private final String moduleJarName = ofNullable(Context.class.getProtectionDomain())
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(URL::getPath)
                .map(File::new)
                .map(File::getPath)
                .orElse(DEFAULT_MODULE_JAR);
        private PreconfiguredModuleProvider preconfiguredModulesProvider;
        public ApplicationContextConfiguration(String applicationModuleId, PreconfiguredModuleProvider preconfiguredModulesProvider) {
            this.mainModuleId = applicationModuleId;
            this.preconfiguredModulesProvider = preconfiguredModulesProvider;
        }
    }
}