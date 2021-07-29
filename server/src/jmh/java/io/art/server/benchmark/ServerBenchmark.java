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

package io.art.server.benchmark;

import io.art.server.method.*;
import io.art.server.test.meta.*;
import io.art.server.test.meta.MetaServerTest.MetaIoPackage.MetaArtPackage.MetaServerPackage.MetaTestPackage.MetaServicePackage.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;
import reactor.core.publisher.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.server.factory.ServiceMethodFactory.*;
import static java.util.concurrent.TimeUnit.*;
import static org.openjdk.jmh.annotations.Mode.*;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode(AverageTime)
@OutputTimeUnit(NANOSECONDS)
public class ServerBenchmark {
    @State(value = Scope.Benchmark)
    public static class BenchmarkState {
        MetaServerTest meta;
        MetaBenchmarkServiceClass serviceClass;
        ServiceMethod m1;
        ServiceMethod m2;
        ServiceMethod m3;
        ServiceMethod m4;
        ServiceMethod m5;
        ServiceMethod m6;
        ServiceMethod m7;
        ServiceMethod m8;
        ServiceMethod m9;
        ServiceMethod m10;
        ServiceMethod m11;
        ServiceMethod m12;
        ServiceMethod m13;
        ServiceMethod m14;
        ServiceMethod m15;
        ServiceMethod m16;

        @Setup
        public void setup() {
            initialize(meta(MetaServerTest::new));
            meta = library();
            serviceClass = meta.ioPackage().artPackage().serverPackage().testPackage().servicePackage().benchmarkServiceClass();
            m1 = preconfiguredServiceMethod(serviceClass, serviceClass.m1Method());
            m2 = preconfiguredServiceMethod(serviceClass, serviceClass.m2Method());
            m3 = preconfiguredServiceMethod(serviceClass, serviceClass.m3Method());
            m4 = preconfiguredServiceMethod(serviceClass, serviceClass.m4Method());
            m5 = preconfiguredServiceMethod(serviceClass, serviceClass.m5Method());
            m6 = preconfiguredServiceMethod(serviceClass, serviceClass.m6Method());
            m7 = preconfiguredServiceMethod(serviceClass, serviceClass.m7Method());
            m8 = preconfiguredServiceMethod(serviceClass, serviceClass.m8Method());
            m9 = preconfiguredServiceMethod(serviceClass, serviceClass.m9Method());
            m10 = preconfiguredServiceMethod(serviceClass, serviceClass.m10Method());
            m11 = preconfiguredServiceMethod(serviceClass, serviceClass.m11Method());
            m12 = preconfiguredServiceMethod(serviceClass, serviceClass.m12Method());
            m13 = preconfiguredServiceMethod(serviceClass, serviceClass.m13Method());
            m14 = preconfiguredServiceMethod(serviceClass, serviceClass.m14Method());
            m15 = preconfiguredServiceMethod(serviceClass, serviceClass.m15Method());
            m16 = preconfiguredServiceMethod(serviceClass, serviceClass.m16Method());
        }
    }

    @Benchmark
    public void a(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m1.serve(Flux.just("test")));
    }

    @Benchmark
    public void b(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m2.serve(Flux.just("test")));
    }

    @Benchmark
    public void c(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m3.serve(Flux.just("test")));
    }

    @Benchmark
    public void d(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m4.serve(Flux.just("test")));
    }

    @Benchmark
    public void e(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m5.serve(Flux.just("test")));
    }

    @Benchmark
    public void f(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m6.serve(Flux.just("test")));
    }

    @Benchmark
    public void g(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m7.serve(Flux.just("test")));
    }

    @Benchmark
    public void h(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m8.serve(Flux.just("test")));
    }

    @Benchmark
    public void i(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m9.serve(Flux.just("test")));
    }

    @Benchmark
    public void j(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m10.serve(Flux.just("test")));
    }

    @Benchmark
    public void k(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m11.serve(Flux.just("test")));
    }

    @Benchmark
    public void l(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m12.serve(Flux.just("test")));
    }

    @Benchmark
    public void m(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m13.serve(Flux.just("test")));
    }

    @Benchmark
    public void n(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m14.serve(Flux.just("test")));
    }

    @Benchmark
    public void o(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m15.serve(Flux.just("test")));
    }

    @Benchmark
    public void p(Blackhole blackhole, BenchmarkState state) {
        blackhole.consume(state.m16.serve(Flux.just("test")));
    }
}
