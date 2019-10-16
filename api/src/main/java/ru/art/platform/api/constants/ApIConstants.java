package ru.art.platform.api.constants;

import lombok.experimental.*;

@UtilityClass
public class ApIConstants {
    public static final String INITIALIZE_PROJECT = "initializeProject";
    public static final String BUILD_PROJECT = "buildProject";

    public enum ProjectState {
        NEW,
        INITIALIZED
    }
}
