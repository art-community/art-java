package ru.art.test.specification.core;

import lombok.*;
import static ru.art.test.specification.core.TestModule.TestModuleConfiguration.*;

@Getter
public class TestModuleOverrideConfiguration extends TestModuleFileConfiguration {
    private String value = OVERRIDE;
}