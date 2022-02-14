package io.art.rsocket.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.core.property.LazyProperty;
import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaMethod;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.MetaProxy;
import io.art.meta.model.StaticMetaMethod;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaRsocketTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaRsocketTest(MetaLibrary... dependencies) {
    super(dependencies);
  }

  public MetaIoPackage ioPackage() {
    return ioPackage;
  }

  public static final class MetaIoPackage extends MetaPackage {
    private final MetaArtPackage artPackage = register(new MetaArtPackage());

    private MetaIoPackage() {
      super("io");
    }

    public MetaArtPackage artPackage() {
      return artPackage;
    }

    public static final class MetaArtPackage extends MetaPackage {
      private final MetaRsocketPackage rsocketPackage = register(new MetaRsocketPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaRsocketPackage rsocketPackage() {
        return rsocketPackage;
      }

      public static final class MetaRsocketPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaRsocketPackage() {
          super("rsocket");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaRsocketDefaultTestClass rsocketDefaultTestClass = register(new MetaRsocketDefaultTestClass());

          private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaRsocketDefaultTestClass rsocketDefaultTestClass() {
            return rsocketDefaultTestClass;
          }

          public MetaCommunicatorPackage communicatorPackage() {
            return communicatorPackage;
          }

          public MetaServicePackage servicePackage() {
            return servicePackage;
          }

          public static final class MetaRsocketDefaultTestClass extends MetaClass<io.art.rsocket.test.RsocketDefaultTest> {
            private static final LazyProperty<MetaRsocketDefaultTestClass> self = MetaClass.self(io.art.rsocket.test.RsocketDefaultTest.class);

            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod(this));

            private final MetaCleanupMethod cleanupMethod = register(new MetaCleanupMethod(this));

            private final MetaTestDefaultRsocketMethod testDefaultRsocketMethod = register(new MetaTestDefaultRsocketMethod(this));

            private MetaRsocketDefaultTestClass() {
              super(metaType(io.art.rsocket.test.RsocketDefaultTest.class));
            }

            public static MetaRsocketDefaultTestClass rsocketDefaultTest() {
              return self.get();
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaSetupMethod setupMethod() {
              return setupMethod;
            }

            public MetaCleanupMethod cleanupMethod() {
              return cleanupMethod;
            }

            public MetaTestDefaultRsocketMethod testDefaultRsocketMethod() {
              return testDefaultRsocketMethod;
            }

            public final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.test.RsocketDefaultTest> {
              private MetaConstructorConstructor(MetaClass owner) {
                super(metaType(io.art.rsocket.test.RsocketDefaultTest.class),owner);
              }

              @Override
              public io.art.rsocket.test.RsocketDefaultTest invoke(Object[] arguments) throws
                  Throwable {
                return new io.art.rsocket.test.RsocketDefaultTest();
              }

              @Override
              public io.art.rsocket.test.RsocketDefaultTest invoke() throws Throwable {
                return new io.art.rsocket.test.RsocketDefaultTest();
              }
            }

            public final class MetaSetupMethod extends StaticMetaMethod<Void> {
              private MetaSetupMethod(MetaClass owner) {
                super("setup",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.rsocket.test.RsocketDefaultTest.setup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.rsocket.test.RsocketDefaultTest.setup();
                return null;
              }
            }

            public final class MetaCleanupMethod extends StaticMetaMethod<Void> {
              private MetaCleanupMethod(MetaClass owner) {
                super("cleanup",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.rsocket.test.RsocketDefaultTest.cleanup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.rsocket.test.RsocketDefaultTest.cleanup();
                return null;
              }
            }

            public final class MetaTestDefaultRsocketMethod extends InstanceMetaMethod<io.art.rsocket.test.RsocketDefaultTest, Void> {
              private MetaTestDefaultRsocketMethod(MetaClass owner) {
                super("testDefaultRsocket",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(io.art.rsocket.test.RsocketDefaultTest instance,
                  Object[] arguments) throws Throwable {
                instance.testDefaultRsocket();
                return null;
              }

              @Override
              public Object invoke(io.art.rsocket.test.RsocketDefaultTest instance) throws
                  Throwable {
                instance.testDefaultRsocket();
                return null;
              }
            }
          }

          public static final class MetaCommunicatorPackage extends MetaPackage {
            private final MetaTestRsocketClass testRsocketClass = register(new MetaTestRsocketClass());

            private MetaCommunicatorPackage() {
              super("communicator");
            }

            public MetaTestRsocketClass testRsocketClass() {
              return testRsocketClass;
            }

            public static final class MetaTestRsocketClass extends MetaClass<io.art.rsocket.test.communicator.TestRsocket> {
              private static final LazyProperty<MetaTestRsocketClass> self = MetaClass.self(io.art.rsocket.test.communicator.TestRsocket.class);

              private final MetaM1Method m1Method = register(new MetaM1Method(this));

              private final MetaM2Method m2Method = register(new MetaM2Method(this));

              private final MetaM3Method m3Method = register(new MetaM3Method(this));

              private final MetaM4Method m4Method = register(new MetaM4Method(this));

              private final MetaM5Method m5Method = register(new MetaM5Method(this));

              private final MetaM6Method m6Method = register(new MetaM6Method(this));

              private final MetaM7Method m7Method = register(new MetaM7Method(this));

              private final MetaM8Method m8Method = register(new MetaM8Method(this));

              private final MetaM9Method m9Method = register(new MetaM9Method(this));

              private final MetaM10Method m10Method = register(new MetaM10Method(this));

              private final MetaM11Method m11Method = register(new MetaM11Method(this));

              private final MetaM12Method m12Method = register(new MetaM12Method(this));

              private final MetaM13Method m13Method = register(new MetaM13Method(this));

              private final MetaM14Method m14Method = register(new MetaM14Method(this));

              private final MetaM15Method m15Method = register(new MetaM15Method(this));

              private final MetaM16Method m16Method = register(new MetaM16Method(this));

              private final MetaTestRsocketConnectorClass testRsocketConnectorClass = register(new MetaTestRsocketConnectorClass());

              private MetaTestRsocketClass() {
                super(metaType(io.art.rsocket.test.communicator.TestRsocket.class));
              }

              public static MetaTestRsocketClass testRsocket() {
                return self.get();
              }

              public MetaM1Method m1Method() {
                return m1Method;
              }

              public MetaM2Method m2Method() {
                return m2Method;
              }

              public MetaM3Method m3Method() {
                return m3Method;
              }

              public MetaM4Method m4Method() {
                return m4Method;
              }

              public MetaM5Method m5Method() {
                return m5Method;
              }

              public MetaM6Method m6Method() {
                return m6Method;
              }

              public MetaM7Method m7Method() {
                return m7Method;
              }

              public MetaM8Method m8Method() {
                return m8Method;
              }

              public MetaM9Method m9Method() {
                return m9Method;
              }

              public MetaM10Method m10Method() {
                return m10Method;
              }

              public MetaM11Method m11Method() {
                return m11Method;
              }

              public MetaM12Method m12Method() {
                return m12Method;
              }

              public MetaM13Method m13Method() {
                return m13Method;
              }

              public MetaM14Method m14Method() {
                return m14Method;
              }

              public MetaM15Method m15Method() {
                return m15Method;
              }

              public MetaM16Method m16Method() {
                return m16Method;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsocketProxy(invocations);
              }

              public MetaTestRsocketConnectorClass testRsocketConnectorClass() {
                return testRsocketConnectorClass;
              }

              public final class MetaM1Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private MetaM1Method(MetaClass owner) {
                  super("m1",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m1();
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance) throws
                    Throwable {
                  instance.m1();
                  return null;
                }
              }

              public final class MetaM2Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, java.lang.String> {
                private MetaM2Method(MetaClass owner) {
                  super("m2",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m2();
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance) throws
                    Throwable {
                  return instance.m2();
                }
              }

              public final class MetaM3Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method(MetaClass owner) {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m3();
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance) throws
                    Throwable {
                  return instance.m3();
                }
              }

              public final class MetaM4Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method(MetaClass owner) {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m4();
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance) throws
                    Throwable {
                  return instance.m4();
                }
              }

              public final class MetaM5Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method(MetaClass owner) {
                  super("m5",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM6Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method(MetaClass owner) {
                  super("m6",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM7Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method(MetaClass owner) {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM8Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method(MetaClass owner) {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM9Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method(MetaClass owner) {
                  super("m9",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM10Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method(MetaClass owner) {
                  super("m10",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM11Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method(MetaClass owner) {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM12Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method(MetaClass owner) {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM13Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method(MetaClass owner) {
                  super("m13",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM14Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method(MetaClass owner) {
                  super("m14",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM15Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method(MetaClass owner) {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM16Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method(MetaClass owner) {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public class MetaTestRsocketProxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocket {
                private final Function<Object, Object> m1Invocation;

                private final Function<Object, Object> m2Invocation;

                private final Function<Object, Object> m3Invocation;

                private final Function<Object, Object> m4Invocation;

                private final Function<Object, Object> m5Invocation;

                private final Function<Object, Object> m6Invocation;

                private final Function<Object, Object> m7Invocation;

                private final Function<Object, Object> m8Invocation;

                private final Function<Object, Object> m9Invocation;

                private final Function<Object, Object> m10Invocation;

                private final Function<Object, Object> m11Invocation;

                private final Function<Object, Object> m12Invocation;

                private final Function<Object, Object> m13Invocation;

                private final Function<Object, Object> m14Invocation;

                private final Function<Object, Object> m15Invocation;

                private final Function<Object, Object> m16Invocation;

                public MetaTestRsocketProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  m1Invocation = invocations.get(m1Method);
                  m2Invocation = invocations.get(m2Method);
                  m3Invocation = invocations.get(m3Method);
                  m4Invocation = invocations.get(m4Method);
                  m5Invocation = invocations.get(m5Method);
                  m6Invocation = invocations.get(m6Method);
                  m7Invocation = invocations.get(m7Method);
                  m8Invocation = invocations.get(m8Method);
                  m9Invocation = invocations.get(m9Method);
                  m10Invocation = invocations.get(m10Method);
                  m11Invocation = invocations.get(m11Method);
                  m12Invocation = invocations.get(m12Method);
                  m13Invocation = invocations.get(m13Method);
                  m14Invocation = invocations.get(m14Method);
                  m15Invocation = invocations.get(m15Method);
                  m16Invocation = invocations.get(m16Method);
                }

                @Override
                public void m1() {
                  m1Invocation.apply(null);
                }

                @Override
                public java.lang.String m2() {
                  return (java.lang.String)(m2Invocation.apply(null));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> m3() {
                  return (reactor.core.publisher.Mono<java.lang.String>)(m3Invocation.apply(null));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> m4() {
                  return (reactor.core.publisher.Flux<java.lang.String>)(m4Invocation.apply(null));
                }

                @Override
                public void m5(java.lang.String input) {
                  m5Invocation.apply(input);
                }

                @Override
                public java.lang.String m6(java.lang.String input) {
                  return (java.lang.String)(m6Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> m7(java.lang.String input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(m7Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> m8(java.lang.String input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(m8Invocation.apply(input));
                }

                @Override
                public void m9(reactor.core.publisher.Mono<java.lang.String> input) {
                  m9Invocation.apply(input);
                }

                @Override
                public java.lang.String m10(reactor.core.publisher.Mono<java.lang.String> input) {
                  return (java.lang.String)(m10Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> m11(
                    reactor.core.publisher.Mono<java.lang.String> input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(m11Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> m12(
                    reactor.core.publisher.Mono<java.lang.String> input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(m12Invocation.apply(input));
                }

                @Override
                public void m13(reactor.core.publisher.Flux<java.lang.String> input) {
                  m13Invocation.apply(input);
                }

                @Override
                public java.lang.String m14(reactor.core.publisher.Flux<java.lang.String> input) {
                  return (java.lang.String)(m14Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> m15(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(m15Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> m16(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(m16Invocation.apply(input));
                }
              }

              public static final class MetaTestRsocketConnectorClass extends MetaClass<io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector> {
                private static final LazyProperty<MetaTestRsocketConnectorClass> self = MetaClass.self(io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector.class);

                private final MetaTestRsocketMethod testRsocketMethod = register(new MetaTestRsocketMethod(this));

                private MetaTestRsocketConnectorClass() {
                  super(metaType(io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector.class));
                }

                public static MetaTestRsocketConnectorClass testRsocketConnector() {
                  return self.get();
                }

                public MetaTestRsocketMethod testRsocketMethod() {
                  return testRsocketMethod;
                }

                @Override
                public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  return new MetaTestRsocketConnectorProxy(invocations);
                }

                public final class MetaTestRsocketMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector, io.art.rsocket.test.communicator.TestRsocket> {
                  private MetaTestRsocketMethod(MetaClass owner) {
                    super("testRsocket",metaType(io.art.rsocket.test.communicator.TestRsocket.class),owner);
                  }

                  @Override
                  public Object invoke(
                      io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector instance,
                      Object[] arguments) throws Throwable {
                    return instance.testRsocket();
                  }

                  @Override
                  public Object invoke(
                      io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector instance)
                      throws Throwable {
                    return instance.testRsocket();
                  }
                }

                public class MetaTestRsocketConnectorProxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector {
                  private final Function<Object, Object> testRsocketInvocation;

                  public MetaTestRsocketConnectorProxy(
                      Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                    super(invocations);
                    testRsocketInvocation = invocations.get(testRsocketMethod);
                  }

                  @Override
                  public io.art.rsocket.test.communicator.TestRsocket testRsocket() {
                    return (io.art.rsocket.test.communicator.TestRsocket)(testRsocketInvocation.apply(null));
                  }
                }
              }
            }
          }

          public static final class MetaServicePackage extends MetaPackage {
            private final MetaTestRsocketServiceClass testRsocketServiceClass = register(new MetaTestRsocketServiceClass());

            private final MetaBenchmarkRsocketServiceClass benchmarkRsocketServiceClass = register(new MetaBenchmarkRsocketServiceClass());

            private MetaServicePackage() {
              super("service");
            }

            public MetaTestRsocketServiceClass testRsocketServiceClass() {
              return testRsocketServiceClass;
            }

            public MetaBenchmarkRsocketServiceClass benchmarkRsocketServiceClass() {
              return benchmarkRsocketServiceClass;
            }

            public static final class MetaTestRsocketServiceClass extends MetaClass<io.art.rsocket.test.service.TestRsocketService> {
              private static final LazyProperty<MetaTestRsocketServiceClass> self = MetaClass.self(io.art.rsocket.test.service.TestRsocketService.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaM1Method m1Method = register(new MetaM1Method(this));

              private final MetaM2Method m2Method = register(new MetaM2Method(this));

              private final MetaM3Method m3Method = register(new MetaM3Method(this));

              private final MetaM4Method m4Method = register(new MetaM4Method(this));

              private final MetaM5Method m5Method = register(new MetaM5Method(this));

              private final MetaM6Method m6Method = register(new MetaM6Method(this));

              private final MetaM7Method m7Method = register(new MetaM7Method(this));

              private final MetaM8Method m8Method = register(new MetaM8Method(this));

              private final MetaM9Method m9Method = register(new MetaM9Method(this));

              private final MetaM10Method m10Method = register(new MetaM10Method(this));

              private final MetaM11Method m11Method = register(new MetaM11Method(this));

              private final MetaM12Method m12Method = register(new MetaM12Method(this));

              private final MetaM13Method m13Method = register(new MetaM13Method(this));

              private final MetaM14Method m14Method = register(new MetaM14Method(this));

              private final MetaM15Method m15Method = register(new MetaM15Method(this));

              private final MetaM16Method m16Method = register(new MetaM16Method(this));

              private MetaTestRsocketServiceClass() {
                super(metaType(io.art.rsocket.test.service.TestRsocketService.class));
              }

              public static MetaTestRsocketServiceClass testRsocketService() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaM1Method m1Method() {
                return m1Method;
              }

              public MetaM2Method m2Method() {
                return m2Method;
              }

              public MetaM3Method m3Method() {
                return m3Method;
              }

              public MetaM4Method m4Method() {
                return m4Method;
              }

              public MetaM5Method m5Method() {
                return m5Method;
              }

              public MetaM6Method m6Method() {
                return m6Method;
              }

              public MetaM7Method m7Method() {
                return m7Method;
              }

              public MetaM8Method m8Method() {
                return m8Method;
              }

              public MetaM9Method m9Method() {
                return m9Method;
              }

              public MetaM10Method m10Method() {
                return m10Method;
              }

              public MetaM11Method m11Method() {
                return m11Method;
              }

              public MetaM12Method m12Method() {
                return m12Method;
              }

              public MetaM13Method m13Method() {
                return m13Method;
              }

              public MetaM14Method m14Method() {
                return m14Method;
              }

              public MetaM15Method m15Method() {
                return m15Method;
              }

              public MetaM16Method m16Method() {
                return m16Method;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.test.service.TestRsocketService> {
                private MetaConstructorConstructor(MetaClass owner) {
                  super(metaType(io.art.rsocket.test.service.TestRsocketService.class),owner);
                }

                @Override
                public io.art.rsocket.test.service.TestRsocketService invoke(Object[] arguments)
                    throws Throwable {
                  return new io.art.rsocket.test.service.TestRsocketService();
                }

                @Override
                public io.art.rsocket.test.service.TestRsocketService invoke() throws Throwable {
                  return new io.art.rsocket.test.service.TestRsocketService();
                }
              }

              public final class MetaM1Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private MetaM1Method(MetaClass owner) {
                  super("m1",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m1();
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance) throws
                    Throwable {
                  instance.m1();
                  return null;
                }
              }

              public final class MetaM2Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, java.lang.String> {
                private MetaM2Method(MetaClass owner) {
                  super("m2",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m2();
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance) throws
                    Throwable {
                  return instance.m2();
                }
              }

              public final class MetaM3Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method(MetaClass owner) {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m3();
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance) throws
                    Throwable {
                  return instance.m3();
                }
              }

              public final class MetaM4Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method(MetaClass owner) {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m4();
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance) throws
                    Throwable {
                  return instance.m4();
                }
              }

              public final class MetaM5Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method(MetaClass owner) {
                  super("m5",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM6Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method(MetaClass owner) {
                  super("m6",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM7Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method(MetaClass owner) {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM8Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method(MetaClass owner) {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM9Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method(MetaClass owner) {
                  super("m9",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM10Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method(MetaClass owner) {
                  super("m10",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM11Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method(MetaClass owner) {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM12Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method(MetaClass owner) {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM13Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method(MetaClass owner) {
                  super("m13",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM14Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method(MetaClass owner) {
                  super("m14",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM15Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method(MetaClass owner) {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM16Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method(MetaClass owner) {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }
            }

            public static final class MetaBenchmarkRsocketServiceClass extends MetaClass<io.art.rsocket.test.service.BenchmarkRsocketService> {
              private static final LazyProperty<MetaBenchmarkRsocketServiceClass> self = MetaClass.self(io.art.rsocket.test.service.BenchmarkRsocketService.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaM1Method m1Method = register(new MetaM1Method(this));

              private final MetaM2Method m2Method = register(new MetaM2Method(this));

              private final MetaM3Method m3Method = register(new MetaM3Method(this));

              private final MetaM4Method m4Method = register(new MetaM4Method(this));

              private final MetaM5Method m5Method = register(new MetaM5Method(this));

              private final MetaM6Method m6Method = register(new MetaM6Method(this));

              private final MetaM7Method m7Method = register(new MetaM7Method(this));

              private final MetaM8Method m8Method = register(new MetaM8Method(this));

              private final MetaM9Method m9Method = register(new MetaM9Method(this));

              private final MetaM10Method m10Method = register(new MetaM10Method(this));

              private final MetaM11Method m11Method = register(new MetaM11Method(this));

              private final MetaM12Method m12Method = register(new MetaM12Method(this));

              private final MetaM13Method m13Method = register(new MetaM13Method(this));

              private final MetaM14Method m14Method = register(new MetaM14Method(this));

              private final MetaM15Method m15Method = register(new MetaM15Method(this));

              private final MetaM16Method m16Method = register(new MetaM16Method(this));

              private MetaBenchmarkRsocketServiceClass() {
                super(metaType(io.art.rsocket.test.service.BenchmarkRsocketService.class));
              }

              public static MetaBenchmarkRsocketServiceClass benchmarkRsocketService() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaM1Method m1Method() {
                return m1Method;
              }

              public MetaM2Method m2Method() {
                return m2Method;
              }

              public MetaM3Method m3Method() {
                return m3Method;
              }

              public MetaM4Method m4Method() {
                return m4Method;
              }

              public MetaM5Method m5Method() {
                return m5Method;
              }

              public MetaM6Method m6Method() {
                return m6Method;
              }

              public MetaM7Method m7Method() {
                return m7Method;
              }

              public MetaM8Method m8Method() {
                return m8Method;
              }

              public MetaM9Method m9Method() {
                return m9Method;
              }

              public MetaM10Method m10Method() {
                return m10Method;
              }

              public MetaM11Method m11Method() {
                return m11Method;
              }

              public MetaM12Method m12Method() {
                return m12Method;
              }

              public MetaM13Method m13Method() {
                return m13Method;
              }

              public MetaM14Method m14Method() {
                return m14Method;
              }

              public MetaM15Method m15Method() {
                return m15Method;
              }

              public MetaM16Method m16Method() {
                return m16Method;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.test.service.BenchmarkRsocketService> {
                private MetaConstructorConstructor(MetaClass owner) {
                  super(metaType(io.art.rsocket.test.service.BenchmarkRsocketService.class),owner);
                }

                @Override
                public io.art.rsocket.test.service.BenchmarkRsocketService invoke(
                    Object[] arguments) throws Throwable {
                  return new io.art.rsocket.test.service.BenchmarkRsocketService();
                }

                @Override
                public io.art.rsocket.test.service.BenchmarkRsocketService invoke() throws
                    Throwable {
                  return new io.art.rsocket.test.service.BenchmarkRsocketService();
                }
              }

              public final class MetaM1Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, Void> {
                private MetaM1Method(MetaClass owner) {
                  super("m1",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m1();
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance)
                    throws Throwable {
                  instance.m1();
                  return null;
                }
              }

              public final class MetaM2Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, java.lang.String> {
                private MetaM2Method(MetaClass owner) {
                  super("m2",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m2();
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance)
                    throws Throwable {
                  return instance.m2();
                }
              }

              public final class MetaM3Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method(MetaClass owner) {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m3();
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance)
                    throws Throwable {
                  return instance.m3();
                }
              }

              public final class MetaM4Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method(MetaClass owner) {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m4();
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance)
                    throws Throwable {
                  return instance.m4();
                }
              }

              public final class MetaM5Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method(MetaClass owner) {
                  super("m5",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM6Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method(MetaClass owner) {
                  super("m6",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM7Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method(MetaClass owner) {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM8Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method(MetaClass owner) {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM9Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method(MetaClass owner) {
                  super("m9",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM10Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method(MetaClass owner) {
                  super("m10",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM11Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method(MetaClass owner) {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM12Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method(MetaClass owner) {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM13Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method(MetaClass owner) {
                  super("m13",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM14Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method(MetaClass owner) {
                  super("m14",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM15Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method(MetaClass owner) {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM16Method extends InstanceMetaMethod<io.art.rsocket.test.service.BenchmarkRsocketService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method(MetaClass owner) {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object[] arguments) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.BenchmarkRsocketService instance,
                    Object argument) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }
            }
          }
        }
      }
    }
  }
}
