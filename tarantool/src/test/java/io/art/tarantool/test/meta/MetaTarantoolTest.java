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
          private final MetaModelPackage modelPackage = register(new MetaModelPackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaModelPackage modelPackage() {
            return modelPackage;
          }

          public static final class MetaModelPackage extends MetaPackage {
            private final MetaTestDataClass testDataClass = register(new MetaTestDataClass());

            private final MetaTestStorageClass testStorageClass = register(new MetaTestStorageClass());

            private MetaModelPackage() {
              super("model");
            }

            public MetaTestDataClass testDataClass() {
              return testDataClass;
            }

            public MetaTestStorageClass testStorageClass() {
              return testStorageClass;
            }

            public static final class MetaTestDataClass extends MetaClass<io.art.tarantool.test.model.TestData> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaField<Integer> idField = register(new MetaField<>("id",metaType(int.class),false));

              private final MetaField<java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false));

              private final MetaField<io.art.tarantool.test.model.TestData.Inner> innerField = register(new MetaField<>("inner",metaType(io.art.tarantool.test.model.TestData.Inner.class),false));

              private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

              private final MetaGetIdMethod getIdMethod = register(new MetaGetIdMethod());

              private final MetaGetContentMethod getContentMethod = register(new MetaGetContentMethod());

              private final MetaGetInnerMethod getInnerMethod = register(new MetaGetInnerMethod());

              private final MetaTestDataBuilderClass testDataBuilderClass = register(new MetaTestDataBuilderClass());

              private final MetaInnerClass innerClass = register(new MetaInnerClass());

              private MetaTestDataClass() {
                super(metaType(io.art.tarantool.test.model.TestData.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<Integer> idField() {
                return idField;
              }

              public MetaField<java.lang.String> contentField() {
                return contentField;
              }

              public MetaField<io.art.tarantool.test.model.TestData.Inner> innerField() {
                return innerField;
              }

              public MetaToBuilderMethod toBuilderMethod() {
                return toBuilderMethod;
              }

              public MetaGetIdMethod getIdMethod() {
                return getIdMethod;
              }

              public MetaGetContentMethod getContentMethod() {
                return getContentMethod;
              }

              public MetaGetInnerMethod getInnerMethod() {
                return getInnerMethod;
              }

              public MetaTestDataBuilderClass testDataBuilderClass() {
                return testDataBuilderClass;
              }

              public MetaInnerClass innerClass() {
                return innerClass;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.tarantool.test.model.TestData> {
                private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(1, "content",metaType(java.lang.String.class)));

                private final MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter = register(new MetaParameter<>(2, "inner",metaType(io.art.tarantool.test.model.TestData.Inner.class)));

                private MetaConstructorConstructor() {
                  super(metaType(io.art.tarantool.test.model.TestData.class));
                }

                @Override
                public io.art.tarantool.test.model.TestData invoke(java.lang.Object[] arguments)
                    throws Throwable {
                  return new io.art.tarantool.test.model.TestData((int)(arguments[0]),(java.lang.String)(arguments[1]),(io.art.tarantool.test.model.TestData.Inner)(arguments[2]));
                }

                public MetaParameter<Integer> idParameter() {
                  return idParameter;
                }

                public MetaParameter<java.lang.String> contentParameter() {
                  return contentParameter;
                }

                public MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter() {
                  return innerParameter;
                }
              }

              public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                private MetaToBuilderMethod() {
                  super("toBuilder",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.toBuilder();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.toBuilder();
                }
              }

              public static final class MetaGetIdMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData, Integer> {
                private MetaGetIdMethod() {
                  super("getId",metaType(int.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getId();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.getId();
                }
              }

              public static final class MetaGetContentMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData, java.lang.String> {
                private MetaGetContentMethod() {
                  super("getContent",metaType(java.lang.String.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getContent();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.getContent();
                }
              }

              public static final class MetaGetInnerMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData, io.art.tarantool.test.model.TestData.Inner> {
                private MetaGetInnerMethod() {
                  super("getInner",metaType(io.art.tarantool.test.model.TestData.Inner.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getInner();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.TestData instance) throws
                    Throwable {
                  return instance.getInner();
                }
              }

              public static final class MetaTestDataBuilderClass extends MetaClass<io.art.tarantool.test.model.TestData.TestDataBuilder> {
                private final MetaField<Integer> idField = register(new MetaField<>("id",metaType(int.class),false));

                private final MetaField<java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false));

                private final MetaField<io.art.tarantool.test.model.TestData.Inner> innerField = register(new MetaField<>("inner",metaType(io.art.tarantool.test.model.TestData.Inner.class),false));

                private final MetaIdMethod idMethod = register(new MetaIdMethod());

                private final MetaContentMethod contentMethod = register(new MetaContentMethod());

                private final MetaInnerMethod innerMethod = register(new MetaInnerMethod());

                private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

                private MetaTestDataBuilderClass() {
                  super(metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class));
                }

                public MetaField<Integer> idField() {
                  return idField;
                }

                public MetaField<java.lang.String> contentField() {
                  return contentField;
                }

                public MetaField<io.art.tarantool.test.model.TestData.Inner> innerField() {
                  return innerField;
                }

                public MetaIdMethod idMethod() {
                  return idMethod;
                }

                public MetaContentMethod contentMethod() {
                  return contentMethod;
                }

                public MetaInnerMethod innerMethod() {
                  return innerMethod;
                }

                public MetaBuildMethod buildMethod() {
                  return buildMethod;
                }

                public static final class MetaIdMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                  private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                  private MetaIdMethod() {
                    super("id",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.id((int)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.id((int)(argument));
                  }

                  public MetaParameter<Integer> idParameter() {
                    return idParameter;
                  }
                }

                public static final class MetaContentMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                  private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                  private MetaContentMethod() {
                    super("content",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.content((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.content((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> contentParameter() {
                    return contentParameter;
                  }
                }

                public static final class MetaInnerMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData.TestDataBuilder> {
                  private final MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter = register(new MetaParameter<>(0, "inner",metaType(io.art.tarantool.test.model.TestData.Inner.class)));

                  private MetaInnerMethod() {
                    super("inner",metaType(io.art.tarantool.test.model.TestData.TestDataBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.inner((io.art.tarantool.test.model.TestData.Inner)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.inner((io.art.tarantool.test.model.TestData.Inner)(argument));
                  }

                  public MetaParameter<io.art.tarantool.test.model.TestData.Inner> innerParameter(
                      ) {
                    return innerParameter;
                  }
                }

                public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.TestDataBuilder, io.art.tarantool.test.model.TestData> {
                  private MetaBuildMethod() {
                    super("build",metaType(io.art.tarantool.test.model.TestData.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.build();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.TestDataBuilder instance) throws
                      Throwable {
                    return instance.build();
                  }
                }
              }

              public static final class MetaInnerClass extends MetaClass<io.art.tarantool.test.model.TestData.Inner> {
                private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

                private final MetaField<java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false));

                private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

                private final MetaGetContentMethod getContentMethod = register(new MetaGetContentMethod());

                private final MetaInnerBuilderClass innerBuilderClass = register(new MetaInnerBuilderClass());

                private MetaInnerClass() {
                  super(metaType(io.art.tarantool.test.model.TestData.Inner.class));
                }

                public MetaConstructorConstructor constructor() {
                  return constructor;
                }

                public MetaField<java.lang.String> contentField() {
                  return contentField;
                }

                public MetaToBuilderMethod toBuilderMethod() {
                  return toBuilderMethod;
                }

                public MetaGetContentMethod getContentMethod() {
                  return getContentMethod;
                }

                public MetaInnerBuilderClass innerBuilderClass() {
                  return innerBuilderClass;
                }

                public static final class MetaConstructorConstructor extends MetaConstructor<io.art.tarantool.test.model.TestData.Inner> {
                  private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                  private MetaConstructorConstructor() {
                    super(metaType(io.art.tarantool.test.model.TestData.Inner.class));
                  }

                  @Override
                  public io.art.tarantool.test.model.TestData.Inner invoke(
                      java.lang.Object[] arguments) throws Throwable {
                    return new io.art.tarantool.test.model.TestData.Inner((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public io.art.tarantool.test.model.TestData.Inner invoke(
                      java.lang.Object argument) throws Throwable {
                    return new io.art.tarantool.test.model.TestData.Inner((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> contentParameter() {
                    return contentParameter;
                  }
                }

                public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.Inner, io.art.tarantool.test.model.TestData.Inner.InnerBuilder> {
                  private MetaToBuilderMethod() {
                    super("toBuilder",metaType(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.toBuilder();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance) throws Throwable {
                    return instance.toBuilder();
                  }
                }

                public static final class MetaGetContentMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.Inner, java.lang.String> {
                  private MetaGetContentMethod() {
                    super("getContent",metaType(java.lang.String.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getContent();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.TestData.Inner instance) throws Throwable {
                    return instance.getContent();
                  }
                }

                public static final class MetaInnerBuilderClass extends MetaClass<io.art.tarantool.test.model.TestData.Inner.InnerBuilder> {
                  private final MetaField<java.lang.String> contentField = register(new MetaField<>("content",metaType(java.lang.String.class),false));

                  private final MetaContentMethod contentMethod = register(new MetaContentMethod());

                  private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

                  private MetaInnerBuilderClass() {
                    super(metaType(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class));
                  }

                  public MetaField<java.lang.String> contentField() {
                    return contentField;
                  }

                  public MetaContentMethod contentMethod() {
                    return contentMethod;
                  }

                  public MetaBuildMethod buildMethod() {
                    return buildMethod;
                  }

                  public static final class MetaContentMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.Inner.InnerBuilder, io.art.tarantool.test.model.TestData.Inner.InnerBuilder> {
                    private final MetaParameter<java.lang.String> contentParameter = register(new MetaParameter<>(0, "content",metaType(java.lang.String.class)));

                    private MetaContentMethod() {
                      super("content",metaType(io.art.tarantool.test.model.TestData.Inner.InnerBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.content((java.lang.String)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.content((java.lang.String)(argument));
                    }

                    public MetaParameter<java.lang.String> contentParameter() {
                      return contentParameter;
                    }
                  }

                  public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.tarantool.test.model.TestData.Inner.InnerBuilder, io.art.tarantool.test.model.TestData.Inner> {
                    private MetaBuildMethod() {
                      super("build",metaType(io.art.tarantool.test.model.TestData.Inner.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.build();
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.TestData.Inner.InnerBuilder instance) throws
                        Throwable {
                      return instance.build();
                    }
                  }
                }
              }
            }

            public static final class MetaTestStorageClass extends MetaClass<io.art.tarantool.test.model.TestStorage> {
              private MetaTestStorageClass() {
                super(metaType(io.art.tarantool.test.model.TestStorage.class));
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaTestStorageProxy(invocations);
              }

              public class MetaTestStorageProxy extends MetaProxy implements io.art.tarantool.test.model.TestStorage {
                public MetaTestStorageProxy(
                    Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                }
              }
            }
          }
        }
      }
    }
  }
}
