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
          private final MetaUserStorageClass userStorageClass = register(new MetaUserStorageClass());

          private final MetaModelPackage modelPackage = register(new MetaModelPackage());

          private MetaTestPackage() {
            super("test");
          }

          public MetaUserStorageClass userStorageClass() {
            return userStorageClass;
          }

          public MetaModelPackage modelPackage() {
            return modelPackage;
          }

          public static final class MetaUserStorageClass extends MetaClass<io.art.tarantool.test.UserStorage> {
            private final MetaTestSpaceMethod testSpaceMethod = register(new MetaTestSpaceMethod());

            private final MetaUserSpaceClass userSpaceClass = register(new MetaUserSpaceClass());

            private MetaUserStorageClass() {
              super(metaType(io.art.tarantool.test.UserStorage.class));
            }

            public MetaTestSpaceMethod testSpaceMethod() {
              return testSpaceMethod;
            }

            @Override
            public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
              return new MetaUserStorageProxy(invocations);
            }

            public MetaUserSpaceClass userSpaceClass() {
              return userSpaceClass;
            }

            public static final class MetaTestSpaceMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage, io.art.tarantool.test.UserStorage.UserSpace> {
              private MetaTestSpaceMethod() {
                super("testSpace",metaType(io.art.tarantool.test.UserStorage.UserSpace.class));
              }

              @Override
              public Object invoke(io.art.tarantool.test.UserStorage instance, Object[] arguments)
                  throws Throwable {
                return instance.testSpace();
              }

              @Override
              public Object invoke(io.art.tarantool.test.UserStorage instance) throws Throwable {
                return instance.testSpace();
              }
            }

            public class MetaUserStorageProxy extends MetaProxy implements io.art.tarantool.test.UserStorage {
              private final Function<Object, Object> testSpaceInvocation;

              public MetaUserStorageProxy(
                  Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                super(invocations);
                testSpaceInvocation = invocations.get(testSpaceMethod);
              }

              @Override
              public io.art.tarantool.test.UserStorage.UserSpace testSpace() {
                return (io.art.tarantool.test.UserStorage.UserSpace)(testSpaceInvocation.apply(null));
              }
            }

            public static final class MetaUserSpaceClass extends MetaClass<io.art.tarantool.test.UserStorage.UserSpace> {
              private final MetaSaveUserMethod saveUserMethod = register(new MetaSaveUserMethod());

              private final MetaGetUserMethod getUserMethod = register(new MetaGetUserMethod());

              private MetaUserSpaceClass() {
                super(metaType(io.art.tarantool.test.UserStorage.UserSpace.class));
              }

              public MetaSaveUserMethod saveUserMethod() {
                return saveUserMethod;
              }

              public MetaGetUserMethod getUserMethod() {
                return getUserMethod;
              }

              @Override
              public MetaProxy proxy(Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                return new MetaUserSpaceProxy(invocations);
              }

              public static final class MetaSaveUserMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage.UserSpace, io.art.tarantool.test.model.User> {
                private final MetaParameter<io.art.tarantool.test.model.User> requestParameter = register(new MetaParameter<>(0, "request",metaType(io.art.tarantool.test.model.User.class)));

                private MetaSaveUserMethod() {
                  super("saveUser",metaType(io.art.tarantool.test.model.User.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    Object[] arguments) throws Throwable {
                  return instance.saveUser((io.art.tarantool.test.model.User)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    Object argument) throws Throwable {
                  return instance.saveUser((io.art.tarantool.test.model.User)(argument));
                }

                public MetaParameter<io.art.tarantool.test.model.User> requestParameter() {
                  return requestParameter;
                }
              }

              public static final class MetaGetUserMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage.UserSpace, io.art.tarantool.test.model.User> {
                private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private MetaGetUserMethod() {
                  super("getUser",metaType(io.art.tarantool.test.model.User.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    Object[] arguments) throws Throwable {
                  return instance.getUser((int)(arguments[0]));
                }

                @Override
                public Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    Object argument) throws Throwable {
                  return instance.getUser((int)(argument));
                }

                public MetaParameter<Integer> idParameter() {
                  return idParameter;
                }
              }

              public class MetaUserSpaceProxy extends MetaProxy implements io.art.tarantool.test.UserStorage.UserSpace {
                private final Function<Object, Object> saveUserInvocation;

                private final Function<Object, Object> getUserInvocation;

                public MetaUserSpaceProxy(
                    Map<MetaMethod<?>, Function<Object, Object>> invocations) {
                  super(invocations);
                  saveUserInvocation = invocations.get(saveUserMethod);
                  getUserInvocation = invocations.get(getUserMethod);
                }

                @Override
                public io.art.tarantool.test.model.User saveUser(
                    io.art.tarantool.test.model.User request) {
                  return (io.art.tarantool.test.model.User)(saveUserInvocation.apply(request));
                }

                @Override
                public io.art.tarantool.test.model.User getUser(int id) {
                  return (io.art.tarantool.test.model.User)(getUserInvocation.apply(id));
                }
              }
            }
          }

          public static final class MetaModelPackage extends MetaPackage {
            private final MetaUserClass userClass = register(new MetaUserClass());

            private MetaModelPackage() {
              super("model");
            }

            public MetaUserClass userClass() {
              return userClass;
            }

            public static final class MetaUserClass extends MetaClass<io.art.tarantool.test.model.User> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaField<Integer> idField = register(new MetaField<>("id",metaType(int.class),false));

              private final MetaField<java.lang.String> nameField = register(new MetaField<>("name",metaType(java.lang.String.class),false));

              private final MetaGetIdMethod getIdMethod = register(new MetaGetIdMethod());

              private final MetaGetNameMethod getNameMethod = register(new MetaGetNameMethod());

              private final MetaUserBuilderClass userBuilderClass = register(new MetaUserBuilderClass());

              private MetaUserClass() {
                super(metaType(io.art.tarantool.test.model.User.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<Integer> idField() {
                return idField;
              }

              public MetaField<java.lang.String> nameField() {
                return nameField;
              }

              public MetaGetIdMethod getIdMethod() {
                return getIdMethod;
              }

              public MetaGetNameMethod getNameMethod() {
                return getNameMethod;
              }

              public MetaUserBuilderClass userBuilderClass() {
                return userBuilderClass;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.tarantool.test.model.User> {
                private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private final MetaParameter<java.lang.String> nameParameter = register(new MetaParameter<>(1, "name",metaType(java.lang.String.class)));

                private MetaConstructorConstructor() {
                  super(metaType(io.art.tarantool.test.model.User.class));
                }

                @Override
                public io.art.tarantool.test.model.User invoke(Object[] arguments) throws
                    Throwable {
                  return new io.art.tarantool.test.model.User((int)(arguments[0]),(java.lang.String)(arguments[1]));
                }

                public MetaParameter<Integer> idParameter() {
                  return idParameter;
                }

                public MetaParameter<java.lang.String> nameParameter() {
                  return nameParameter;
                }
              }

              public static final class MetaGetIdMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User, Integer> {
                private MetaGetIdMethod() {
                  super("getId",metaType(int.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.User instance, Object[] arguments)
                    throws Throwable {
                  return instance.getId();
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.User instance) throws Throwable {
                  return instance.getId();
                }
              }

              public static final class MetaGetNameMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User, java.lang.String> {
                private MetaGetNameMethod() {
                  super("getName",metaType(java.lang.String.class));
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.User instance, Object[] arguments)
                    throws Throwable {
                  return instance.getName();
                }

                @Override
                public Object invoke(io.art.tarantool.test.model.User instance) throws Throwable {
                  return instance.getName();
                }
              }

              public static final class MetaUserBuilderClass extends MetaClass<io.art.tarantool.test.model.User.UserBuilder> {
                private final MetaField<Integer> idField = register(new MetaField<>("id",metaType(int.class),false));

                private final MetaField<java.lang.String> nameField = register(new MetaField<>("name",metaType(java.lang.String.class),false));

                private final MetaIdMethod idMethod = register(new MetaIdMethod());

                private final MetaNameMethod nameMethod = register(new MetaNameMethod());

                private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

                private MetaUserBuilderClass() {
                  super(metaType(io.art.tarantool.test.model.User.UserBuilder.class));
                }

                public MetaField<Integer> idField() {
                  return idField;
                }

                public MetaField<java.lang.String> nameField() {
                  return nameField;
                }

                public MetaIdMethod idMethod() {
                  return idMethod;
                }

                public MetaNameMethod nameMethod() {
                  return nameMethod;
                }

                public MetaBuildMethod buildMethod() {
                  return buildMethod;
                }

                public static final class MetaIdMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.UserBuilder, io.art.tarantool.test.model.User.UserBuilder> {
                  private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                  private MetaIdMethod() {
                    super("id",metaType(io.art.tarantool.test.model.User.UserBuilder.class));
                  }

                  @Override
                  public Object invoke(io.art.tarantool.test.model.User.UserBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.id((int)(arguments[0]));
                  }

                  @Override
                  public Object invoke(io.art.tarantool.test.model.User.UserBuilder instance,
                      Object argument) throws Throwable {
                    return instance.id((int)(argument));
                  }

                  public MetaParameter<Integer> idParameter() {
                    return idParameter;
                  }
                }

                public static final class MetaNameMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.UserBuilder, io.art.tarantool.test.model.User.UserBuilder> {
                  private final MetaParameter<java.lang.String> nameParameter = register(new MetaParameter<>(0, "name",metaType(java.lang.String.class)));

                  private MetaNameMethod() {
                    super("name",metaType(io.art.tarantool.test.model.User.UserBuilder.class));
                  }

                  @Override
                  public Object invoke(io.art.tarantool.test.model.User.UserBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.name((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public Object invoke(io.art.tarantool.test.model.User.UserBuilder instance,
                      Object argument) throws Throwable {
                    return instance.name((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> nameParameter() {
                    return nameParameter;
                  }
                }

                public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.UserBuilder, io.art.tarantool.test.model.User> {
                  private MetaBuildMethod() {
                    super("build",metaType(io.art.tarantool.test.model.User.class));
                  }

                  @Override
                  public Object invoke(io.art.tarantool.test.model.User.UserBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.build();
                  }

                  @Override
                  public Object invoke(io.art.tarantool.test.model.User.UserBuilder instance) throws
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
