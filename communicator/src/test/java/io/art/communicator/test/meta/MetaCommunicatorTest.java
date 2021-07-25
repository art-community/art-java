package io.art.communicator.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

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
              private final MetaM0Method m0Method = register(new MetaM0Method());

              private final MetaM1Method m1Method = register(new MetaM1Method());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private final MetaM3Method m3Method = register(new MetaM3Method());

              private final MetaM3_1Method m3_1Method = register(new MetaM3_1Method());

              private MetaTestCommunicatorClass() {
                super(metaType(io.art.communicator.test.proxy.TestCommunicator.class));
              }

              public MetaM0Method m0Method() {
                return m0Method;
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

              public MetaM3_1Method m3_1Method() {
                return m3_1Method;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestCommunicatorProxy(invocations);
              }

              public static final class MetaM0Method extends InstanceMetaMethod<io.art.communicator.test.proxy.TestCommunicator, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM0Method() {
                  super("m0",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  instance.m0((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  instance.m0((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaM1Method extends InstanceMetaMethod<io.art.communicator.test.proxy.TestCommunicator, Void> {
                private MetaM1Method() {
                  super("m1",metaType(Void.class));
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

              public static final class MetaM2Method extends InstanceMetaMethod<io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaM2Method() {
                  super("m2",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m2((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object argument) throws Throwable {
                  return instance.m2((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaM3Method extends InstanceMetaMethod<io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private MetaM3Method() {
                  super("m3",metaType(java.lang.String.class));
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

              public static final class MetaM3_1Method extends InstanceMetaMethod<io.art.communicator.test.proxy.TestCommunicator, java.lang.String> {
                private final MetaParameter<java.lang.String> v1Parameter = register(new MetaParameter<>(0, "v1",metaType(java.lang.String.class)));

                private final MetaParameter<java.lang.String> v2Parameter = register(new MetaParameter<>(1, "v2",metaType(java.lang.String.class)));

                private MetaM3_1Method() {
                  super("m3",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.communicator.test.proxy.TestCommunicator instance,
                    Object[] arguments) throws Throwable {
                  return instance.m3((java.lang.String)(arguments[0]),(java.lang.String)(arguments[1]));
                }

                public MetaParameter<java.lang.String> v1Parameter() {
                  return v1Parameter;
                }

                public MetaParameter<java.lang.String> v2Parameter() {
                  return v2Parameter;
                }
              }

              public class MetaTestCommunicatorProxy extends MetaProxy implements io.art.communicator.test.proxy.TestCommunicator {
                private final Function<Object, Object> m0Invocation;

                private final Function<Object, Object> m1Invocation;

                private final Function<Object, Object> m2Invocation;

                private final Function<Object, Object> m3Invocation;

                private final Function<Object, Object> m3_1Invocation;

                public MetaTestCommunicatorProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  m0Invocation = invocations.get(m0Method);
                  m1Invocation = invocations.get(m1Method);
                  m2Invocation = invocations.get(m2Method);
                  m3Invocation = invocations.get(m3Method);
                  m3_1Invocation = invocations.get(m3_1Method);
                }

                @Override
                public void m0(reactor.core.publisher.Flux<java.lang.String> input) {
                  m0Invocation.apply(input);
                }

                @Override
                public void m1() {
                  m1Invocation.apply(null);
                }

                @Override
                public java.lang.String m2(reactor.core.publisher.Flux<java.lang.String> input) {
                  return (java.lang.String)(m2Invocation.apply(input));
                }

                @Override
                public java.lang.String m3() {
                  return (java.lang.String)(m3Invocation.apply(null));
                }

                @Override
                public java.lang.String m3(java.lang.String v1, java.lang.String v2) {
                  return (java.lang.String)(m3_1Invocation.apply(new Object[]{v1,v2}));
                }
              }
            }
          }
        }
      }
    }
  }
}
