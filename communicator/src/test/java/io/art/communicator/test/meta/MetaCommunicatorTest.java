package io.art.communicator.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.core.property.LazyProperty;
import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaMethod;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.MetaProxy;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaCommunicatorTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaCommunicatorTest(MetaLibrary... dependencies) {
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
      private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaCommunicatorPackage communicatorPackage() {
        return communicatorPackage;
      }

      public static final class MetaCommunicatorPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaCommunicatorPackage() {
          super("communicator");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaProxyPackage proxyPackage = register(new MetaProxyPackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaProxyPackage proxyPackage() {
            return proxyPackage;
          }

          public static final class MetaProxyPackage extends MetaPackage {
            private final MetaTestCommunicatorClass testCommunicatorClass = register(new MetaTestCommunicatorClass());

            private MetaProxyPackage() {
              super("proxy");
            }

            public MetaTestCommunicatorClass testCommunicatorClass() {
              return testCommunicatorClass;
            }

            public static final class MetaTestCommunicatorClass extends MetaClass<io.art.communicator.test.proxy.TestCommunicator> {
              private static final LazyProperty<MetaTestCommunicatorClass> self = MetaClass.self(io.art.communicator.test.proxy.TestCommunicator.class);

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

              private MetaTestCommunicatorClass() {
                super(metaType(io.art.communicator.test.proxy.TestCommunicator.class));
              }

              public static MetaTestCommunicatorClass testCommunicator() {
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
              public MetaProxy proxy(
                  Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
                return new MetaTestCommunicatorProxy(invocations);
              }

              public final class MetaM1Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, Void> {
                private MetaM1Method(MetaTestCommunicatorClass owner) {
                  super("m1",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  instance.m1();
                  return null;
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance)
                    throws Throwable {
                  instance.m1();
                  return null;
                }
              }

              public final class MetaM2Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private MetaM2Method(MetaTestCommunicatorClass owner) {
                  super("m2",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m2();
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance)
                    throws Throwable {
                  return instance.m2();
                }
              }

              public final class MetaM3Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaM3Method(MetaTestCommunicatorClass owner) {
                  super("m3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m3();
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance)
                    throws Throwable {
                  return instance.m3();
                }
              }

              public final class MetaM4Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaM4Method(MetaTestCommunicatorClass owner) {
                  super("m4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m4();
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance)
                    throws Throwable {
                  return instance.m4();
                }
              }

              public final class MetaM5Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM5Method(MetaTestCommunicatorClass owner) {
                  super("m5",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  instance.m5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  instance.m5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM6Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM6Method(MetaTestCommunicatorClass owner) {
                  super("m6",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM7Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM7Method(MetaTestCommunicatorClass owner) {
                  super("m7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM8Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM8Method(MetaTestCommunicatorClass owner) {
                  super("m8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public final class MetaM9Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM9Method(MetaTestCommunicatorClass owner) {
                  super("m9",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  instance.m9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM10Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM10Method(MetaTestCommunicatorClass owner) {
                  super("m10",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM11Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM11Method(MetaTestCommunicatorClass owner) {
                  super("m11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM12Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaM12Method(MetaTestCommunicatorClass owner) {
                  super("m12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM13Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM13Method(MetaTestCommunicatorClass owner) {
                  super("m13",metaType(Void.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  instance.m13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM14Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM14Method(MetaTestCommunicatorClass owner) {
                  super("m14",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM15Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM15Method(MetaTestCommunicatorClass owner) {
                  super("m15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public final class MetaM16Method extends InstanceMetaMethod<MetaTestCommunicatorClass, io.art.communicator.test.proxy.TestCommunicator, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM16Method(MetaTestCommunicatorClass owner) {
                  super("m16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public class MetaTestCommunicatorProxy extends MetaProxy implements io.art.communicator.test.proxy.TestCommunicator {
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

                public MetaTestCommunicatorProxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
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
            }
          }
        }
      }
    }
  }
}
