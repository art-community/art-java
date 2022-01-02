package io.art.tarantool.test.meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
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
              private final MetaSaveRequestMethod saveRequestMethod = register(new MetaSaveRequestMethod());

              private final MetaGetRequestMethod getRequestMethod = register(new MetaGetRequestMethod());

              private MetaTestSpaceClass() {
                super(metaType(io.art.tarantool.test.TestStorage.TestSpace.class));
              }

              public MetaSaveRequestMethod saveRequestMethod() {
                return saveRequestMethod;
              }

              public MetaGetRequestMethod getRequestMethod() {
                return getRequestMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaTestSpaceProxy(invocations);
              }

              public static final class MetaSaveRequestMethod extends InstanceMetaMethod<io.art.tarantool.test.TestStorage.TestSpace, io.art.tarantool.test.model.TestRequest> {
                private final MetaParameter<io.art.tarantool.test.model.TestRequest> requestParameter = register(new MetaParameter<>(0, "request",metaType(io.art.tarantool.test.model.TestRequest.class)));

                private MetaSaveRequestMethod() {
                  super("saveRequest",metaType(io.art.tarantool.test.model.TestRequest.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.TestStorage.TestSpace instance,
                    Object[] arguments) throws Throwable {
                  return instance.saveRequest((io.art.tarantool.test.model.TestRequest)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.tarantool.test.TestStorage.TestSpace instance,
                    Object argument) throws Throwable {
                  return instance.saveRequest((io.art.tarantool.test.model.TestRequest)(argument));
                }

                public MetaParameter<io.art.tarantool.test.model.TestRequest> requestParameter() {
                  return requestParameter;
                }
              }

              public static final class MetaGetRequestMethod extends InstanceMetaMethod<io.art.tarantool.test.TestStorage.TestSpace, io.art.tarantool.test.model.TestRequest> {
                private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private MetaGetRequestMethod() {
                  super("getRequest",metaType(io.art.tarantool.test.model.TestRequest.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.TestStorage.TestSpace instance,
                    Object[] arguments) throws Throwable {
                  return instance.getRequest((int)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.tarantool.test.TestStorage.TestSpace instance,
                    Object argument) throws Throwable {
                  return instance.getRequest((int)(argument));
                }

                public MetaParameter<Integer> idParameter() {
                  return idParameter;
                }
              }

              public class MetaTestSpaceProxy extends MetaProxy implements io.art.tarantool.test.TestStorage.TestSpace {
                private final Function<Object, Object> saveRequestInvocation;

                private final Function<Object, Object> getRequestInvocation;

                public MetaTestSpaceProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  saveRequestInvocation = invocations.get(saveRequestMethod);
                  getRequestInvocation = invocations.get(getRequestMethod);
                }

                @Override
                public io.art.tarantool.test.model.TestRequest saveRequest(
                    io.art.tarantool.test.model.TestRequest request) {
                  return (io.art.tarantool.test.model.TestRequest)(saveRequestInvocation.apply(request));
                }

                @Override
                public io.art.tarantool.test.model.TestRequest getRequest(int id) {
                  return (io.art.tarantool.test.model.TestRequest)(getRequestInvocation.apply(id));
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
              private final MetaField<Integer> idField = register(new MetaField<>("id",metaType(int.class),false));

              private final MetaField<java.lang.String> dataField = register(new MetaField<>("data",metaType(java.lang.String.class),false));

              private final MetaGetIdMethod getIdMethod = register(new MetaGetIdMethod());

              private final MetaGetDataMethod getDataMethod = register(new MetaGetDataMethod());

              private final MetaTestRequestBuilderClass testRequestBuilderClass = register(new MetaTestRequestBuilderClass());

              private MetaTestRequestClass() {
                super(metaType(io.art.tarantool.test.model.TestRequest.class));
              }

              public MetaField<Integer> idField() {
                return idField;
              }

              public MetaField<java.lang.String> dataField() {
                return dataField;
              }

              public MetaGetIdMethod getIdMethod() {
                return getIdMethod;
              }

              public MetaGetDataMethod getDataMethod() {
                return getDataMethod;
              }

              public MetaTestRequestBuilderClass testRequestBuilderClass() {
                return testRequestBuilderClass;
              }

              public static final class MetaGetIdMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestRequest, Integer> {
                private MetaGetIdMethod() {
                  super("getId",metaType(int.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.TestRequest instance,
                    Object[] arguments) throws Throwable {
                  return instance.getId();
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.TestRequest instance) throws
                    Throwable {
                  return instance.getId();
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

              public static final class MetaTestRequestBuilderClass extends MetaClass<io.art.tarantool.test.model.TestRequest.TestRequestBuilder> {
                private final MetaField<Integer> idField = register(new MetaField<>("id",metaType(int.class),false));

                private final MetaField<java.lang.String> dataField = register(new MetaField<>("data",metaType(java.lang.String.class),false));

                private final MetaIdMethod idMethod = register(new MetaIdMethod());

                private final MetaDataMethod dataMethod = register(new MetaDataMethod());

                private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

                private MetaTestRequestBuilderClass() {
                  super(metaType(io.art.tarantool.test.model.TestRequest.TestRequestBuilder.class));
                }

                public MetaField<Integer> idField() {
                  return idField;
                }

                public MetaField<java.lang.String> dataField() {
                  return dataField;
                }

                public MetaIdMethod idMethod() {
                  return idMethod;
                }

                public MetaDataMethod dataMethod() {
                  return dataMethod;
                }

                public MetaBuildMethod buildMethod() {
                  return buildMethod;
                }

                public static final class MetaIdMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestRequest.TestRequestBuilder, io.art.tarantool.test.model.TestRequest.TestRequestBuilder> {
                  private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                  private MetaIdMethod() {
                    super("id",metaType(io.art.tarantool.test.model.TestRequest.TestRequestBuilder.class));
                  }

                  @Override
                  public Object invoke(
                      io.art.tarantool.test.model.TestRequest.TestRequestBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.id((int)(arguments[0]));
                  }

                  @Override
                  public Object invoke(
                      io.art.tarantool.test.model.TestRequest.TestRequestBuilder instance,
                      Object argument) throws Throwable {
                    return instance.id((int)(argument));
                  }

                  public MetaParameter<Integer> idParameter() {
                    return idParameter;
                  }
                }

                public static final class MetaDataMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestRequest.TestRequestBuilder, io.art.tarantool.test.model.TestRequest.TestRequestBuilder> {
                  private final MetaParameter<java.lang.String> dataParameter = register(new MetaParameter<>(0, "data",metaType(java.lang.String.class)));

                  private MetaDataMethod() {
                    super("data",metaType(io.art.tarantool.test.model.TestRequest.TestRequestBuilder.class));
                  }

                  @Override
                  public Object invoke(
                      io.art.tarantool.test.model.TestRequest.TestRequestBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.data((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public Object invoke(
                      io.art.tarantool.test.model.TestRequest.TestRequestBuilder instance,
                      Object argument) throws Throwable {
                    return instance.data((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> dataParameter() {
                    return dataParameter;
                  }
                }

                public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestRequest.TestRequestBuilder, io.art.tarantool.test.model.TestRequest> {
                  private MetaBuildMethod() {
                    super("build",metaType(io.art.tarantool.test.model.TestRequest.class));
                  }

                  @Override
                  public Object invoke(
                      io.art.tarantool.test.model.TestRequest.TestRequestBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.build();
                  }

                  @Override
                  public Object invoke(
                      io.art.tarantool.test.model.TestRequest.TestRequestBuilder instance) throws
                      Throwable {
                    return instance.build();
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
