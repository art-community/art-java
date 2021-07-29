/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.communicator.benchmark;

import io.art.communicator.action.*;
import io.art.communicator.test.meta.*;
import io.art.communicator.test.meta.MetaCommunicatorTest.MetaIoPackage.MetaArtPackage.MetaCommunicatorPackage.MetaTestPackage.MetaProxyPackage.*;
import io.art.communicator.test.proxy.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;
import reactor.core.publisher.*;
import static io.art.communicator.factory.CommunicatorActionFactory.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.concurrent.TimeUnit.*;
import static org.openjdk.jmh.annotations.Mode.*;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode(AverageTime)
@OutputTimeUnit(NANOSECONDS)
public class CommunicatorBenchmark {
    @State(value = Scope.Benchmark)
    public static class BenchmarkState {
        MetaCommunicatorTest meta;
        MetaTestCommunicatorClass communicatorClass;
        TestCommunicator communicator;

        @Setup
        public void setup() {
            initialize(meta(MetaCommunicatorTest::new));
            meta = library();
            communicatorClass = cast(meta.classes().get(TestCommunicator.class));
            communicator = communicatorProxy(communicatorClass, method -> apply(preconfiguredCommunicatorAction(communicatorClass, method, new BenchmarkCommunication()), CommunicatorAction::initialize));
        }
    }

    @Benchmark
    public void a(Blackhole blackhole, BenchmarkState state) {
        state.communicator.m1();
    }

    @Benchmark
    public void b(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m2());
    }

    @Benchmark
    public void c(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m3());
    }

    @Benchmark
    public void d(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m4());
    }

    @Benchmark
    public void e(Blackhole blackhole, BenchmarkState state) {
        state.communicator.m5("test");
    }

    @Benchmark
    public void f(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m6("test"));
    }

    @Benchmark
    public void g(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m7("test"));
    }

    @Benchmark
    public void h(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m8("test"));
    }

    @Benchmark
    public void i(Blackhole blackhole, BenchmarkState state) {
        state.communicator.m9(Mono.just("test"));
    }

    @Benchmark
    public void j(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m10(Mono.just("test")));
    }

    @Benchmark
    public void k(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m11(Mono.just("test")));
    }

    @Benchmark
    public void l(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m12(Mono.just("test")));
    }

    @Benchmark
    public void m(Blackhole blackhole, BenchmarkState state) {
        state.communicator.m13(Flux.just("test"));
    }

    @Benchmark
    public void n(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m14(Flux.just("test")));
    }

    @Benchmark
    public void o(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m15(Flux.just("test")));
    }

    @Benchmark
    public void p(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.communicator.m16(Flux.just("test")));
    }
}
