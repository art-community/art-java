package io.art.tarantool.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaField;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaMethod;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.MetaProxy;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaTarantoolTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaTarantoolTest(MetaLibrary... dependencies) {
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
      private final MetaTarantoolPackage tarantoolPackage = register(new MetaTarantoolPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaTarantoolPackage tarantoolPackage() {
        return tarantoolPackage;
      }

      public static final class MetaTarantoolPackage extends MetaPackage {
        private final MetaTestPackage testPackage = register(new MetaTestPackage());

        private MetaTarantoolPackage() {
          super("tarantool");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaTestStorageClass testStorageClass = register(new MetaTestStorageClass());

          private final MetaModelPackage modelPackage = register(new MetaModelPackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaTestStorageClass testStorageClass() {
            return testStorageClass;
          }

          public MetaModelPackage modelPackage() {
            return modelPackage;
          }

          public static final class MetaTestStorageClass extends MetaClass<io.art.tarantool.test.TestStorage> {
            private final MetaTestSpaceMethod testSpaceMethod = register(new MetaTestSpaceMethod());

            private final MetaTestSpaceClass testSpaceClass = register(new MetaTestSpaceClass());

            private MetaTestStorageClass() {
              super(metaType(io.art.tarantool.test.TestStorage.class));
            }

            public MetaTestSpaceMethod testSpaceMethod() {
              return testSpaceMethod;
            }

            @Override
            public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
              return new MetaTestStorageProxy(invocations);
            }

            public MetaTestSpaceClass testSpaceClass() {
              return testSpaceClass;
            }

            public static final class MetaTestSpaceMethod extends InstanceMetaMethod<io.art.tarantool.test.TestStorage, io.art.tarantool.test.TestStorage.TestSpace> {
              private MetaTestSpaceMethod() {
                super("testSpace",metaType(io.art.tarantool.test.TestStorage.TestSpace.class));
              }

              @Override
              public Object invoke(io.art.tarantool.test.TestStorage instance, Object[] arguments)
                  throws Throwable {
                return instance.testSpace();
              }

              @Override
              public Object invoke(io.art.tarantool.test.TestStorage instance) throws Throwable {
                return instance.testSpace();
              }
            }

            public class MetaTestStorageProxy extends MetaProxy implements io.art.tarantool.test.TestStorage {
              private final Function<Object, Object> testSpaceInvocation;

              public MetaTestStorageProxy(
                  Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                super(invocations);
                testSpaceInvocation = invocations.get(testSpaceMethod);
              }

              @Override
              public io.art.tarantool.test.TestStorage.TestSpace testSpace() {
                return (io.art.tarantool.test.TestStorage.TestSpace)(testSpaceInvocation.apply(null));
              }
            }

            public static final class MetaTestSpaceClass extends MetaClass<io.art.tarantool.test.TestStorage.TestSpace> {
              private final MetaTryRequestMethod tryRequestMethod = register(new MetaTryRequestMethod());

              private MetaTestSpaceClass() {
                super(metaType(io.art.tarantool.test.TestStorage.TestSpace.class));
              }

              public MetaTryRequestMethod tryRequestMethod() {
                return tryRequestMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestSpaceProxy(invocations);
              }

              public static final class MetaTryRequestMethod extends InstanceMetaMethod<io.art.tarantool.test.TestStorage.TestSpace, io.art.tarantool.test.model.TestRequest> {
                private final MetaParameter<io.art.tarantool.test.model.TestRequest> requestParameter = register(new MetaParameter<>(0, "request",metaType(io.art.tarantool.test.model.TestRequest.class)));

                private MetaTryRequestMethod() {
                  super("tryRequest",metaType(io.art.tarantool.test.model.TestRequest.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.TestStorage.TestSpace instance,
                    Object[] arguments) throws Throwable {
                  return instance.tryRequest((io.art.tarantool.test.model.TestRequest)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.tarantool.test.TestStorage.TestSpace instance,
                    Object argument) throws Throwable {
                  return instance.tryRequest((io.art.tarantool.test.model.TestRequest)(argument));
                }

                public MetaParameter<io.art.tarantool.test.model.TestRequest> requestParameter() {
                  return requestParameter;
                }
              }

              public class MetaTestSpaceProxy extends MetaProxy implements io.art.tarantool.test.TestStorage.TestSpace {
                private final Function<Object, Object> tryRequestInvocation;

                public MetaTestSpaceProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  tryRequestInvocation = invocations.get(tryRequestMethod);
                }

                @Override
                public io.art.tarantool.test.model.TestRequest tryRequest(
                    io.art.tarantool.test.model.TestRequest request) {
                  return (io.art.tarantool.test.model.TestRequest)(tryRequestInvocation.apply(request));
                }
              }
            }
          }

          public static final class MetaModelPackage extends MetaPackage {
            private final MetaTestRequestClass testRequestClass = register(new MetaTestRequestClass());

            private MetaModelPackage() {
              super("model");
            }

            public MetaTestRequestClass testRequestClass() {
              return testRequestClass;
            }

            public static final class MetaTestRequestClass extends MetaClass<io.art.tarantool.test.model.TestRequest> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaField<java.lang.String> dataField = register(new MetaField<>("data",metaType(java.lang.String.class),false));

              private final MetaGetDataMethod getDataMethod = register(new MetaGetDataMethod());

              private MetaTestRequestClass() {
                super(metaType(io.art.tarantool.test.model.TestRequest.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<java.lang.String> dataField() {
                return dataField;
              }

              public MetaGetDataMethod getDataMethod() {
                return getDataMethod;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.tarantool.test.model.TestRequest> {
                private final MetaParameter<java.lang.String> dataParameter = register(new MetaParameter<>(0, "data",metaType(java.lang.String.class)));

                private MetaConstructorConstructor() {
                  super(metaType(io.art.tarantool.test.model.TestRequest.class));
                }

                @Override
                public io.art.tarantool.test.model.TestRequest invoke(Object[] arguments) throws
                    Throwable {
                  return new io.art.tarantool.test.model.TestRequest((java.lang.String)(arguments[0]));
                }

                @Override
                public io.art.tarantool.test.model.TestRequest invoke(Object argument) throws
                    Throwable {
                  return new io.art.tarantool.test.model.TestRequest((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> dataParameter() {
                  return dataParameter;
                }
              }

              public static final class MetaGetDataMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestRequest, java.lang.String> {
                private MetaGetDataMethod() {
                  super("getData",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.TestRequest instance,
                    Object[] arguments) throws Throwable {
                  return instance.getData();
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.TestRequest instance) throws
                    Throwable {
                  return instance.getData();
                }
              }
            }
          }
        }
      }
    }
  }
}
