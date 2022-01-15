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
            public MetaProxy proxy(
                Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
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
              public java.lang.Object invoke(io.art.tarantool.test.UserStorage instance,
                  java.lang.Object[] arguments) throws Throwable {
                return instance.testSpace();
              }

              @Override
              public java.lang.Object invoke(io.art.tarantool.test.UserStorage instance) throws
                  Throwable {
                return instance.testSpace();
              }
            }

            public class MetaUserStorageProxy extends MetaProxy implements io.art.tarantool.test.UserStorage {
              private final Function<java.lang.Object, java.lang.Object> testSpaceInvocation;

              public MetaUserStorageProxy(
                  Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                super(invocations);
                testSpaceInvocation = invocations.get(testSpaceMethod);
              }

              @Override
              public io.art.tarantool.test.UserStorage.UserSpace testSpace() {
                return (io.art.tarantool.test.UserStorage.UserSpace)(testSpaceInvocation.apply(null));
              }
            }

            public static final class MetaUserSpaceClass extends MetaClass<io.art.tarantool.test.UserStorage.UserSpace> {
              private final MetaSaveMethod saveMethod = register(new MetaSaveMethod());

              private final MetaGetMethod getMethod = register(new MetaGetMethod());

              private final MetaDeleteMethod deleteMethod = register(new MetaDeleteMethod());

              private final MetaGetAllMethod getAllMethod = register(new MetaGetAllMethod());

              private final MetaFindTestMethod findTestMethod = register(new MetaFindTestMethod());

              private MetaUserSpaceClass() {
                super(metaType(io.art.tarantool.test.UserStorage.UserSpace.class));
              }

              public MetaSaveMethod saveMethod() {
                return saveMethod;
              }

              public MetaGetMethod getMethod() {
                return getMethod;
              }

              public MetaDeleteMethod deleteMethod() {
                return deleteMethod;
              }

              public MetaGetAllMethod getAllMethod() {
                return getAllMethod;
              }

              public MetaFindTestMethod findTestMethod() {
                return findTestMethod;
              }

              @Override
              public MetaProxy proxy(
                  Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                return new MetaUserSpaceProxy(invocations);
              }

              public static final class MetaSaveMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage.UserSpace, reactor.core.publisher.Mono<io.art.tarantool.test.model.User>> {
                private final MetaParameter<io.art.tarantool.test.model.User> requestParameter = register(new MetaParameter<>(0, "request",metaType(io.art.tarantool.test.model.User.class)));

                private MetaSaveMethod() {
                  super("save",metaType(reactor.core.publisher.Mono.class,metaType(io.art.tarantool.test.model.User.class)));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.save((io.art.tarantool.test.model.User)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.save((io.art.tarantool.test.model.User)(argument));
                }

                public MetaParameter<io.art.tarantool.test.model.User> requestParameter() {
                  return requestParameter;
                }
              }

              public static final class MetaGetMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage.UserSpace, reactor.core.publisher.Flux<io.art.tarantool.test.model.User>> {
                private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private MetaGetMethod() {
                  super("get",metaType(reactor.core.publisher.Flux.class,metaType(io.art.tarantool.test.model.User.class)));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.get((int)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.get((int)(argument));
                }

                public MetaParameter<Integer> idParameter() {
                  return idParameter;
                }
              }

              public static final class MetaDeleteMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage.UserSpace, io.art.tarantool.test.model.User> {
                private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private MetaDeleteMethod() {
                  super("delete",metaType(io.art.tarantool.test.model.User.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.delete((int)(arguments[0]));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object argument) throws Throwable {
                  return instance.delete((int)(argument));
                }

                public MetaParameter<Integer> idParameter() {
                  return idParameter;
                }
              }

              public static final class MetaGetAllMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage.UserSpace, java.util.stream.Stream<io.art.tarantool.test.model.User>> {
                private MetaGetAllMethod() {
                  super("getAll",metaType(java.util.stream.Stream.class,metaType(io.art.tarantool.test.model.User.class)));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getAll();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance)
                    throws Throwable {
                  return instance.getAll();
                }
              }

              public static final class MetaFindTestMethod extends InstanceMetaMethod<io.art.tarantool.test.UserStorage.UserSpace, reactor.core.publisher.Flux<java.util.List<io.art.tarantool.test.model.User>>> {
                private MetaFindTestMethod() {
                  super("findTest",metaType(reactor.core.publisher.Flux.class,metaType(java.util.List.class,metaType(io.art.tarantool.test.model.User.class))));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.findTest();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.UserStorage.UserSpace instance)
                    throws Throwable {
                  return instance.findTest();
                }
              }

              public class MetaUserSpaceProxy extends MetaProxy implements io.art.tarantool.test.UserStorage.UserSpace {
                private final Function<java.lang.Object, java.lang.Object> saveInvocation;

                private final Function<java.lang.Object, java.lang.Object> getInvocation;

                private final Function<java.lang.Object, java.lang.Object> deleteInvocation;

                private final Function<java.lang.Object, java.lang.Object> getAllInvocation;

                private final Function<java.lang.Object, java.lang.Object> findTestInvocation;

                public MetaUserSpaceProxy(
                    Map<MetaMethod<?>, Function<java.lang.Object, java.lang.Object>> invocations) {
                  super(invocations);
                  saveInvocation = invocations.get(saveMethod);
                  getInvocation = invocations.get(getMethod);
                  deleteInvocation = invocations.get(deleteMethod);
                  getAllInvocation = invocations.get(getAllMethod);
                  findTestInvocation = invocations.get(findTestMethod);
                }

                @Override
                public reactor.core.publisher.Mono<io.art.tarantool.test.model.User> save(
                    io.art.tarantool.test.model.User request) {
                  return (reactor.core.publisher.Mono<io.art.tarantool.test.model.User>)(saveInvocation.apply(request));
                }

                @Override
                public reactor.core.publisher.Flux<io.art.tarantool.test.model.User> get(int id) {
                  return (reactor.core.publisher.Flux<io.art.tarantool.test.model.User>)(getInvocation.apply(id));
                }

                @Override
                public io.art.tarantool.test.model.User delete(int id) {
                  return (io.art.tarantool.test.model.User)(deleteInvocation.apply(id));
                }

                @Override
                public java.util.stream.Stream<io.art.tarantool.test.model.User> getAll() {
                  return (java.util.stream.Stream<io.art.tarantool.test.model.User>)(getAllInvocation.apply(null));
                }

                @Override
                public reactor.core.publisher.Flux<java.util.List<io.art.tarantool.test.model.User>> findTest(
                    ) {
                  return (reactor.core.publisher.Flux<java.util.List<io.art.tarantool.test.model.User>>)(findTestInvocation.apply(null));
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

              private final MetaField<io.art.tarantool.test.model.User.Address> addressField = register(new MetaField<>("address",metaType(io.art.tarantool.test.model.User.Address.class),false));

              private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

              private final MetaGetIdMethod getIdMethod = register(new MetaGetIdMethod());

              private final MetaGetNameMethod getNameMethod = register(new MetaGetNameMethod());

              private final MetaGetAddressMethod getAddressMethod = register(new MetaGetAddressMethod());

              private final MetaUserBuilderClass userBuilderClass = register(new MetaUserBuilderClass());

              private final MetaAddressClass addressClass = register(new MetaAddressClass());

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

              public MetaField<io.art.tarantool.test.model.User.Address> addressField() {
                return addressField;
              }

              public MetaToBuilderMethod toBuilderMethod() {
                return toBuilderMethod;
              }

              public MetaGetIdMethod getIdMethod() {
                return getIdMethod;
              }

              public MetaGetNameMethod getNameMethod() {
                return getNameMethod;
              }

              public MetaGetAddressMethod getAddressMethod() {
                return getAddressMethod;
              }

              public MetaUserBuilderClass userBuilderClass() {
                return userBuilderClass;
              }

              public MetaAddressClass addressClass() {
                return addressClass;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.tarantool.test.model.User> {
                private final MetaParameter<Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private final MetaParameter<java.lang.String> nameParameter = register(new MetaParameter<>(1, "name",metaType(java.lang.String.class)));

                private final MetaParameter<io.art.tarantool.test.model.User.Address> addressParameter = register(new MetaParameter<>(2, "address",metaType(io.art.tarantool.test.model.User.Address.class)));

                private MetaConstructorConstructor() {
                  super(metaType(io.art.tarantool.test.model.User.class));
                }

                @Override
                public io.art.tarantool.test.model.User invoke(java.lang.Object[] arguments) throws
                    Throwable {
                  return new io.art.tarantool.test.model.User((int)(arguments[0]),(java.lang.String)(arguments[1]),(io.art.tarantool.test.model.User.Address)(arguments[2]));
                }

                public MetaParameter<Integer> idParameter() {
                  return idParameter;
                }

                public MetaParameter<java.lang.String> nameParameter() {
                  return nameParameter;
                }

                public MetaParameter<io.art.tarantool.test.model.User.Address> addressParameter() {
                  return addressParameter;
                }
              }

              public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User, io.art.tarantool.test.model.User.UserBuilder> {
                private MetaToBuilderMethod() {
                  super("toBuilder",metaType(io.art.tarantool.test.model.User.UserBuilder.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.toBuilder();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance) throws
                    Throwable {
                  return instance.toBuilder();
                }
              }

              public static final class MetaGetIdMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User, Integer> {
                private MetaGetIdMethod() {
                  super("getId",metaType(int.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getId();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance) throws
                    Throwable {
                  return instance.getId();
                }
              }

              public static final class MetaGetNameMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User, java.lang.String> {
                private MetaGetNameMethod() {
                  super("getName",metaType(java.lang.String.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getName();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance) throws
                    Throwable {
                  return instance.getName();
                }
              }

              public static final class MetaGetAddressMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User, io.art.tarantool.test.model.User.Address> {
                private MetaGetAddressMethod() {
                  super("getAddress",metaType(io.art.tarantool.test.model.User.Address.class));
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance,
                    java.lang.Object[] arguments) throws Throwable {
                  return instance.getAddress();
                }

                @Override
                public java.lang.Object invoke(io.art.tarantool.test.model.User instance) throws
                    Throwable {
                  return instance.getAddress();
                }
              }

              public static final class MetaUserBuilderClass extends MetaClass<io.art.tarantool.test.model.User.UserBuilder> {
                private final MetaField<Integer> idField = register(new MetaField<>("id",metaType(int.class),false));

                private final MetaField<java.lang.String> nameField = register(new MetaField<>("name",metaType(java.lang.String.class),false));

                private final MetaField<io.art.tarantool.test.model.User.Address> addressField = register(new MetaField<>("address",metaType(io.art.tarantool.test.model.User.Address.class),false));

                private final MetaIdMethod idMethod = register(new MetaIdMethod());

                private final MetaNameMethod nameMethod = register(new MetaNameMethod());

                private final MetaAddressMethod addressMethod = register(new MetaAddressMethod());

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

                public MetaField<io.art.tarantool.test.model.User.Address> addressField() {
                  return addressField;
                }

                public MetaIdMethod idMethod() {
                  return idMethod;
                }

                public MetaNameMethod nameMethod() {
                  return nameMethod;
                }

                public MetaAddressMethod addressMethod() {
                  return addressMethod;
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
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.id((int)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance,
                      java.lang.Object argument) throws Throwable {
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
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.name((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.name((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> nameParameter() {
                    return nameParameter;
                  }
                }

                public static final class MetaAddressMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.UserBuilder, io.art.tarantool.test.model.User.UserBuilder> {
                  private final MetaParameter<io.art.tarantool.test.model.User.Address> addressParameter = register(new MetaParameter<>(0, "address",metaType(io.art.tarantool.test.model.User.Address.class)));

                  private MetaAddressMethod() {
                    super("address",metaType(io.art.tarantool.test.model.User.UserBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.address((io.art.tarantool.test.model.User.Address)(arguments[0]));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance,
                      java.lang.Object argument) throws Throwable {
                    return instance.address((io.art.tarantool.test.model.User.Address)(argument));
                  }

                  public MetaParameter<io.art.tarantool.test.model.User.Address> addressParameter(
                      ) {
                    return addressParameter;
                  }
                }

                public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.UserBuilder, io.art.tarantool.test.model.User> {
                  private MetaBuildMethod() {
                    super("build",metaType(io.art.tarantool.test.model.User.class));
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.build();
                  }

                  @Override
                  public java.lang.Object invoke(
                      io.art.tarantool.test.model.User.UserBuilder instance) throws Throwable {
                    return instance.build();
                  }
                }
              }

              public static final class MetaAddressClass extends MetaClass<io.art.tarantool.test.model.User.Address> {
                private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

                private final MetaField<Integer> houseField = register(new MetaField<>("house",metaType(int.class),false));

                private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

                private final MetaGetHouseMethod getHouseMethod = register(new MetaGetHouseMethod());

                private final MetaAddressBuilderClass addressBuilderClass = register(new MetaAddressBuilderClass());

                private MetaAddressClass() {
                  super(metaType(io.art.tarantool.test.model.User.Address.class));
                }

                public MetaConstructorConstructor constructor() {
                  return constructor;
                }

                public MetaField<Integer> houseField() {
                  return houseField;
                }

                public MetaToBuilderMethod toBuilderMethod() {
                  return toBuilderMethod;
                }

                public MetaGetHouseMethod getHouseMethod() {
                  return getHouseMethod;
                }

                public MetaAddressBuilderClass addressBuilderClass() {
                  return addressBuilderClass;
                }

                public static final class MetaConstructorConstructor extends MetaConstructor<io.art.tarantool.test.model.User.Address> {
                  private final MetaParameter<Integer> houseParameter = register(new MetaParameter<>(0, "house",metaType(int.class)));

                  private MetaConstructorConstructor() {
                    super(metaType(io.art.tarantool.test.model.User.Address.class));
                  }

                  @Override
                  public io.art.tarantool.test.model.User.Address invoke(
                      java.lang.Object[] arguments) throws Throwable {
                    return new io.art.tarantool.test.model.User.Address((int)(arguments[0]));
                  }

                  @Override
                  public io.art.tarantool.test.model.User.Address invoke(java.lang.Object argument)
                      throws Throwable {
                    return new io.art.tarantool.test.model.User.Address((int)(argument));
                  }

                  public MetaParameter<Integer> houseParameter() {
                    return houseParameter;
                  }
                }

                public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.Address, io.art.tarantool.test.model.User.Address.AddressBuilder> {
                  private MetaToBuilderMethod() {
                    super("toBuilder",metaType(io.art.tarantool.test.model.User.Address.AddressBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.tarantool.test.model.User.Address instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.toBuilder();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.tarantool.test.model.User.Address instance)
                      throws Throwable {
                    return instance.toBuilder();
                  }
                }

                public static final class MetaGetHouseMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.Address, Integer> {
                  private MetaGetHouseMethod() {
                    super("getHouse",metaType(int.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.tarantool.test.model.User.Address instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getHouse();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.tarantool.test.model.User.Address instance)
                      throws Throwable {
                    return instance.getHouse();
                  }
                }

                public static final class MetaAddressBuilderClass extends MetaClass<io.art.tarantool.test.model.User.Address.AddressBuilder> {
                  private final MetaField<Integer> houseField = register(new MetaField<>("house",metaType(int.class),false));

                  private final MetaHouseMethod houseMethod = register(new MetaHouseMethod());

                  private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

                  private MetaAddressBuilderClass() {
                    super(metaType(io.art.tarantool.test.model.User.Address.AddressBuilder.class));
                  }

                  public MetaField<Integer> houseField() {
                    return houseField;
                  }

                  public MetaHouseMethod houseMethod() {
                    return houseMethod;
                  }

                  public MetaBuildMethod buildMethod() {
                    return buildMethod;
                  }

                  public static final class MetaHouseMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.Address.AddressBuilder, io.art.tarantool.test.model.User.Address.AddressBuilder> {
                    private final MetaParameter<Integer> houseParameter = register(new MetaParameter<>(0, "house",metaType(int.class)));

                    private MetaHouseMethod() {
                      super("house",metaType(io.art.tarantool.test.model.User.Address.AddressBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.User.Address.AddressBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.house((int)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.User.Address.AddressBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.house((int)(argument));
                    }

                    public MetaParameter<Integer> houseParameter() {
                      return houseParameter;
                    }
                  }

                  public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.tarantool.test.model.User.Address.AddressBuilder, io.art.tarantool.test.model.User.Address> {
                    private MetaBuildMethod() {
                      super("build",metaType(io.art.tarantool.test.model.User.Address.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.User.Address.AddressBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.build();
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.tarantool.test.model.User.Address.AddressBuilder instance) throws
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
}
