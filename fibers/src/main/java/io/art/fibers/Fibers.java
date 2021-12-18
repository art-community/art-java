package io.art.fibers;

import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.word.*;
import static io.art.fibers.Koishi.*;

public class Fibers {
    public static void main(String[] args) {
        System.out.println(koishi_util_page_size());

        koishi_coroutine_t co = koishi_create();

        System.out.println(co.rawValue());

        koishi_init(co, 1024 * 1024 * 1024, runFiber.getFunctionPointer(), CurrentIsolate.getCurrentThread());

        System.out.println("Here we are!");

        koishi_resume(co, ObjectHandles.getGlobal().create(new Fiber()));

        System.out.println("Whooaaaa 1!!!");

        koishi_resume(co, ObjectHandles.getGlobal().create(new Fiber()));

        System.out.println("Whooaaaa 2!!!");

        koishi_destroy(co);
    }

    @CEntryPoint
    public static void runFiber(IsolateThread thread, ObjectHandle data) {
        koishi_dump(1);
        koishi_yield(WordFactory.nullPointer());
        Fiber f = ObjectHandles.getGlobal().get(data);
        f.run();
        koishi_dump(2);
    }


    public static class Fiber {
        public void run() {
            System.out.println("run");
        }
    }

}
