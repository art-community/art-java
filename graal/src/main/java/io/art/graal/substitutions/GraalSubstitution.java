package io.art.graal.substitutions;

import com.oracle.svm.core.annotate.*;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.script.*;
import org.apache.logging.log4j.core.util.*;
import static com.oracle.svm.core.annotate.TargetElement.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.graal.constants.GraalConstants.*;

@SuppressWarnings(UNUSED)
@TargetClass(ScriptManager.class)
final class Log4jScriptManagerSubstitution {
    @Substitute
    @TargetElement(name = CONSTRUCTOR_NAME)
    public final void init(final Configuration configuration, final WatchManager watchManager) {
        throw new LinkageError(LOG4j_SCRIPT_MANAGER_MESSAGE);
    }
}

@SuppressWarnings(UNUSED)
final class GraalSubstitution {
}
