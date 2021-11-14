package io.art.fibers;

import org.graalvm.nativeimage.c.*;
import org.graalvm.nativeimage.c.function.*;
import java.util.*;

@CContext(Koishi.Directives.class)
public class Koishi {
    public static final class Directives implements CContext.Directives {
        @Override
        public List<String> getHeaderFiles() {
            return Collections.singletonList("koishi.h");
        }

        @Override
        public List<String> getLibraries() {
            return Collections.singletonList("koishi");
        }
    }

    @CFunction("koishi_util_page_size")
    public static native int koishi_util_page_size();

    public static void main(String[] args) {
        System.out.println(Koishi.koishi_util_page_size());
    }
}
