package io.art.http.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.core.property.LazyProperty;
import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaField;
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
          private final MetaHttpDefaultTestClass httpDefaultTestClass = register(new MetaHttpDefaultTestClass());

          private final MetaWsTestClass wsTestClass = register(new MetaWsTestClass());

          private final MetaCommunicatorPackage communicatorPackage = register(new MetaCommunicatorPackage());

          private final MetaServicePackage servicePackage = register(new MetaServicePackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaHttpDefaultTestClass httpDefaultTestClass() {
            return httpDefaultTestClass;
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

          public static final class MetaHttpDefaultTestClass extends MetaClass<io.art.http.test.HttpDefaultTest> {
            private static final LazyProperty<MetaHttpDefaultTestClass> self = MetaClass.self(io.art.http.test.HttpDefaultTest.class);

            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

            private final MetaField<MetaHttpDefaultTestClass, java.nio.file.Path> testFileField = register(new MetaField<>("testFile",metaType(java.nio.file.Path.class),false,this));

            private final MetaField<MetaHttpDefaultTestClass, java.nio.file.Path> downloadedFileField = register(new MetaField<>("downloadedFile",metaType(java.nio.file.Path.class),false,this));

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod(this));

            private final MetaCleanupMethod cleanupMethod = register(new MetaCleanupMethod(this));

            private final MetaTestHttpDefaultCommunicatorMethod testHttpDefaultCommunicatorMethod = register(new MetaTestHttpDefaultCommunicatorMethod(this));

            private MetaHttpDefaultTestClass() {
              super(metaType(io.art.http.test.HttpDefaultTest.class));
            }

            public static MetaHttpDefaultTestClass httpDefaultTest() {
              return self.get();
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<MetaHttpDefaultTestClass, java.nio.file.Path> testFileField() {
              return testFileField;
            }

            public MetaField<MetaHttpDefaultTestClass, java.nio.file.Path> downloadedFileField() {
              return downloadedFileField;
            }

            public MetaSetupMethod setupMethod() {
              return setupMethod;
            }

            public MetaCleanupMethod cleanupMethod() {
              return cleanupMethod;
            }

            public MetaTestHttpDefaultCommunicatorMethod testHttpDefaultCommunicatorMethod() {
              return testHttpDefaultCommunicatorMethod;
            }

            public final class MetaConstructorConstructor extends MetaConstructor<MetaHttpDefaultTestClass, io.art.http.test.HttpDefaultTest> {
              private MetaConstructorConstructor(MetaHttpDefaultTestClass owner) {
                super(metaType(io.art.http.test.HttpDefaultTest.class),owner);
              }

              @Override
              public io.art.http.test.HttpDefaultTest invoke(Object[] arguments) throws Throwable {
                return new io.art.http.test.HttpDefaultTest();
              }

              @Override
              public io.art.http.test.HttpDefaultTest invoke() throws Throwable {
                return new io.art.http.test.HttpDefaultTest();
              }
            }

            public final class MetaSetupMethod extends StaticMetaMethod<MetaHttpDefaultTestClass, Void> {
              private MetaSetupMethod(MetaHttpDefaultTestClass owner) {
                super("setup",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.http.test.HttpDefaultTest.setup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.http.test.HttpDefaultTest.setup();
                return null;
              }
            }

            public final class MetaCleanupMethod extends StaticMetaMethod<MetaHttpDefaultTestClass, Void> {
              private MetaCleanupMethod(MetaHttpDefaultTestClass owner) {
                super("cleanup",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                io.art.http.test.HttpDefaultTest.cleanup();
                return null;
              }

              @Override
              public Object invoke() throws Throwable {
                io.art.http.test.HttpDefaultTest.cleanup();
                return null;
              }
            }

            public final class MetaTestHttpDefaultCommunicatorMethod extends InstanceMetaMethod<MetaHttpDefaultTestClass, io.art.http.test.HttpDefaultTest, Void> {
              private MetaTestHttpDefaultCommunicatorMethod(MetaHttpDefaultTestClass owner) {
                super("testHttpDefaultCommunicator",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(io.art.http.test.HttpDefaultTest instance, Object[] arguments)
                  throws Throwable {
                instance.testHttpDefaultCommunicator();
                return null;
              }

              @Override
              public Object invoke(io.art.http.test.HttpDefaultTest instance) throws Throwable {
                instance.testHttpDefaultCommunicator();
                return null;
              }
            }
          }

          public static final class MetaWsTestClass extends MetaClass<io.art.http.test.WsTest> {
            private static final LazyProperty<MetaWsTestClass> self = MetaClass.self(io.art.http.test.WsTest.class);

            private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

            private final MetaSetupMethod setupMethod = register(new MetaSetupMethod(this));

            private final MetaCleanupMethod cleanupMethod = register(new MetaCleanupMethod(this));

            private final MetaTestWsMethod testWsMethod = register(new MetaTestWsMethod(this));

            private MetaWsTestClass() {
              super(metaType(io.art.http.test.WsTest.class));
            }

            public static MetaWsTestClass wsTest() {
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

            public MetaTestWsMethod testWsMethod() {
              return testWsMethod;
            }

            public final class MetaConstructorConstructor extends MetaConstructor<MetaWsTestClass, io.art.http.test.WsTest> {
              private MetaConstructorConstructor(MetaWsTestClass owner) {
                super(metaType(io.art.http.test.WsTest.class),owner);
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

            public final class MetaSetupMethod extends StaticMetaMethod<MetaWsTestClass, Void> {
              private MetaSetupMethod(MetaWsTestClass owner) {
                super("setup",metaType(Void.class),owner);
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

            public final class MetaCleanupMethod extends StaticMetaMethod<MetaWsTestClass, Void> {
              private MetaCleanupMethod(MetaWsTestClass owner) {
                super("cleanup",metaType(Void.class),owner);
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

            public final class MetaTestWsMethod extends InstanceMetaMethod<MetaWsTestClass, io.art.http.test.WsTest, Void> {
              private MetaTestWsMethod(MetaWsTestClass owner) {
                super("testWs",metaType(Void.class),owner);
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
              private static final LazyProperty<MetaTestHttpClass> self = MetaClass.self(io.art.http.test.communicator.TestHttp.class);

              private final MetaPost1Method post1Method = register(new MetaPost1Method(this));

              private final MetaPost2Method post2Method = register(new MetaPost2Method(this));

              private final MetaPost3Method post3Method = register(new MetaPost3Method(this));

              private final MetaPost4Method post4Method = register(new MetaPost4Method(this));

              private final MetaPost5Method post5Method = register(new MetaPost5Method(this));

              private final MetaPost6Method post6Method = register(new MetaPost6Method(this));

              private final MetaPost7Method post7Method = register(new MetaPost7Method(this));

              private final MetaPost8Method post8Method = register(new MetaPost8Method(this));

              private final MetaPost9Method post9Method = register(new MetaPost9Method(this));

              private final MetaPost10Method post10Method = register(new MetaPost10Method(this));

              private final MetaPost11Method post11Method = register(new MetaPost11Method(this));

              private final MetaPost12Method post12Method = register(new MetaPost12Method(this));

              private final MetaPost13Method post13Method = register(new MetaPost13Method(this));

              private final MetaPost14Method post14Method = register(new MetaPost14Method(this));

              private final MetaPost15Method post15Method = register(new MetaPost15Method(this));

              private final MetaPost16Method post16Method = register(new MetaPost16Method(this));

              private final MetaPost17Method post17Method = register(new MetaPost17Method(this));

              private final MetaPost18Method post18Method = register(new MetaPost18Method(this));

              private final MetaPost19Method post19Method = register(new MetaPost19Method(this));

              private final MetaPost20Method post20Method = register(new MetaPost20Method(this));

              private final MetaGetFileMethod getFileMethod = register(new MetaGetFileMethod(this));

              private final MetaTestHttpConnectorClass testHttpConnectorClass = register(new MetaTestHttpConnectorClass());

              private MetaTestHttpClass() {
                super(metaType(io.art.http.test.communicator.TestHttp.class));
              }

              public static MetaTestHttpClass testHttp() {
                return self.get();
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

              public MetaGetFileMethod getFileMethod() {
                return getFileMethod;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
                return new MetaTestHttpProxy(invocations);
              }

              public MetaTestHttpConnectorClass testHttpConnectorClass() {
                return testHttpConnectorClass;
              }

              public final class MetaPost1Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, Void> {
                private MetaPost1Method(MetaTestHttpClass owner) {
                  super("post1",metaType(Void.class),owner);
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

              public final class MetaPost2Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, java.lang.String> {
                private MetaPost2Method(MetaTestHttpClass owner) {
                  super("post2",metaType(java.lang.String.class),owner);
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

              public final class MetaPost3Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaPost3Method(MetaTestHttpClass owner) {
                  super("post3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost4Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaPost4Method(MetaTestHttpClass owner) {
                  super("post4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost5Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost5Method(MetaTestHttpClass owner) {
                  super("post5",metaType(Void.class),owner);
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

              public final class MetaPost6Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost6Method(MetaTestHttpClass owner) {
                  super("post6",metaType(java.lang.String.class),owner);
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

              public final class MetaPost7Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost7Method(MetaTestHttpClass owner) {
                  super("post7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost8Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost8Method(MetaTestHttpClass owner) {
                  super("post8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost9Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost9Method(MetaTestHttpClass owner) {
                  super("post9",metaType(Void.class),owner);
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

              public final class MetaPost10Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost10Method(MetaTestHttpClass owner) {
                  super("post10",metaType(java.lang.String.class),owner);
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

              public final class MetaPost11Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost11Method(MetaTestHttpClass owner) {
                  super("post11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost12Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost12Method(MetaTestHttpClass owner) {
                  super("post12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost13Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost13Method(MetaTestHttpClass owner) {
                  super("post13",metaType(Void.class),owner);
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

              public final class MetaPost14Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost14Method(MetaTestHttpClass owner) {
                  super("post14",metaType(java.lang.String.class),owner);
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

              public final class MetaPost15Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost15Method(MetaTestHttpClass owner) {
                  super("post15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost16Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost16Method(MetaTestHttpClass owner) {
                  super("post16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost17Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost17Method(MetaTestHttpClass owner) {
                  super("post17",metaType(Void.class),owner);
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

              public final class MetaPost18Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost18Method(MetaTestHttpClass owner) {
                  super("post18",metaType(java.lang.String.class),owner);
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

              public final class MetaPost19Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost19Method(MetaTestHttpClass owner) {
                  super("post19",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost20Method extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost20Method(MetaTestHttpClass owner) {
                  super("post20",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaGetFileMethod extends InstanceMetaMethod<MetaTestHttpClass, io.art.http.test.communicator.TestHttp, java.lang.String> {
                private MetaGetFileMethod(MetaTestHttpClass owner) {
                  super("getFile",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance,
                    Object[] arguments) throws Throwable {
                  return instance.getFile();
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestHttp instance) throws
                    Throwable {
                  return instance.getFile();
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

                private final Function<Object, Object> getFileInvocation;

                public MetaTestHttpProxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
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
                  getFileInvocation = invocations.get(getFileMethod);
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

                @Override
                public java.lang.String getFile() {
                  return (java.lang.String)(getFileInvocation.apply(null));
                }
              }

              public static final class MetaTestHttpConnectorClass extends MetaClass<io.art.http.test.communicator.TestHttp.TestHttpConnector> {
                private static final LazyProperty<MetaTestHttpConnectorClass> self = MetaClass.self(io.art.http.test.communicator.TestHttp.TestHttpConnector.class);

                private final MetaTestHttpMethod testHttpMethod = register(new MetaTestHttpMethod(this));

                private MetaTestHttpConnectorClass() {
                  super(metaType(io.art.http.test.communicator.TestHttp.TestHttpConnector.class));
                }

                public static MetaTestHttpConnectorClass testHttpConnector() {
                  return self.get();
                }

                public MetaTestHttpMethod testHttpMethod() {
                  return testHttpMethod;
                }

                @Override
                public MetaProxy proxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
                  return new MetaTestHttpConnectorProxy(invocations);
                }

                public final class MetaTestHttpMethod extends InstanceMetaMethod<MetaTestHttpConnectorClass, io.art.http.test.communicator.TestHttp.TestHttpConnector, io.art.http.test.communicator.TestHttp> {
                  private MetaTestHttpMethod(MetaTestHttpConnectorClass owner) {
                    super("testHttp",metaType(io.art.http.test.communicator.TestHttp.class),owner);
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
                      Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
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
              private static final LazyProperty<MetaTestWsClass> self = MetaClass.self(io.art.http.test.communicator.TestWs.class);

              private final MetaWs1Method ws1Method = register(new MetaWs1Method(this));

              private final MetaWs2Method ws2Method = register(new MetaWs2Method(this));

              private final MetaWs3Method ws3Method = register(new MetaWs3Method(this));

              private final MetaWs4Method ws4Method = register(new MetaWs4Method(this));

              private final MetaWs5Method ws5Method = register(new MetaWs5Method(this));

              private final MetaWs6Method ws6Method = register(new MetaWs6Method(this));

              private final MetaWs7Method ws7Method = register(new MetaWs7Method(this));

              private final MetaWs8Method ws8Method = register(new MetaWs8Method(this));

              private final MetaWs9Method ws9Method = register(new MetaWs9Method(this));

              private final MetaWs10Method ws10Method = register(new MetaWs10Method(this));

              private final MetaWs11Method ws11Method = register(new MetaWs11Method(this));

              private final MetaWs12Method ws12Method = register(new MetaWs12Method(this));

              private final MetaWs13Method ws13Method = register(new MetaWs13Method(this));

              private final MetaWs14Method ws14Method = register(new MetaWs14Method(this));

              private final MetaWs15Method ws15Method = register(new MetaWs15Method(this));

              private final MetaWs16Method ws16Method = register(new MetaWs16Method(this));

              private final MetaWs17Method ws17Method = register(new MetaWs17Method(this));

              private final MetaWsEchoMethod wsEchoMethod = register(new MetaWsEchoMethod(this));

              private final MetaTestWsConnectorClass testWsConnectorClass = register(new MetaTestWsConnectorClass());

              private MetaTestWsClass() {
                super(metaType(io.art.http.test.communicator.TestWs.class));
              }

              public static MetaTestWsClass testWs() {
                return self.get();
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

              public MetaWsEchoMethod wsEchoMethod() {
                return wsEchoMethod;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
                return new MetaTestWsProxy(invocations);
              }

              public MetaTestWsConnectorClass testWsConnectorClass() {
                return testWsConnectorClass;
              }

              public final class MetaWs1Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, Void> {
                private MetaWs1Method(MetaTestWsClass owner) {
                  super("ws1",metaType(Void.class),owner);
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

              public final class MetaWs2Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, java.lang.String> {
                private MetaWs2Method(MetaTestWsClass owner) {
                  super("ws2",metaType(java.lang.String.class),owner);
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

              public final class MetaWs3Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaWs3Method(MetaTestWsClass owner) {
                  super("ws3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs4Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaWs4Method(MetaTestWsClass owner) {
                  super("ws4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs5Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs5Method(MetaTestWsClass owner) {
                  super("ws5",metaType(Void.class),owner);
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

              public final class MetaWs6Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs6Method(MetaTestWsClass owner) {
                  super("ws6",metaType(java.lang.String.class),owner);
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

              public final class MetaWs7Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs7Method(MetaTestWsClass owner) {
                  super("ws7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs8Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs8Method(MetaTestWsClass owner) {
                  super("ws8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs9Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs9Method(MetaTestWsClass owner) {
                  super("ws9",metaType(Void.class),owner);
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

              public final class MetaWs10Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs10Method(MetaTestWsClass owner) {
                  super("ws10",metaType(java.lang.String.class),owner);
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

              public final class MetaWs11Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs11Method(MetaTestWsClass owner) {
                  super("ws11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs12Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs12Method(MetaTestWsClass owner) {
                  super("ws12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs13Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs13Method(MetaTestWsClass owner) {
                  super("ws13",metaType(Void.class),owner);
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

              public final class MetaWs14Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs14Method(MetaTestWsClass owner) {
                  super("ws14",metaType(java.lang.String.class),owner);
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

              public final class MetaWs15Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs15Method(MetaTestWsClass owner) {
                  super("ws15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs16Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs16Method(MetaTestWsClass owner) {
                  super("ws16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs17Method extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs17Method(MetaTestWsClass owner) {
                  super("ws17",metaType(Void.class),owner);
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

              public final class MetaWsEchoMethod extends InstanceMetaMethod<MetaTestWsClass, io.art.http.test.communicator.TestWs, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWsEchoMethod(MetaTestWsClass owner) {
                  super("wsEcho",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance,
                    Object[] arguments) throws Throwable {
                  return instance.wsEcho((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.communicator.TestWs instance, Object argument)
                    throws Throwable {
                  return instance.wsEcho((reactor.core.publisher.Flux)(argument));
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

                private final Function<Object, Object> wsEchoInvocation;

                public MetaTestWsProxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
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
                  wsEchoInvocation = invocations.get(wsEchoMethod);
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

                @Override
                public reactor.core.publisher.Flux<java.lang.String> wsEcho(
                    reactor.core.publisher.Flux<java.lang.String> input) {
                  return (reactor.core.publisher.Flux<java.lang.String>)(wsEchoInvocation.apply(input));
                }
              }

              public static final class MetaTestWsConnectorClass extends MetaClass<io.art.http.test.communicator.TestWs.TestWsConnector> {
                private static final LazyProperty<MetaTestWsConnectorClass> self = MetaClass.self(io.art.http.test.communicator.TestWs.TestWsConnector.class);

                private final MetaTestWsMethod testWsMethod = register(new MetaTestWsMethod(this));

                private MetaTestWsConnectorClass() {
                  super(metaType(io.art.http.test.communicator.TestWs.TestWsConnector.class));
                }

                public static MetaTestWsConnectorClass testWsConnector() {
                  return self.get();
                }

                public MetaTestWsMethod testWsMethod() {
                  return testWsMethod;
                }

                @Override
                public MetaProxy proxy(
                    Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
                  return new MetaTestWsConnectorProxy(invocations);
                }

                public final class MetaTestWsMethod extends InstanceMetaMethod<MetaTestWsConnectorClass, io.art.http.test.communicator.TestWs.TestWsConnector, io.art.http.test.communicator.TestWs> {
                  private MetaTestWsMethod(MetaTestWsConnectorClass owner) {
                    super("testWs",metaType(io.art.http.test.communicator.TestWs.class),owner);
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
                      Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
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
              private static final LazyProperty<MetaTestHttpServiceClass> self = MetaClass.self(io.art.http.test.service.TestHttpService.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaPost1Method post1Method = register(new MetaPost1Method(this));

              private final MetaPost2Method post2Method = register(new MetaPost2Method(this));

              private final MetaPost3Method post3Method = register(new MetaPost3Method(this));

              private final MetaPost4Method post4Method = register(new MetaPost4Method(this));

              private final MetaPost5Method post5Method = register(new MetaPost5Method(this));

              private final MetaPost6Method post6Method = register(new MetaPost6Method(this));

              private final MetaPost7Method post7Method = register(new MetaPost7Method(this));

              private final MetaPost8Method post8Method = register(new MetaPost8Method(this));

              private final MetaPost9Method post9Method = register(new MetaPost9Method(this));

              private final MetaPost10Method post10Method = register(new MetaPost10Method(this));

              private final MetaPost11Method post11Method = register(new MetaPost11Method(this));

              private final MetaPost12Method post12Method = register(new MetaPost12Method(this));

              private final MetaPost13Method post13Method = register(new MetaPost13Method(this));

              private final MetaPost14Method post14Method = register(new MetaPost14Method(this));

              private final MetaPost15Method post15Method = register(new MetaPost15Method(this));

              private final MetaPost16Method post16Method = register(new MetaPost16Method(this));

              private final MetaPost17Method post17Method = register(new MetaPost17Method(this));

              private final MetaPost18Method post18Method = register(new MetaPost18Method(this));

              private final MetaPost19Method post19Method = register(new MetaPost19Method(this));

              private final MetaPost20Method post20Method = register(new MetaPost20Method(this));

              private MetaTestHttpServiceClass() {
                super(metaType(io.art.http.test.service.TestHttpService.class));
              }

              public static MetaTestHttpServiceClass testHttpService() {
                return self.get();
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

              public final class MetaConstructorConstructor extends MetaConstructor<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService> {
                private MetaConstructorConstructor(MetaTestHttpServiceClass owner) {
                  super(metaType(io.art.http.test.service.TestHttpService.class),owner);
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

              public final class MetaPost1Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, Void> {
                private MetaPost1Method(MetaTestHttpServiceClass owner) {
                  super("post1",metaType(Void.class),owner);
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

              public final class MetaPost2Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, java.lang.String> {
                private MetaPost2Method(MetaTestHttpServiceClass owner) {
                  super("post2",metaType(java.lang.String.class),owner);
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

              public final class MetaPost3Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaPost3Method(MetaTestHttpServiceClass owner) {
                  super("post3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost4Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaPost4Method(MetaTestHttpServiceClass owner) {
                  super("post4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost5Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost5Method(MetaTestHttpServiceClass owner) {
                  super("post5",metaType(Void.class),owner);
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

              public final class MetaPost6Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost6Method(MetaTestHttpServiceClass owner) {
                  super("post6",metaType(java.lang.String.class),owner);
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

              public final class MetaPost7Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost7Method(MetaTestHttpServiceClass owner) {
                  super("post7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost8Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaPost8Method(MetaTestHttpServiceClass owner) {
                  super("post8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost9Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost9Method(MetaTestHttpServiceClass owner) {
                  super("post9",metaType(Void.class),owner);
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

              public final class MetaPost10Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost10Method(MetaTestHttpServiceClass owner) {
                  super("post10",metaType(java.lang.String.class),owner);
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

              public final class MetaPost11Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost11Method(MetaTestHttpServiceClass owner) {
                  super("post11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost12Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaPost12Method(MetaTestHttpServiceClass owner) {
                  super("post12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost13Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost13Method(MetaTestHttpServiceClass owner) {
                  super("post13",metaType(Void.class),owner);
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

              public final class MetaPost14Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost14Method(MetaTestHttpServiceClass owner) {
                  super("post14",metaType(java.lang.String.class),owner);
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

              public final class MetaPost15Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost15Method(MetaTestHttpServiceClass owner) {
                  super("post15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost16Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost16Method(MetaTestHttpServiceClass owner) {
                  super("post16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost17Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost17Method(MetaTestHttpServiceClass owner) {
                  super("post17",metaType(Void.class),owner);
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

              public final class MetaPost18Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost18Method(MetaTestHttpServiceClass owner) {
                  super("post18",metaType(java.lang.String.class),owner);
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

              public final class MetaPost19Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost19Method(MetaTestHttpServiceClass owner) {
                  super("post19",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaPost20Method extends InstanceMetaMethod<MetaTestHttpServiceClass, io.art.http.test.service.TestHttpService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> emptyParameter = register(new MetaParameter<>(0, "empty",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaPost20Method(MetaTestHttpServiceClass owner) {
                  super("post20",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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
              private static final LazyProperty<MetaTestWsServiceClass> self = MetaClass.self(io.art.http.test.service.TestWsService.class);

              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor(this));

              private final MetaWs1Method ws1Method = register(new MetaWs1Method(this));

              private final MetaWs2Method ws2Method = register(new MetaWs2Method(this));

              private final MetaWs3Method ws3Method = register(new MetaWs3Method(this));

              private final MetaWs4Method ws4Method = register(new MetaWs4Method(this));

              private final MetaWs5Method ws5Method = register(new MetaWs5Method(this));

              private final MetaWs6Method ws6Method = register(new MetaWs6Method(this));

              private final MetaWs7Method ws7Method = register(new MetaWs7Method(this));

              private final MetaWs8Method ws8Method = register(new MetaWs8Method(this));

              private final MetaWs9Method ws9Method = register(new MetaWs9Method(this));

              private final MetaWs10Method ws10Method = register(new MetaWs10Method(this));

              private final MetaWs11Method ws11Method = register(new MetaWs11Method(this));

              private final MetaWs12Method ws12Method = register(new MetaWs12Method(this));

              private final MetaWs13Method ws13Method = register(new MetaWs13Method(this));

              private final MetaWs14Method ws14Method = register(new MetaWs14Method(this));

              private final MetaWs15Method ws15Method = register(new MetaWs15Method(this));

              private final MetaWs16Method ws16Method = register(new MetaWs16Method(this));

              private final MetaWs17Method ws17Method = register(new MetaWs17Method(this));

              private final MetaWsEchoMethod wsEchoMethod = register(new MetaWsEchoMethod(this));

              private MetaTestWsServiceClass() {
                super(metaType(io.art.http.test.service.TestWsService.class));
              }

              public static MetaTestWsServiceClass testWsService() {
                return self.get();
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

              public MetaWsEchoMethod wsEchoMethod() {
                return wsEchoMethod;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<MetaTestWsServiceClass, io.art.http.test.service.TestWsService> {
                private MetaConstructorConstructor(MetaTestWsServiceClass owner) {
                  super(metaType(io.art.http.test.service.TestWsService.class),owner);
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

              public final class MetaWs1Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, Void> {
                private MetaWs1Method(MetaTestWsServiceClass owner) {
                  super("ws1",metaType(Void.class),owner);
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

              public final class MetaWs2Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, java.lang.String> {
                private MetaWs2Method(MetaTestWsServiceClass owner) {
                  super("ws2",metaType(java.lang.String.class),owner);
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

              public final class MetaWs3Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private MetaWs3Method(MetaTestWsServiceClass owner) {
                  super("ws3",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs4Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private MetaWs4Method(MetaTestWsServiceClass owner) {
                  super("ws4",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs5Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs5Method(MetaTestWsServiceClass owner) {
                  super("ws5",metaType(Void.class),owner);
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

              public final class MetaWs6Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, java.lang.String> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs6Method(MetaTestWsServiceClass owner) {
                  super("ws6",metaType(java.lang.String.class),owner);
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

              public final class MetaWs7Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs7Method(MetaTestWsServiceClass owner) {
                  super("ws7",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs8Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<java.lang.String> inputParameter = register(new MetaParameter<>(0, "input",metaType(java.lang.String.class)));

                private MetaWs8Method(MetaTestWsServiceClass owner) {
                  super("ws8",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs9Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs9Method(MetaTestWsServiceClass owner) {
                  super("ws9",metaType(Void.class),owner);
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

              public final class MetaWs10Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs10Method(MetaTestWsServiceClass owner) {
                  super("ws10",metaType(java.lang.String.class),owner);
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

              public final class MetaWs11Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs11Method(MetaTestWsServiceClass owner) {
                  super("ws11",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs12Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaWs12Method(MetaTestWsServiceClass owner) {
                  super("ws12",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs13Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs13Method(MetaTestWsServiceClass owner) {
                  super("ws13",metaType(Void.class),owner);
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

              public final class MetaWs14Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, java.lang.String> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs14Method(MetaTestWsServiceClass owner) {
                  super("ws14",metaType(java.lang.String.class),owner);
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

              public final class MetaWs15Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Mono<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs15Method(MetaTestWsServiceClass owner) {
                  super("ws15",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs16Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs16Method(MetaTestWsServiceClass owner) {
                  super("ws16",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
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

              public final class MetaWs17Method extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, Void> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWs17Method(MetaTestWsServiceClass owner) {
                  super("ws17",metaType(Void.class),owner);
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

              public final class MetaWsEchoMethod extends InstanceMetaMethod<MetaTestWsServiceClass, io.art.http.test.service.TestWsService, reactor.core.publisher.Flux<java.lang.String>> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> inputParameter = register(new MetaParameter<>(0, "input",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaWsEchoMethod(MetaTestWsServiceClass owner) {
                  super("wsEcho",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object[] arguments) throws Throwable {
                  return instance.wsEcho((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.http.test.service.TestWsService instance,
                    Object argument) throws Throwable {
                  return instance.wsEcho((reactor.core.publisher.Flux)(argument));
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
