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

import io.art.logging.module.*;
import io.art.server.module.*;
import io.art.server.specification.*;
import io.art.server.test.meta.*;
import io.art.server.test.meta.MetaServerTest.MetaIoPackage.MetaArtPackage.MetaServerPackage.MetaTestPackage.MetaServicePackage.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;
import reactor.core.publisher.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.server.factory.ServiceMethodSpecificationFactory.*;
import static java.util.concurrent.TimeUnit.*;
import static org.openjdk.jmh.annotations.Mode.*;

public class JavaBenchmark {
    @State(value = Scope.Benchmark)
    public static class This {
        MetaServerTest meta;
        MetaTestServiceClass serviceClass;
        ServiceMethodSpecification specification;

        @Setup
        public void setup() {
            initialize(meta(MetaServerTest::new), LoggingActivator.logging(), ServerActivator.server());
            meta = library();
            serviceClass = meta.ioPackage().artPackage().serverPackage().testPackage().servicePackage().testServiceClass();
            specification = forMethod(serviceClass, serviceClass.m15Method());
        }
    }

    @Benchmark
    @BenchmarkMode(Throughput)
    @OutputTimeUnit(MILLISECONDS)
    @Warmup(iterations = 1)
    public void defaultBenchmark(Blackhole blackhole, This state) throws Throwable {
        blackhole.consume(state.specification.serve(Flux.just("test")));
        //blackhole.consume(state.serviceClass.m1Method().invokeCatched());
    }
}
