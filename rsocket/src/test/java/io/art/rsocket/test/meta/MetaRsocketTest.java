package io.art.rsocket.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaMethod;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.MetaProxy;
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
          private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaCommunicatorPackage communicatorPackage() {
            return communicatorPackage;
          }

          public MetaServicePackage servicePackage() {
            return servicePackage;
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
              private final MetaM1Method m1Method = register(new MetaM1Method());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private final MetaM3Method m3Method = register(new MetaM3Method());

              private final MetaM4Method m4Method = register(new MetaM4Method());

              private final MetaTestRsocketConnectorClass testRsocketConnectorClass = register(new MetaTestRsocketConnectorClass());

              private MetaTestRsocketClass() {
                super(metaType(io.art.rsocket.test.communicator.TestRsocket.class));
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

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsocketProxy(invocations);
              }

              public MetaTestRsocketConnectorClass testRsocketConnectorClass() {
                return testRsocketConnectorClass;
              }

              public static final class MetaM1Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM1Method() {
                  super("m1",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m1((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m1((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaM2Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM2Method() {
                  super("m2",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m2((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m2((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaM3Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM3Method() {
                  super("m3",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m3((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m3((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaM4Method extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM4Method() {
                  super("m4",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object[] arguments) throws Throwable {
                  instance.m4((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.communicator.TestRsocket instance,
                    Object argument) throws Throwable {
                  instance.m4((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public class MetaTestRsocketProxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocket {
                private final Function<Object, Object> m1Invocation;

                private final Function<Object, Object> m2Invocation;

                private final Function<Object, Object> m3Invocation;

                private final Function<Object, Object> m4Invocation;

                public MetaTestRsocketProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  m1Invocation = invocations.get(m1Method);
                  m2Invocation = invocations.get(m2Method);
                  m3Invocation = invocations.get(m3Method);
                  m4Invocation = invocations.get(m4Method);
                }

                @Override
                public void m1(java.lang.String input) {
                  m1Invocation.apply(input);
                }

                @Override
                public void m2(java.lang.String input) {
                  m2Invocation.apply(input);
                }

                @Override
                public void m3(java.lang.String input) {
                  m3Invocation.apply(input);
                }

                @Override
                public void m4(java.lang.String input) {
                  m4Invocation.apply(input);
                }
              }

              public static final class MetaTestRsocketConnectorClass extends MetaClass<io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector> {
                private final MetaTestRsocketMethod testRsocketMethod = register(new MetaTestRsocketMethod());

                private MetaTestRsocketConnectorClass() {
                  super(metaType(io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector.class));
                }

                public MetaTestRsocketMethod testRsocketMethod() {
                  return testRsocketMethod;
                }

                @Override
                public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  return new MetaTestRsocketConnectorProxy(invocations);
                }

                public static final class MetaTestRsocketMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocket.TestRsocketConnector, io.art.rsocket.test.communicator.TestRsocket> {
                  private MetaTestRsocketMethod() {
                    super("testRsocket",metaType(io.art.rsocket.test.communicator.TestRsocket.class));
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

            private MetaServicePackage() {
              super("service");
            }

            public MetaTestRsocketServiceClass testRsocketServiceClass() {
              return testRsocketServiceClass;
            }

            public static final class MetaTestRsocketServiceClass extends MetaClass<io.art.rsocket.test.service.TestRsocketService> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaM1Method m1Method = register(new MetaM1Method());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private final MetaM3Method m3Method = register(new MetaM3Method());

              private final MetaM4Method m4Method = register(new MetaM4Method());

              private MetaTestRsocketServiceClass() {
                super(metaType(io.art.rsocket.test.service.TestRsocketService.class));
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

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.test.service.TestRsocketService> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.rsocket.test.service.TestRsocketService.class));
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

              public static final class MetaM1Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM1Method() {
                  super("m1",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m1((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m1((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaM2Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM2Method() {
                  super("m2",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m2((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m2((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaM3Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM3Method() {
                  super("m3",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m3((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m3((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaM4Method extends InstanceMetaMethod<io.art.rsocket.test.service.TestRsocketService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaM4Method() {
                  super("m4",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object[] arguments) throws Throwable {
                  instance.m4((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.rsocket.test.service.TestRsocketService instance,
                    Object argument) throws Throwable {
                  instance.m4((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
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
