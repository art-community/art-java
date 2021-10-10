package io.art.http.test.meta;

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
public class MetaHttpTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaHttpTest(MetaLibrary... dependencies) {
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
      private final MetaHttpPackage httpPackage = register(new MetaHttpPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaHttpPackage httpPackage() {
        return httpPackage;
      }

      public static final class MetaHttpPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaHttpPackage() {
          super("http");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaWsTestClass wsTestClass = register(new MetaWsTestClass());

          private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaWsTestClass wsTestClass() {
            return wsTestClass;
          }

          public MetaCommunicatorPackage communicatorPackage() {
            return communicatorPackage;
          }

          public MetaServicePackage servicePackage() {
            return servicePackage;
          }

          public static final class MetaWsTestClass extends MetaClass<io.art.http.test.WsTest> {
            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod());

            private final MetaCleanupMethod cleanupMethod = register(new MetaCleanupMethod());

            private final MetaTestWsMethod testWsMethod = register(new MetaTestWsMethod());

            private MetaWsTestClass() {
              super(metaType(io.art.http.test.WsTest.class));
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

            public MetaTestWsMethod testWsMethod() {
              return testWsMethod;
            }

            public static final class MetaConstructorConstructor extends MetaConstructor<io.art.http.test.WsTest> {
              private MetaConstructorConstructor() {
                super(metaType(io.art.http.test.WsTest.class));
              }

              @Override
              public io.art.http.test.WsTest invoke(Object[] arguments) throws Throwable {
                return new io.art.http.test.WsTest();
              }

              @Override
              public io.art.http.test.WsTest invoke() throws Throwable {
                return new io.art.http.test.WsTest();
              }
            }

            public static final class MetaSetupMethod extends StaticMetaMethod<Void> {
              private MetaSetupMethod() {
                super("setup",metaType(Void.class));
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.http.test.WsTest.setup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.http.test.WsTest.setup();
                return null;
              }
            }

            public static final class MetaCleanupMethod extends StaticMetaMethod<Void> {
              private MetaCleanupMethod() {
                super("cleanup",metaType(Void.class));
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.http.test.WsTest.cleanup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.http.test.WsTest.cleanup();
                return null;
              }
            }

            public static final class MetaTestWsMethod extends InstanceMetaMethod<io.art.http.test.WsTest, Void> {
              private MetaTestWsMethod() {
                super("testWs",metaType(Void.class));
              }

              @Override
              public Object invoke(io.art.http.test.WsTest instance, Object[] arguments) throws
                  Throwable {
                instance.testWs();
                return null;
              }

              @Override
              public Object invoke(io.art.http.test.WsTest instance) throws Throwable {
                instance.testWs();
                return null;
              }
            }
          }

          public static final class MetaCommunicatorPackage extends MetaPackage {
            private final MetaTestHttpClass testHttpClass = register(new MetaTestHttpClass());

            private final MetaTestWsClass testWsClass = register(new MetaTestWsClass());

            private MetaCommunicatorPackage() {
              super("communicator");
            }

            public MetaTestHttpClass testHttpClass() {
              return testHttpClass;
            }

            public MetaTestWsClass testWsClass() {
              return testWsClass;
            }

            public static final class MetaTestHttpClass extends MetaClass<io.art.http.test.communicator.TestHttp> {
              private final MetaPost1Method post1Method = register(new MetaPost1Method());

              private final MetaPost2Method post2Method = register(new MetaPost2Method());

              private final MetaPost3Method post3Method = register(new MetaPost3Method());

              private final MetaPost4Method post4Method = register(new MetaPost4Method());

              private final MetaPost5Method post5Method = register(new MetaPost5Method());

              private final MetaPost6Method post6Method = register(new MetaPost6Method());

              private final MetaPost7Method post7Method = register(new MetaPost7Method());

              private final MetaPost8Method post8Method = register(new MetaPost8Method());

              private final MetaPost9Method post9Method = register(new MetaPost9Method());

              private final MetaPost10Method post10Method = register(new MetaPost10Method());

              private final MetaPost11Method post11Method = register(new MetaPost11Method());

              private final MetaPost12Method post12Method = register(new MetaPost12Method());

              private final MetaPost13Method post13Method = register(new MetaPost13Method());

              private final MetaPost14Method post14Method = register(new MetaPost14Method());

              private final MetaPost15Method post15Method = register(new MetaPost15Method());

              private final MetaPost16Method post16Method = register(new MetaPost16Method());

              private final MetaPost17Method post17Method = register(new MetaPost17Method());

              private final MetaPost18Method post18Method = register(new MetaPost18Method());

              private final MetaPost19Method post19Method = register(new MetaPost19Method());

              private final MetaPost20Method post20Method = register(new MetaPost20Method());

              private final MetaTestHttpConnectorClass testHttpConnectorClass = register(new MetaTestHttpConnectorClass());

              private MetaTestHttpClass() {
                super(metaType(io.art.http.test.communicator.TestHttp.class));
              }

              public MetaPost1Method post1Method() {
                return post1Method;
              }

              public MetaPost2Method post2Method() {
                return post2Method;
              }

              public MetaPost3Method post3Method() {
                return post3Method;
              }

              public MetaPost4Method post4Method() {
                return post4Method;
              }

              public MetaPost5Method post5Method() {
                return post5Method;
              }

              public MetaPost6Method post6Method() {
                return post6Method;
              }

              public MetaPost7Method post7Method() {
                return post7Method;
              }

              public MetaPost8Method post8Method() {
                return post8Method;
              }

              public MetaPost9Method post9Method() {
                return post9Method;
              }

              public MetaPost10Method post10Method() {
                return post10Method;
              }

              public MetaPost11Method post11Method() {
                return post11Method;
              }

              public MetaPost12Method post12Method() {
                return post12Method;
              }

              public MetaPost13Method post13Method() {
                return post13Method;
              }

              public MetaPost14Method post14Method() {
                return post14Method;
              }

              public MetaPost15Method post15Method() {
                return post15Method;
              }

              public MetaPost16Method post16Method() {
                return post16Method;
              }

              public MetaPost17Method post17Method() {
                return post17Method;
              }

              public MetaPost18Method post18Method() {
                return post18Method;
              }

              public MetaPost19Method post19Method() {
                return post19Method;
              }

              public MetaPost20Method post20Method() {
                return post20Method;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestHttpProxy(invocations);
              }

              public MetaTestHttpConnectorClass testHttpConnectorClass() {
                return testHttpConnectorClass;
              }

              public static final class MetaPost1Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, Void> {
                private MetaPost1Method() {
                  super("post1",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  instance.post1();
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance) throws
                    Throwable {
                  instance.post1();
                  return null;
                }
              }

              public static final class MetaPost2Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, java.lang.String> {
                private MetaPost2Method() {
                  super("post2",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post2();
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance) throws
                    Throwable {
                  return instance.post2();
                }
              }

              public static final class MetaPost3Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaPost3Method() {
                  super("post3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post3();
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance) throws
                    Throwable {
                  return instance.post3();
                }
              }

              public static final class MetaPost4Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaPost4Method() {
                  super("post4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post4();
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance) throws
                    Throwable {
                  return instance.post4();
                }
              }

              public static final class MetaPost5Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost5Method() {
                  super("post5",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  instance.post5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  instance.post5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost6Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost6Method() {
                  super("post6",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost7Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost7Method() {
                  super("post7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost8Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost8Method() {
                  super("post8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost9Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost9Method() {
                  super("post9",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  instance.post9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  instance.post9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost10Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost10Method() {
                  super("post10",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost11Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost11Method() {
                  super("post11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost12Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost12Method() {
                  super("post12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost13Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost13Method() {
                  super("post13",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  instance.post13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  instance.post13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost14Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost14Method() {
                  super("post14",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost15Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost15Method() {
                  super("post15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost16Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost16Method() {
                  super("post16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost17Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost17Method() {
                  super("post17",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  instance.post17((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  instance.post17((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }

              public static final class MetaPost18Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost18Method() {
                  super("post18",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post18((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post18((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }

              public static final class MetaPost19Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost19Method() {
                  super("post19",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post19((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post19((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }

              public static final class MetaPost20Method extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost20Method() {
                  super("post20",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.post20((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object argument) throws Throwable {
                  return instance.post20((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }

              public class MetaTestHttpProxy extends MetaProxy implements io.art.http.test.communicator.TestHttp {
                private final Function<Object, Object> post1Invocation;

                private final Function<Object, Object> post2Invocation;

                private final Function<Object, Object> post3Invocation;

                private final Function<Object, Object> post4Invocation;

                private final Function<Object, Object> post5Invocation;

                private final Function<Object, Object> post6Invocation;

                private final Function<Object, Object> post7Invocation;

                private final Function<Object, Object> post8Invocation;

                private final Function<Object, Object> post9Invocation;

                private final Function<Object, Object> post10Invocation;

                private final Function<Object, Object> post11Invocation;

                private final Function<Object, Object> post12Invocation;

                private final Function<Object, Object> post13Invocation;

                private final Function<Object, Object> post14Invocation;

                private final Function<Object, Object> post15Invocation;

                private final Function<Object, Object> post16Invocation;

                private final Function<Object, Object> post17Invocation;

                private final Function<Object, Object> post18Invocation;

                private final Function<Object, Object> post19Invocation;

                private final Function<Object, Object> post20Invocation;

                public MetaTestHttpProxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  post1Invocation = invocations.get(post1Method);
                  post2Invocation = invocations.get(post2Method);
                  post3Invocation = invocations.get(post3Method);
                  post4Invocation = invocations.get(post4Method);
                  post5Invocation = invocations.get(post5Method);
                  post6Invocation = invocations.get(post6Method);
                  post7Invocation = invocations.get(post7Method);
                  post8Invocation = invocations.get(post8Method);
                  post9Invocation = invocations.get(post9Method);
                  post10Invocation = invocations.get(post10Method);
                  post11Invocation = invocations.get(post11Method);
                  post12Invocation = invocations.get(post12Method);
                  post13Invocation = invocations.get(post13Method);
                  post14Invocation = invocations.get(post14Method);
                  post15Invocation = invocations.get(post15Method);
                  post16Invocation = invocations.get(post16Method);
                  post17Invocation = invocations.get(post17Method);
                  post18Invocation = invocations.get(post18Method);
                  post19Invocation = invocations.get(post19Method);
                  post20Invocation = invocations.get(post20Method);
                }

                @Override
                public void post1() {
                  post1Invocation.apply(null);
                }

                @Override
                public java.lang.String post2() {
                  return (java.lang.String)(post2Invocation.apply(null));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> post3() {
                  return (reactor.core.publisher.Mono<java.lang.String>)(post3Invocation.apply(null));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> post4() {
                  return (reactor.core.publisher.Flux<java.lang.String>)(post4Invocation.apply(null));
                }

                @Override
                public void post5(java.lang.String input) {
                  post5Invocation.apply(input);
                }

                @Override
                public java.lang.String post6(java.lang.String input) {
                  return (java.lang.String)(post6Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> post7(java.lang.String input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(post7Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> post8(java.lang.String input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(post8Invocation.apply(input));
                }

                @Override
                public void post9(reactor.core.publisher.Mono<java.lang.String> input) {
                  post9Invocation.apply(input);
                }

                @Override
                public java.lang.String post10(
                    reactor.core.publisher.Mono<java.lang.String> input) {
                  return (java.lang.String)(post10Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> post11(
                    reactor.core.publisher.Mono<java.lang.String> input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(post11Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> post12(
                    reactor.core.publisher.Mono<java.lang.String> input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(post12Invocation.apply(input));
                }

                @Override
                public void post13(reactor.core.publisher.Flux<java.lang.String> input) {
                  post13Invocation.apply(input);
                }

                @Override
                public java.lang.String post14(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (java.lang.String)(post14Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> post15(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(post15Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> post16(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(post16Invocation.apply(input));
                }

                @Override
                public void post17(reactor.core.publisher.Flux<java.lang.String> empty) {
                  post17Invocation.apply(empty);
                }

                @Override
                public java.lang.String post18(
                    reactor.core.publisher.Flux<java.lang.String> empty) {
                  return (java.lang.String)(post18Invocation.apply(empty));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> post19(
                    reactor.core.publisher.Flux<java.lang.String> empty) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(post19Invocation.apply(empty));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> post20(
                    reactor.core.publisher.Flux<java.lang.String> empty) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(post20Invocation.apply(empty));
                }
              }

              public static final class MetaTestHttpConnectorClass extends MetaClass<io.art.http.test.communicator.TestHttp.TestHttpConnector> {
                private final MetaTestHttpMethod testHttpMethod = register(new MetaTestHttpMethod());

                private MetaTestHttpConnectorClass() {
                  super(metaType(io.art.http.test.communicator.TestHttp.TestHttpConnector.class));
                }

                public MetaTestHttpMethod testHttpMethod() {
                  return testHttpMethod;
                }

                @Override
                public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  return new MetaTestHttpConnectorProxy(invocations);
                }

                public static final class MetaTestHttpMethod extends InstanceMetaMethod<io.art.http.test.communicator.TestHttp.TestHttpConnector, io.art.http.test.communicator.TestHttp> {
                  private MetaTestHttpMethod() {
                    super("testHttp",metaType(io.art.http.test.communicator.TestHttp.class));
                  }

                  @Override
                  public Object invoke(
                      io.art.http.test.communicator.TestHttp.TestHttpConnector instance,
                      Object[] arguments) throws Throwable {
                    return instance.testHttp();
                  }

                  @Override
                  public Object invoke(
                      io.art.http.test.communicator.TestHttp.TestHttpConnector instance) throws
                      Throwable {
                    return instance.testHttp();
                  }
                }

                public class MetaTestHttpConnectorProxy extends MetaProxy implements io.art.http.test.communicator.TestHttp.TestHttpConnector {
                  private final Function<Object, Object> testHttpInvocation;

                  public MetaTestHttpConnectorProxy(
                      Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                    super(invocations);
                    testHttpInvocation = invocations.get(testHttpMethod);
                  }

                  @Override
                  public io.art.http.test.communicator.TestHttp testHttp() {
                    return (io.art.http.test.communicator.TestHttp)(testHttpInvocation.apply(null));
                  }
                }
              }
            }

            public static final class MetaTestWsClass extends MetaClass<io.art.http.test.communicator.TestWs> {
              private final MetaWs1Method ws1Method = register(new MetaWs1Method());

              private final MetaWs2Method ws2Method = register(new MetaWs2Method());

              private final MetaWs3Method ws3Method = register(new MetaWs3Method());

              private final MetaWs4Method ws4Method = register(new MetaWs4Method());

              private final MetaWs5Method ws5Method = register(new MetaWs5Method());

              private final MetaWs6Method ws6Method = register(new MetaWs6Method());

              private final MetaWs7Method ws7Method = register(new MetaWs7Method());

              private final MetaWs8Method ws8Method = register(new MetaWs8Method());

              private final MetaWs9Method ws9Method = register(new MetaWs9Method());

              private final MetaWs10Method ws10Method = register(new MetaWs10Method());

              private final MetaWs11Method ws11Method = register(new MetaWs11Method());

              private final MetaWs12Method ws12Method = register(new MetaWs12Method());

              private final MetaWs13Method ws13Method = register(new MetaWs13Method());

              private final MetaWs14Method ws14Method = register(new MetaWs14Method());

              private final MetaWs15Method ws15Method = register(new MetaWs15Method());

              private final MetaWs16Method ws16Method = register(new MetaWs16Method());

              private final MetaWs17Method ws17Method = register(new MetaWs17Method());

              private final MetaTestWsConnectorClass testWsConnectorClass = register(new MetaTestWsConnectorClass());

              private MetaTestWsClass() {
                super(metaType(io.art.http.test.communicator.TestWs.class));
              }

              public MetaWs1Method ws1Method() {
                return ws1Method;
              }

              public MetaWs2Method ws2Method() {
                return ws2Method;
              }

              public MetaWs3Method ws3Method() {
                return ws3Method;
              }

              public MetaWs4Method ws4Method() {
                return ws4Method;
              }

              public MetaWs5Method ws5Method() {
                return ws5Method;
              }

              public MetaWs6Method ws6Method() {
                return ws6Method;
              }

              public MetaWs7Method ws7Method() {
                return ws7Method;
              }

              public MetaWs8Method ws8Method() {
                return ws8Method;
              }

              public MetaWs9Method ws9Method() {
                return ws9Method;
              }

              public MetaWs10Method ws10Method() {
                return ws10Method;
              }

              public MetaWs11Method ws11Method() {
                return ws11Method;
              }

              public MetaWs12Method ws12Method() {
                return ws12Method;
              }

              public MetaWs13Method ws13Method() {
                return ws13Method;
              }

              public MetaWs14Method ws14Method() {
                return ws14Method;
              }

              public MetaWs15Method ws15Method() {
                return ws15Method;
              }

              public MetaWs16Method ws16Method() {
                return ws16Method;
              }

              public MetaWs17Method ws17Method() {
                return ws17Method;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestWsProxy(invocations);
              }

              public MetaTestWsConnectorClass testWsConnectorClass() {
                return testWsConnectorClass;
              }

              public static final class MetaWs1Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, Void> {
                private MetaWs1Method() {
                  super("ws1",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  instance.ws1();
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance) throws
                    Throwable {
                  instance.ws1();
                  return null;
                }
              }

              public static final class MetaWs2Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, java.lang.String> {
                private MetaWs2Method() {
                  super("ws2",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws2();
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance) throws
                    Throwable {
                  return instance.ws2();
                }
              }

              public static final class MetaWs3Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaWs3Method() {
                  super("ws3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws3();
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance) throws
                    Throwable {
                  return instance.ws3();
                }
              }

              public static final class MetaWs4Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaWs4Method() {
                  super("ws4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws4();
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance) throws
                    Throwable {
                  return instance.ws4();
                }
              }

              public static final class MetaWs5Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs5Method() {
                  super("ws5",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  instance.ws5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  instance.ws5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs6Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs6Method() {
                  super("ws6",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs7Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs7Method() {
                  super("ws7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs8Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs8Method() {
                  super("ws8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs9Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs9Method() {
                  super("ws9",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  instance.ws9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  instance.ws9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs10Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs10Method() {
                  super("ws10",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs11Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs11Method() {
                  super("ws11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs12Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs12Method() {
                  super("ws12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs13Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs13Method() {
                  super("ws13",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  instance.ws13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  instance.ws13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs14Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs14Method() {
                  super("ws14",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs15Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs15Method() {
                  super("ws15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs16Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs16Method() {
                  super("ws16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.ws16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs17Method extends InstanceMetaMethod<io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs17Method() {
                  super("ws17",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  instance.ws17((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  instance.ws17((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public class MetaTestWsProxy extends MetaProxy implements io.art.http.test.communicator.TestWs {
                private final Function<Object, Object> ws1Invocation;

                private final Function<Object, Object> ws2Invocation;

                private final Function<Object, Object> ws3Invocation;

                private final Function<Object, Object> ws4Invocation;

                private final Function<Object, Object> ws5Invocation;

                private final Function<Object, Object> ws6Invocation;

                private final Function<Object, Object> ws7Invocation;

                private final Function<Object, Object> ws8Invocation;

                private final Function<Object, Object> ws9Invocation;

                private final Function<Object, Object> ws10Invocation;

                private final Function<Object, Object> ws11Invocation;

                private final Function<Object, Object> ws12Invocation;

                private final Function<Object, Object> ws13Invocation;

                private final Function<Object, Object> ws14Invocation;

                private final Function<Object, Object> ws15Invocation;

                private final Function<Object, Object> ws16Invocation;

                private final Function<Object, Object> ws17Invocation;

                public MetaTestWsProxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  ws1Invocation = invocations.get(ws1Method);
                  ws2Invocation = invocations.get(ws2Method);
                  ws3Invocation = invocations.get(ws3Method);
                  ws4Invocation = invocations.get(ws4Method);
                  ws5Invocation = invocations.get(ws5Method);
                  ws6Invocation = invocations.get(ws6Method);
                  ws7Invocation = invocations.get(ws7Method);
                  ws8Invocation = invocations.get(ws8Method);
                  ws9Invocation = invocations.get(ws9Method);
                  ws10Invocation = invocations.get(ws10Method);
                  ws11Invocation = invocations.get(ws11Method);
                  ws12Invocation = invocations.get(ws12Method);
                  ws13Invocation = invocations.get(ws13Method);
                  ws14Invocation = invocations.get(ws14Method);
                  ws15Invocation = invocations.get(ws15Method);
                  ws16Invocation = invocations.get(ws16Method);
                  ws17Invocation = invocations.get(ws17Method);
                }

                @Override
                public void ws1() {
                  ws1Invocation.apply(null);
                }

                @Override
                public java.lang.String ws2() {
                  return (java.lang.String)(ws2Invocation.apply(null));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> ws3() {
                  return (reactor.core.publisher.Mono<java.lang.String>)(ws3Invocation.apply(null));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> ws4() {
                  return (reactor.core.publisher.Flux<java.lang.String>)(ws4Invocation.apply(null));
                }

                @Override
                public void ws5(java.lang.String input) {
                  ws5Invocation.apply(input);
                }

                @Override
                public java.lang.String ws6(java.lang.String input) {
                  return (java.lang.String)(ws6Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> ws7(java.lang.String input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(ws7Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> ws8(java.lang.String input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(ws8Invocation.apply(input));
                }

                @Override
                public void ws9(reactor.core.publisher.Mono<java.lang.String> input) {
                  ws9Invocation.apply(input);
                }

                @Override
                public java.lang.String ws10(reactor.core.publisher.Mono<java.lang.String> input) {
                  return (java.lang.String)(ws10Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> ws11(
                    reactor.core.publisher.Mono<java.lang.String> input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(ws11Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> ws12(
                    reactor.core.publisher.Mono<java.lang.String> input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(ws12Invocation.apply(input));
                }

                @Override
                public void ws13(reactor.core.publisher.Flux<java.lang.String> input) {
                  ws13Invocation.apply(input);
                }

                @Override
                public java.lang.String ws14(reactor.core.publisher.Flux<java.lang.String> input) {
                  return (java.lang.String)(ws14Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Mono<java.lang.String> ws15(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (reactor.core.publisher.Mono<java.lang.String>)(ws15Invocation.apply(input));
                }

                @Override
                public reactor.core.publisher.Flux<java.lang.String> ws16(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(ws16Invocation.apply(input));
                }

                @Override
                public void ws17(reactor.core.publisher.Flux<java.lang.String> input) {
                  ws17Invocation.apply(input);
                }
              }

              public static final class MetaTestWsConnectorClass extends MetaClass<io.art.http.test.communicator.TestWs.TestWsConnector> {
                private final MetaTestWsMethod testWsMethod = register(new MetaTestWsMethod());

                private MetaTestWsConnectorClass() {
                  super(metaType(io.art.http.test.communicator.TestWs.TestWsConnector.class));
                }

                public MetaTestWsMethod testWsMethod() {
                  return testWsMethod;
                }

                @Override
                public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  return new MetaTestWsConnectorProxy(invocations);
                }

                public static final class MetaTestWsMethod extends InstanceMetaMethod<io.art.http.test.communicator.TestWs.TestWsConnector, io.art.http.test.communicator.TestWs> {
                  private MetaTestWsMethod() {
                    super("testWs",metaType(io.art.http.test.communicator.TestWs.class));
                  }

                  @Override
                  public Object invoke(
                      io.art.http.test.communicator.TestWs.TestWsConnector instance,
                      Object[] arguments) throws Throwable {
                    return instance.testWs();
                  }

                  @Override
                  public Object invoke(
                      io.art.http.test.communicator.TestWs.TestWsConnector instance) throws
                      Throwable {
                    return instance.testWs();
                  }
                }

                public class MetaTestWsConnectorProxy extends MetaProxy implements io.art.http.test.communicator.TestWs.TestWsConnector {
                  private final Function<Object, Object> testWsInvocation;

                  public MetaTestWsConnectorProxy(
                      Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                    super(invocations);
                    testWsInvocation = invocations.get(testWsMethod);
                  }

                  @Override
                  public io.art.http.test.communicator.TestWs testWs() {
                    return (io.art.http.test.communicator.TestWs)(testWsInvocation.apply(null));
                  }
                }
              }
            }
          }

          public static final class MetaServicePackage extends MetaPackage {
            private final MetaTestHttpServiceClass testHttpServiceClass = register(new MetaTestHttpServiceClass());

            private final MetaTestWsServiceClass testWsServiceClass = register(new MetaTestWsServiceClass());

            private MetaServicePackage() {
              super("service");
            }

            public MetaTestHttpServiceClass testHttpServiceClass() {
              return testHttpServiceClass;
            }

            public MetaTestWsServiceClass testWsServiceClass() {
              return testWsServiceClass;
            }

            public static final class MetaTestHttpServiceClass extends MetaClass<io.art.http.test.service.TestHttpService> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaPost1Method post1Method = register(new MetaPost1Method());

              private final MetaPost2Method post2Method = register(new MetaPost2Method());

              private final MetaPost3Method post3Method = register(new MetaPost3Method());

              private final MetaPost4Method post4Method = register(new MetaPost4Method());

              private final MetaPost5Method post5Method = register(new MetaPost5Method());

              private final MetaPost6Method post6Method = register(new MetaPost6Method());

              private final MetaPost7Method post7Method = register(new MetaPost7Method());

              private final MetaPost8Method post8Method = register(new MetaPost8Method());

              private final MetaPost9Method post9Method = register(new MetaPost9Method());

              private final MetaPost10Method post10Method = register(new MetaPost10Method());

              private final MetaPost11Method post11Method = register(new MetaPost11Method());

              private final MetaPost12Method post12Method = register(new MetaPost12Method());

              private final MetaPost13Method post13Method = register(new MetaPost13Method());

              private final MetaPost14Method post14Method = register(new MetaPost14Method());

              private final MetaPost15Method post15Method = register(new MetaPost15Method());

              private final MetaPost16Method post16Method = register(new MetaPost16Method());

              private final MetaPost17Method post17Method = register(new MetaPost17Method());

              private final MetaPost18Method post18Method = register(new MetaPost18Method());

              private final MetaPost19Method post19Method = register(new MetaPost19Method());

              private final MetaPost20Method post20Method = register(new MetaPost20Method());

              private MetaTestHttpServiceClass() {
                super(metaType(io.art.http.test.service.TestHttpService.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaPost1Method post1Method() {
                return post1Method;
              }

              public MetaPost2Method post2Method() {
                return post2Method;
              }

              public MetaPost3Method post3Method() {
                return post3Method;
              }

              public MetaPost4Method post4Method() {
                return post4Method;
              }

              public MetaPost5Method post5Method() {
                return post5Method;
              }

              public MetaPost6Method post6Method() {
                return post6Method;
              }

              public MetaPost7Method post7Method() {
                return post7Method;
              }

              public MetaPost8Method post8Method() {
                return post8Method;
              }

              public MetaPost9Method post9Method() {
                return post9Method;
              }

              public MetaPost10Method post10Method() {
                return post10Method;
              }

              public MetaPost11Method post11Method() {
                return post11Method;
              }

              public MetaPost12Method post12Method() {
                return post12Method;
              }

              public MetaPost13Method post13Method() {
                return post13Method;
              }

              public MetaPost14Method post14Method() {
                return post14Method;
              }

              public MetaPost15Method post15Method() {
                return post15Method;
              }

              public MetaPost16Method post16Method() {
                return post16Method;
              }

              public MetaPost17Method post17Method() {
                return post17Method;
              }

              public MetaPost18Method post18Method() {
                return post18Method;
              }

              public MetaPost19Method post19Method() {
                return post19Method;
              }

              public MetaPost20Method post20Method() {
                return post20Method;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.http.test.service.TestHttpService> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.http.test.service.TestHttpService.class));
                }

                @Override
                public io.art.http.test.service.TestHttpService invoke(Object[] arguments) throws
                    Throwable {
                  return new io.art.http.test.service.TestHttpService();
                }

                @Override
                public io.art.http.test.service.TestHttpService invoke() throws Throwable {
                  return new io.art.http.test.service.TestHttpService();
                }
              }

              public static final class MetaPost1Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, Void> {
                private MetaPost1Method() {
                  super("post1",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  instance.post1();
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance) throws
                    Throwable {
                  instance.post1();
                  return null;
                }
              }

              public static final class MetaPost2Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, java.lang.String> {
                private MetaPost2Method() {
                  super("post2",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post2();
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance) throws
                    Throwable {
                  return instance.post2();
                }
              }

              public static final class MetaPost3Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaPost3Method() {
                  super("post3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post3();
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance) throws
                    Throwable {
                  return instance.post3();
                }
              }

              public static final class MetaPost4Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaPost4Method() {
                  super("post4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post4();
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance) throws
                    Throwable {
                  return instance.post4();
                }
              }

              public static final class MetaPost5Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost5Method() {
                  super("post5",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  instance.post5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  instance.post5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost6Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost6Method() {
                  super("post6",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost7Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost7Method() {
                  super("post7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost8Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost8Method() {
                  super("post8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaPost9Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost9Method() {
                  super("post9",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  instance.post9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  instance.post9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost10Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost10Method() {
                  super("post10",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost11Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost11Method() {
                  super("post11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost12Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost12Method() {
                  super("post12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost13Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost13Method() {
                  super("post13",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  instance.post13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  instance.post13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost14Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost14Method() {
                  super("post14",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost15Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost15Method() {
                  super("post15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost16Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost16Method() {
                  super("post16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaPost17Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost17Method() {
                  super("post17",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  instance.post17((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  instance.post17((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }

              public static final class MetaPost18Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost18Method() {
                  super("post18",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post18((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post18((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }

              public static final class MetaPost19Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost19Method() {
                  super("post19",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post19((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post19((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }

              public static final class MetaPost20Method extends InstanceMetaMethod<io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost20Method() {
                  super("post20",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object[] arguments) throws Throwable {
                  return instance.post20((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestHttpService instance,
                    Object argument) throws Throwable {
                  return instance.post20((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter(
                    ) {
                  return emptyParameter;
                }
              }
            }

            public static final class MetaTestWsServiceClass extends MetaClass<io.art.http.test.service.TestWsService> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaWs1Method ws1Method = register(new MetaWs1Method());

              private final MetaWs2Method ws2Method = register(new MetaWs2Method());

              private final MetaWs3Method ws3Method = register(new MetaWs3Method());

              private final MetaWs4Method ws4Method = register(new MetaWs4Method());

              private final MetaWs5Method ws5Method = register(new MetaWs5Method());

              private final MetaWs6Method ws6Method = register(new MetaWs6Method());

              private final MetaWs7Method ws7Method = register(new MetaWs7Method());

              private final MetaWs8Method ws8Method = register(new MetaWs8Method());

              private final MetaWs9Method ws9Method = register(new MetaWs9Method());

              private final MetaWs10Method ws10Method = register(new MetaWs10Method());

              private final MetaWs11Method ws11Method = register(new MetaWs11Method());

              private final MetaWs12Method ws12Method = register(new MetaWs12Method());

              private final MetaWs13Method ws13Method = register(new MetaWs13Method());

              private final MetaWs14Method ws14Method = register(new MetaWs14Method());

              private final MetaWs15Method ws15Method = register(new MetaWs15Method());

              private final MetaWs16Method ws16Method = register(new MetaWs16Method());

              private final MetaWs17Method ws17Method = register(new MetaWs17Method());

              private MetaTestWsServiceClass() {
                super(metaType(io.art.http.test.service.TestWsService.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaWs1Method ws1Method() {
                return ws1Method;
              }

              public MetaWs2Method ws2Method() {
                return ws2Method;
              }

              public MetaWs3Method ws3Method() {
                return ws3Method;
              }

              public MetaWs4Method ws4Method() {
                return ws4Method;
              }

              public MetaWs5Method ws5Method() {
                return ws5Method;
              }

              public MetaWs6Method ws6Method() {
                return ws6Method;
              }

              public MetaWs7Method ws7Method() {
                return ws7Method;
              }

              public MetaWs8Method ws8Method() {
                return ws8Method;
              }

              public MetaWs9Method ws9Method() {
                return ws9Method;
              }

              public MetaWs10Method ws10Method() {
                return ws10Method;
              }

              public MetaWs11Method ws11Method() {
                return ws11Method;
              }

              public MetaWs12Method ws12Method() {
                return ws12Method;
              }

              public MetaWs13Method ws13Method() {
                return ws13Method;
              }

              public MetaWs14Method ws14Method() {
                return ws14Method;
              }

              public MetaWs15Method ws15Method() {
                return ws15Method;
              }

              public MetaWs16Method ws16Method() {
                return ws16Method;
              }

              public MetaWs17Method ws17Method() {
                return ws17Method;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.http.test.service.TestWsService> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.http.test.service.TestWsService.class));
                }

                @Override
                public io.art.http.test.service.TestWsService invoke(Object[] arguments) throws
                    Throwable {
                  return new io.art.http.test.service.TestWsService();
                }

                @Override
                public io.art.http.test.service.TestWsService invoke() throws Throwable {
                  return new io.art.http.test.service.TestWsService();
                }
              }

              public static final class MetaWs1Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, Void> {
                private MetaWs1Method() {
                  super("ws1",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  instance.ws1();
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance) throws
                    Throwable {
                  instance.ws1();
                  return null;
                }
              }

              public static final class MetaWs2Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, java.lang.String> {
                private MetaWs2Method() {
                  super("ws2",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws2();
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance) throws
                    Throwable {
                  return instance.ws2();
                }
              }

              public static final class MetaWs3Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaWs3Method() {
                  super("ws3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws3();
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance) throws
                    Throwable {
                  return instance.ws3();
                }
              }

              public static final class MetaWs4Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaWs4Method() {
                  super("ws4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws4();
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance) throws
                    Throwable {
                  return instance.ws4();
                }
              }

              public static final class MetaWs5Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs5Method() {
                  super("ws5",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  instance.ws5((java.lang.String)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  instance.ws5((java.lang.String)(argument));
                  return null;
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs6Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs6Method() {
                  super("ws6",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws6((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws6((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs7Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs7Method() {
                  super("ws7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws7((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws7((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs8Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs8Method() {
                  super("ws8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws8((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws8((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> inputParameter() {
                  return inputParameter;
                }
              }

              public static final class MetaWs9Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs9Method() {
                  super("ws9",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  instance.ws9((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  instance.ws9((reactor.core.publisher.Mono)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs10Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs10Method() {
                  super("ws10",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws10((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws10((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs11Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs11Method() {
                  super("ws11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws11((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws11((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs12Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs12Method() {
                  super("ws12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws12((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws12((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs13Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs13Method() {
                  super("ws13",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  instance.ws13((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  instance.ws13((reactor.core.publisher.Flux)(argument));
                  return null;
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs14Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs14Method() {
                  super("ws14",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws14((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws14((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs15Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs15Method() {
                  super("ws15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws15((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws15((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs16Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs16Method() {
                  super("ws16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.ws16((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.ws16((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter(
                    ) {
                  return inputParameter;
                }
              }

              public static final class MetaWs17Method extends InstanceMetaMethod<io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs17Method() {
                  super("ws17",metaType(Void.class));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  instance.ws17((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                  return null;
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  instance.ws17((reactor.core.publisher.Flux)(argument));
                  return null;
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
