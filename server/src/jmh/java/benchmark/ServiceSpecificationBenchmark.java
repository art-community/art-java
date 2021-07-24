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

package benchmark;

import io.art.server.specification.*;
import io.art.server.test.meta.*;
import io.art.server.test.meta.MetaServerTest.MetaIoPackage.MetaArtPackage.MetaServerPackage.MetaTestPackage.MetaServicePackage.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;
import reactor.core.publisher.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.server.factory.ServiceMethodSpecificationFactory.*;
import static io.art.server.module.ServerActivator.*;
import static java.util.concurrent.TimeUnit.*;
import static org.openjdk.jmh.annotations.Mode.*;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode(AverageTime)
@OutputTimeUnit(NANOSECONDS)
public class ServiceSpecificationBenchmark {
    @State(value = Scope.Benchmark)
    public static class BenchmarkState {
        MetaServerTest meta;
        MetaBenchmarkServiceClass serviceClass;
        ServiceMethodSpecification m1;
        ServiceMethodSpecification m2;
        ServiceMethodSpecification m3;
        ServiceMethodSpecification m4;
        ServiceMethodSpecification m5;
        ServiceMethodSpecification m6;
        ServiceMethodSpecification m7;
        ServiceMethodSpecification m8;
        ServiceMethodSpecification m9;
        ServiceMethodSpecification m10;
        ServiceMethodSpecification m11;
        ServiceMethodSpecification m12;
        ServiceMethodSpecification m13;
        ServiceMethodSpecification m14;
        ServiceMethodSpecification m15;
        ServiceMethodSpecification m16;

        @Setup
        public void setup() {
            initialize(meta(MetaServerTest::new), logging(), server());
            meta = library();
            serviceClass = meta.ioPackage().artPackage().serverPackage().testPackage().servicePackage().benchmarkServiceClass();
            m1 = forMethod(serviceClass, serviceClass.m1Method());
            m2 = forMethod(serviceClass, serviceClass.m2Method());
            m3 = forMethod(serviceClass, serviceClass.m3Method());
            m4 = forMethod(serviceClass, serviceClass.m4Method());
            m5 = forMethod(serviceClass, serviceClass.m5Method());
            m6 = forMethod(serviceClass, serviceClass.m6Method());
            m7 = forMethod(serviceClass, serviceClass.m7Method());
            m8 = forMethod(serviceClass, serviceClass.m8Method());
            m9 = forMethod(serviceClass, serviceClass.m9Method());
            m10 = forMethod(serviceClass, serviceClass.m10Method());
            m11 = forMethod(serviceClass, serviceClass.m11Method());
            m12 = forMethod(serviceClass, serviceClass.m12Method());
            m13 = forMethod(serviceClass, serviceClass.m13Method());
            m14 = forMethod(serviceClass, serviceClass.m14Method());
            m15 = forMethod(serviceClass, serviceClass.m15Method());
            m16 = forMethod(serviceClass, serviceClass.m16Method());
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
