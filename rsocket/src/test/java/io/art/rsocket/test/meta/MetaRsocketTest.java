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
          private final MetaRsocketTestClass rsocketTestClass = register(new MetaRsocketTestClass());

          private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaRsocketTestClass rsocketTestClass() {
            return rsocketTestClass;
          }

          public MetaCommunicatorPackage communicatorPackage() {
            return communicatorPackage;
          }

          public MetaServicePackage servicePackage() {
            return servicePackage;
          }

          public static final class MetaRsocketTestClass extends MetaClass<io.art.rsocket.test.RsocketTest> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod());

            private final MetaTestMethod testMethod = register(new MetaTestMethod());

            private MetaRsocketTestClass() {
              super(metaType(io.art.rsocket.test.RsocketTest.class));
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaSetupMethod setupMethod() {
              return setupMethod;
            }

            public MetaTestMethod testMethod() {
              return testMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.rsocket.test.RsocketTest> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.rsocket.test.RsocketTest.class));
              }

              @Override
              public io.art.rsocket.test.RsocketTest invoke(Object[] arguments) throws Throwable {
                return new io.art.rsocket.test.RsocketTest();
              }

              @Override
              public io.art.rsocket.test.RsocketTest invoke() throws Throwable {
                return new io.art.rsocket.test.RsocketTest();
              }
            }

            public static final class MetaSetupMethod extends StaticMetaMethod<Void> {
              private MetaSetupMethod() {
                super("setup",metaType(Void.class));
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.rsocket.test.RsocketTest.setup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.rsocket.test.RsocketTest.setup();
                return null;
              }
            }

            public static final class MetaTestMethod extends InstanceMetaMethod<io.art.rsocket.test.RsocketTest, Void> {
              private MetaTestMethod() {
                super("test",metaType(Void.class));
              }

              @Override
              public Object invoke(io.art.rsocket.test.RsocketTest instance, Object[] arguments)
                  throws Throwable {
                instance.test();
                return null;
              }

              @Override
              public Object invoke(io.art.rsocket.test.RsocketTest instance) throws Throwable {
                instance.test();
                return null;
              }
            }
          }

          public static final class MetaCommunicatorPackage extends MetaPackage {
            private final MetaTestRsocketClass testRsocketClass = register(new MetaTestRsocketClass());

            private final MetaTestRsocketConnector1Class testRsocketConnector1Class = register(new MetaTestRsocketConnector1Class());

            private final MetaTestRsocketConnector2Class testRsocketConnector2Class = register(new MetaTestRsocketConnector2Class());

            private MetaCommunicatorPackage() {
              super("communicator");
            }

            public MetaTestRsocketClass testRsocketClass() {
              return testRsocketClass;
            }

            public MetaTestRsocketConnector1Class testRsocketConnector1Class() {
              return testRsocketConnector1Class;
            }

            public MetaTestRsocketConnector2Class testRsocketConnector2Class() {
              return testRsocketConnector2Class;
            }

            public static final class MetaTestRsocketClass extends MetaClass<io.art.rsocket.test.communicator.TestRsocket> {
              private final MetaM1Method m1Method = register(new MetaM1Method());

              private final MetaM2Method m2Method = register(new MetaM2Method());

              private final MetaM3Method m3Method = register(new MetaM3Method());

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

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsocketProxy(invocations);
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

              public class MetaTestRsocketProxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocket {
                private final Function<Object, Object> m1Invocation;

                private final Function<Object, Object> m2Invocation;

                private final Function<Object, Object> m3Invocation;

                public MetaTestRsocketProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  m1Invocation = invocations.get(m1Method);
                  m2Invocation = invocations.get(m2Method);
                  m3Invocation = invocations.get(m3Method);
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
              }
            }

            public static final class MetaTestRsocketConnector1Class extends MetaClass<io.art.rsocket.test.communicator.TestRsocketConnector1> {
              private final MetaTestRsocketMethod testRsocketMethod = register(new MetaTestRsocketMethod());

              private MetaTestRsocketConnector1Class() {
                super(metaType(io.art.rsocket.test.communicator.TestRsocketConnector1.class));
              }

              public MetaTestRsocketMethod testRsocketMethod() {
                return testRsocketMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsocketConnector1Proxy(invocations);
              }

              public static final class MetaTestRsocketMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocketConnector1, io.art.rsocket.test.communicator.TestRsocket> {
                private MetaTestRsocketMethod() {
                  super("testRsocket",metaType(io.art.rsocket.test.communicator.TestRsocket.class));
                }

                @Override
                public Object invoke(
                    io.art.rsocket.test.communicator.TestRsocketConnector1 instance,
                    Object[] arguments) throws Throwable {
                  return instance.testRsocket();
                }

                @Override
                public Object invoke(
                    io.art.rsocket.test.communicator.TestRsocketConnector1 instance) throws
                    Throwable {
                  return instance.testRsocket();
                }
              }

              public class MetaTestRsocketConnector1Proxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocketConnector1 {
                private final Function<Object, Object> testRsocketInvocation;

                public MetaTestRsocketConnector1Proxy(
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

            public static final class MetaTestRsocketConnector2Class extends MetaClass<io.art.rsocket.test.communicator.TestRsocketConnector2> {
              private final MetaTestRsocketMethod testRsocketMethod = register(new MetaTestRsocketMethod());

              private MetaTestRsocketConnector2Class() {
                super(metaType(io.art.rsocket.test.communicator.TestRsocketConnector2.class));
              }

              public MetaTestRsocketMethod testRsocketMethod() {
                return testRsocketMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestRsocketConnector2Proxy(invocations);
              }

              public static final class MetaTestRsocketMethod extends InstanceMetaMethod<io.art.rsocket.test.communicator.TestRsocketConnector2, io.art.rsocket.test.communicator.TestRsocket> {
                private MetaTestRsocketMethod() {
                  super("testRsocket",metaType(io.art.rsocket.test.communicator.TestRsocket.class));
                }

                @Override
                public Object invoke(
                    io.art.rsocket.test.communicator.TestRsocketConnector2 instance,
                    Object[] arguments) throws Throwable {
                  return instance.testRsocket();
                }

                @Override
                public Object invoke(
                    io.art.rsocket.test.communicator.TestRsocketConnector2 instance) throws
                    Throwable {
                  return instance.testRsocket();
                }
              }

              public class MetaTestRsocketConnector2Proxy extends MetaProxy implements io.art.rsocket.test.communicator.TestRsocketConnector2 {
                private final Function<Object, Object> testRsocketInvocation;

                public MetaTestRsocketConnector2Proxy(
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
            }
          }
        }
      }
    }
  }
}
