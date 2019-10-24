package ru.art.information.module;

import lombok.*;
import ru.art.core.module.*;
import ru.art.information.configuration.*;
import static ru.art.core.context.Context.*;
import static ru.art.information.configuration.InformationModuleConfiguration.*;
import static ru.art.information.constants.InformationModuleConstants.*;

@Getter
public class InformationModule implements Module<InformationModuleConfiguration, ModuleState> {
    private final String id = INFORMATION_MODULE_ID;
    private final InformationModuleConfiguration defaultConfiguration = new InformationModuleDefaultConfiguration();
    @Getter(lazy = true)
    private final static InformationModuleConfiguration informationModule = context().getModule(INFORMATION_MODULE_ID, InformationModule::new);

    public static InformationModuleConfiguration informationModule() {
        return getInformationModule();
    }
}
