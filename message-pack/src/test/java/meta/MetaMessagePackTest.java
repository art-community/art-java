package meta;

import static io.art.meta.model.MetaType.metaArray;
import static io.art.meta.model.MetaType.metaEnum;
import static io.art.meta.model.MetaType.metaType;

import io.art.meta.model.InstanceMetaMethod;
import io.art.meta.model.MetaClass;
import io.art.meta.model.MetaConstructor;
import io.art.meta.model.MetaField;
import io.art.meta.model.MetaLibrary;
import io.art.meta.model.MetaPackage;
import io.art.meta.model.MetaParameter;
import io.art.meta.model.StaticMetaMethod;

@SuppressWarnings({"all","unchecked","unused"})
public class MetaMessagePackTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = register(new MetaIoPackage());

  public MetaMessagePackTest(MetaLibrary... dependencies) {
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
      private final MetaMessagePackage messagePackage = register(new MetaMessagePackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaMessagePackage messagePackage() {
        return messagePackage;
      }

      public static final class MetaMessagePackage extends MetaPackage {
        private final MetaPackPackage packPackage = register(new MetaPackPackage());

        private MetaMessagePackage() {
          super("message");
        }

        public MetaPackPackage packPackage() {
          return packPackage;
        }

        public static final class MetaPackPackage extends MetaPackage {
          private final MetaTestPackage testPackage = register(new MetaTestPackage());

          private MetaPackPackage() {
            super("pack");
          }

          public MetaTestPackage testPackage() {
            return testPackage;
          }

          public static final class MetaTestPackage extends MetaPackage {
            private final MetaMessagePackTestClass messagePackTestClass = register(new MetaMessagePackTestClass());

            private final MetaGeneratorPackage generatorPackage = register(new MetaGeneratorPackage());

            private final MetaModelPackage modelPackage = register(new MetaModelPackage());

            private MetaTestPackage() {
              super("test");
            }

            public MetaMessagePackTestClass messagePackTestClass() {
              return messagePackTestClass;
            }

            public MetaGeneratorPackage generatorPackage() {
              return generatorPackage;
            }

            public MetaModelPackage modelPackage() {
              return modelPackage;
            }

            public static final class MetaMessagePackTestClass extends MetaClass<io.art.message.pack.test.MessagePackTest> {
              private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

              private final MetaSetupMethod setupMethod = register(new MetaSetupMethod());

              private final MetaTestMessagePackReadMethod testMessagePackReadMethod = register(new MetaTestMessagePackReadMethod());

              private MetaMessagePackTestClass() {
                super(metaType(io.art.message.pack.test.MessagePackTest.class));
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaSetupMethod setupMethod() {
                return setupMethod;
              }

              public MetaTestMessagePackReadMethod testMessagePackReadMethod() {
                return testMessagePackReadMethod;
              }

              public static final class MetaConstructorConstructor extends MetaConstructor<io.art.message.pack.test.MessagePackTest> {
                private MetaConstructorConstructor() {
                  super(metaType(io.art.message.pack.test.MessagePackTest.class));
                }

                @Override
                public io.art.message.pack.test.MessagePackTest invoke(java.lang.Object[] arguments)
                    throws Throwable {
                  return new io.art.message.pack.test.MessagePackTest();
                }

                @Override
                public io.art.message.pack.test.MessagePackTest invoke() throws Throwable {
                  return new io.art.message.pack.test.MessagePackTest();
                }
              }

              public static final class MetaSetupMethod extends StaticMetaMethod<Void> {
                private MetaSetupMethod() {
                  super("setup",metaType(Void.class));
                }

                @Override
                public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                  io.art.message.pack.test.MessagePackTest.setup();
                  return null;
                }

                @Override
                public java.lang.Object invoke() throws Throwable {
                  io.art.message.pack.test.MessagePackTest.setup();
                  return null;
                }
              }

              public static final class MetaTestMessagePackReadMethod extends InstanceMetaMethod<io.art.message.pack.test.MessagePackTest, Void> {
                private MetaTestMessagePackReadMethod() {
                  super("testMessagePackRead",metaType(Void.class));
                }

                @Override
                public java.lang.Object invoke(io.art.message.pack.test.MessagePackTest instance,
                    java.lang.Object[] arguments) throws Throwable {
                  instance.testMessagePackRead();
                  return null;
                }

                @Override
                public java.lang.Object invoke(io.art.message.pack.test.MessagePackTest instance)
                    throws Throwable {
                  instance.testMessagePackRead();
                  return null;
                }
              }
            }

            public static final class MetaGeneratorPackage extends MetaPackage {
              private final MetaModelGeneratorClass modelGeneratorClass = register(new MetaModelGeneratorClass());

              private MetaGeneratorPackage() {
                super("generator");
              }

              public MetaModelGeneratorClass modelGeneratorClass() {
                return modelGeneratorClass;
              }

              public static final class MetaModelGeneratorClass extends MetaClass<io.art.message.pack.test.generator.ModelGenerator> {
                private final MetaGenerateModelMethod generateModelMethod = register(new MetaGenerateModelMethod());

                private MetaModelGeneratorClass() {
                  super(metaType(io.art.message.pack.test.generator.ModelGenerator.class));
                }

                public MetaGenerateModelMethod generateModelMethod() {
                  return generateModelMethod;
                }

                public static final class MetaGenerateModelMethod extends StaticMetaMethod<io.art.message.pack.test.model.Model> {
                  private MetaGenerateModelMethod() {
                    super("generateModel",metaType(io.art.message.pack.test.model.Model.class));
                  }

                  @Override
                  public java.lang.Object invoke(java.lang.Object[] arguments) throws Throwable {
                    return io.art.message.pack.test.generator.ModelGenerator.generateModel();
                  }

                  @Override
                  public java.lang.Object invoke() throws Throwable {
                    return io.art.message.pack.test.generator.ModelGenerator.generateModel();
                  }
                }
              }
            }

            public static final class MetaModelPackage extends MetaPackage {
              private final MetaModelClass modelClass = register(new MetaModelClass());

              private MetaModelPackage() {
                super("model");
              }

              public MetaModelClass modelClass() {
                return modelClass;
              }

              public static final class MetaModelClass extends MetaClass<io.art.message.pack.test.model.Model> {
                private final MetaConstructorConstructor constructor = register(new MetaConstructorConstructor());

                private final MetaField<reactor.core.publisher.Mono<java.lang.String>> mono1Field = register(new MetaField<>("mono1",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),false));

                private final MetaField<reactor.core.publisher.Flux<java.lang.String>> fluxField = register(new MetaField<>("flux",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),false));

                private final MetaField<java.util.stream.Stream<java.lang.String>> streamField = register(new MetaField<>("stream",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),false));

                private final MetaField<reactor.core.publisher.Mono<java.lang.String[]>> mono2Field = register(new MetaField<>("mono2",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false));

                private final MetaField<java.lang.String> nullFieldField = register(new MetaField<>("nullField",metaType(java.lang.String.class),false));

                private final MetaField<java.lang.String> emptyStringField = register(new MetaField<>("emptyString",metaType(java.lang.String.class),false));

                private final MetaField<java.lang.Integer> f1Field = register(new MetaField<>("f1",metaType(int.class),false));

                private final MetaField<java.lang.Short> f2Field = register(new MetaField<>("f2",metaType(short.class),false));

                private final MetaField<java.lang.Double> f3Field = register(new MetaField<>("f3",metaType(double.class),false));

                private final MetaField<java.lang.Float> f4Field = register(new MetaField<>("f4",metaType(float.class),false));

                private final MetaField<java.lang.Long> f5Field = register(new MetaField<>("f5",metaType(long.class),false));

                private final MetaField<java.lang.Boolean> f6Field = register(new MetaField<>("f6",metaType(boolean.class),false));

                private final MetaField<java.lang.Character> f7Field = register(new MetaField<>("f7",metaType(char.class),false));

                private final MetaField<Byte> f8Field = register(new MetaField<>("f8",metaType(byte.class),false));

                private final MetaField<java.lang.Integer> f9Field = register(new MetaField<>("f9",metaType(java.lang.Integer.class),false));

                private final MetaField<java.lang.Short> f10Field = register(new MetaField<>("f10",metaType(java.lang.Short.class),false));

                private final MetaField<java.lang.Double> f11Field = register(new MetaField<>("f11",metaType(java.lang.Double.class),false));

                private final MetaField<java.lang.Float> f12Field = register(new MetaField<>("f12",metaType(java.lang.Float.class),false));

                private final MetaField<java.lang.Long> f13Field = register(new MetaField<>("f13",metaType(java.lang.Long.class),false));

                private final MetaField<java.lang.Boolean> f14Field = register(new MetaField<>("f14",metaType(java.lang.Boolean.class),false));

                private final MetaField<java.lang.Character> f15Field = register(new MetaField<>("f15",metaType(java.lang.Character.class),false));

                private final MetaField<java.lang.String> f16Field = register(new MetaField<>("f16",metaType(java.lang.String.class),false));

                private final MetaField<int[]> f17Field = register(new MetaField<>("f17",metaArray(int[].class, int[]::new, metaType(int.class)),false));

                private final MetaField<short[]> f18Field = register(new MetaField<>("f18",metaArray(short[].class, short[]::new, metaType(short.class)),false));

                private final MetaField<double[]> f19Field = register(new MetaField<>("f19",metaArray(double[].class, double[]::new, metaType(double.class)),false));

                private final MetaField<float[]> f20Field = register(new MetaField<>("f20",metaArray(float[].class, float[]::new, metaType(float.class)),false));

                private final MetaField<long[]> f21Field = register(new MetaField<>("f21",metaArray(long[].class, long[]::new, metaType(long.class)),false));

                private final MetaField<boolean[]> f22Field = register(new MetaField<>("f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),false));

                private final MetaField<char[]> f23Field = register(new MetaField<>("f23",metaArray(char[].class, char[]::new, metaType(char.class)),false));

                private final MetaField<byte[]> f24Field = register(new MetaField<>("f24",metaArray(byte[].class, byte[]::new, metaType(byte.class)),false));

                private final MetaField<java.lang.Integer[]> f25Field = register(new MetaField<>("f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),false));

                private final MetaField<java.lang.Short[]> f26Field = register(new MetaField<>("f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),false));

                private final MetaField<java.lang.Double[]> f27Field = register(new MetaField<>("f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),false));

                private final MetaField<java.lang.Float[]> f28Field = register(new MetaField<>("f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),false));

                private final MetaField<java.lang.Long[]> f29Field = register(new MetaField<>("f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),false));

                private final MetaField<java.lang.Boolean[]> f30Field = register(new MetaField<>("f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),false));

                private final MetaField<java.lang.Character[]> f31Field = register(new MetaField<>("f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),false));

                private final MetaField<java.lang.String[]> f32Field = register(new MetaField<>("f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),false));

                private final MetaField<java.util.List<java.lang.String>> f33Field = register(new MetaField<>("f33",metaType(java.util.List.class,metaType(java.lang.String.class)),false));

                private final MetaField<java.util.Set<java.lang.String>> f34Field = register(new MetaField<>("f34",metaType(java.util.Set.class,metaType(java.lang.String.class)),false));

                private final MetaField<java.util.Collection<java.lang.String>> f35Field = register(new MetaField<>("f35",metaType(java.util.Collection.class,metaType(java.lang.String.class)),false));

                private final MetaField<io.art.core.collection.ImmutableArray<java.lang.String>> f36Field = register(new MetaField<>("f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),false));

                private final MetaField<io.art.core.collection.ImmutableSet<java.lang.String>> f37Field = register(new MetaField<>("f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),false));

                private final MetaField<java.util.function.Supplier<java.lang.String>> f38Field = register(new MetaField<>("f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),false));

                private final MetaField<io.art.core.property.LazyProperty<java.lang.String>> f39Field = register(new MetaField<>("f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),false));

                private final MetaField<java.util.List<int[]>> f40Field = register(new MetaField<>("f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),false));

                private final MetaField<java.util.List<byte[]>> f41Field = register(new MetaField<>("f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))),false));

                private final MetaField<java.util.List<java.lang.String[]>> f42Field = register(new MetaField<>("f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false));

                private final MetaField<java.util.List<byte[]>[]> f43Field = register(new MetaField<>("f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))),false));

                private final MetaField<java.util.List<int[]>[]> f44Field = register(new MetaField<>("f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),false));

                private final MetaField<java.util.List<java.lang.String[]>[]> f45Field = register(new MetaField<>("f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),false));

                private final MetaField<java.util.List<java.util.List<java.lang.String>>> f46Field = register(new MetaField<>("f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),false));

                private final MetaField<java.util.List<java.util.List<java.lang.String>[]>> f47Field = register(new MetaField<>("f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),false));

                private final MetaField<java.util.Map<java.lang.Integer, java.lang.String>> f48Field = register(new MetaField<>("f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class)),false));

                private final MetaField<java.util.Map<java.lang.String, java.lang.String[]>> f49Field = register(new MetaField<>("f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false));

                private final MetaField<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field = register(new MetaField<>("f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),false));

                private final MetaField<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field = register(new MetaField<>("f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false));

                private final MetaField<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field = register(new MetaField<>("f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false));

                private final MetaField<io.art.message.pack.test.model.Model> f53Field = register(new MetaField<>("f53",metaType(io.art.message.pack.test.model.Model.class),false));

                private final MetaField<java.time.LocalDateTime> f54Field = register(new MetaField<>("f54",metaType(java.time.LocalDateTime.class),false));

                private final MetaField<java.time.ZonedDateTime> f55Field = register(new MetaField<>("f55",metaType(java.time.ZonedDateTime.class),false));

                private final MetaField<java.time.Duration> f56Field = register(new MetaField<>("f56",metaType(java.time.Duration.class),false));

                private final MetaField<io.art.message.pack.test.model.Model.ModelEnum> f57Field = register(new MetaField<>("f57",metaEnum(io.art.message.pack.test.model.Model.ModelEnum.class, io.art.message.pack.test.model.Model.ModelEnum::valueOf),false));

                private final MetaField<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field = register(new MetaField<>("f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),false));

                private final MetaField<java.util.List<io.art.message.pack.test.model.Model>> f59Field = register(new MetaField<>("f59",metaType(java.util.List.class,metaType(io.art.message.pack.test.model.Model.class)),false));

                private final MetaField<java.util.Set<io.art.message.pack.test.model.Model>> f60Field = register(new MetaField<>("f60",metaType(java.util.Set.class,metaType(io.art.message.pack.test.model.Model.class)),false));

                private final MetaField<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Field = register(new MetaField<>("f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.message.pack.test.model.Model.class)),false));

                private final MetaToBuilderMethod toBuilderMethod = register(new MetaToBuilderMethod());

                private final MetaGetMono1Method getMono1Method = register(new MetaGetMono1Method());

                private final MetaGetFluxMethod getFluxMethod = register(new MetaGetFluxMethod());

                private final MetaGetStreamMethod getStreamMethod = register(new MetaGetStreamMethod());

                private final MetaGetMono2Method getMono2Method = register(new MetaGetMono2Method());

                private final MetaGetNullFieldMethod getNullFieldMethod = register(new MetaGetNullFieldMethod());

                private final MetaGetEmptyStringMethod getEmptyStringMethod = register(new MetaGetEmptyStringMethod());

                private final MetaGetF1Method getF1Method = register(new MetaGetF1Method());

                private final MetaGetF2Method getF2Method = register(new MetaGetF2Method());

                private final MetaGetF3Method getF3Method = register(new MetaGetF3Method());

                private final MetaGetF4Method getF4Method = register(new MetaGetF4Method());

                private final MetaGetF5Method getF5Method = register(new MetaGetF5Method());

                private final MetaIsF6Method isF6Method = register(new MetaIsF6Method());

                private final MetaGetF7Method getF7Method = register(new MetaGetF7Method());

                private final MetaGetF8Method getF8Method = register(new MetaGetF8Method());

                private final MetaGetF9Method getF9Method = register(new MetaGetF9Method());

                private final MetaGetF10Method getF10Method = register(new MetaGetF10Method());

                private final MetaGetF11Method getF11Method = register(new MetaGetF11Method());

                private final MetaGetF12Method getF12Method = register(new MetaGetF12Method());

                private final MetaGetF13Method getF13Method = register(new MetaGetF13Method());

                private final MetaGetF14Method getF14Method = register(new MetaGetF14Method());

                private final MetaGetF15Method getF15Method = register(new MetaGetF15Method());

                private final MetaGetF16Method getF16Method = register(new MetaGetF16Method());

                private final MetaGetF17Method getF17Method = register(new MetaGetF17Method());

                private final MetaGetF18Method getF18Method = register(new MetaGetF18Method());

                private final MetaGetF19Method getF19Method = register(new MetaGetF19Method());

                private final MetaGetF20Method getF20Method = register(new MetaGetF20Method());

                private final MetaGetF21Method getF21Method = register(new MetaGetF21Method());

                private final MetaGetF22Method getF22Method = register(new MetaGetF22Method());

                private final MetaGetF23Method getF23Method = register(new MetaGetF23Method());

                private final MetaGetF24Method getF24Method = register(new MetaGetF24Method());

                private final MetaGetF25Method getF25Method = register(new MetaGetF25Method());

                private final MetaGetF26Method getF26Method = register(new MetaGetF26Method());

                private final MetaGetF27Method getF27Method = register(new MetaGetF27Method());

                private final MetaGetF28Method getF28Method = register(new MetaGetF28Method());

                private final MetaGetF29Method getF29Method = register(new MetaGetF29Method());

                private final MetaGetF30Method getF30Method = register(new MetaGetF30Method());

                private final MetaGetF31Method getF31Method = register(new MetaGetF31Method());

                private final MetaGetF32Method getF32Method = register(new MetaGetF32Method());

                private final MetaGetF33Method getF33Method = register(new MetaGetF33Method());

                private final MetaGetF34Method getF34Method = register(new MetaGetF34Method());

                private final MetaGetF35Method getF35Method = register(new MetaGetF35Method());

                private final MetaGetF36Method getF36Method = register(new MetaGetF36Method());

                private final MetaGetF37Method getF37Method = register(new MetaGetF37Method());

                private final MetaGetF38Method getF38Method = register(new MetaGetF38Method());

                private final MetaGetF39Method getF39Method = register(new MetaGetF39Method());

                private final MetaGetF40Method getF40Method = register(new MetaGetF40Method());

                private final MetaGetF41Method getF41Method = register(new MetaGetF41Method());

                private final MetaGetF42Method getF42Method = register(new MetaGetF42Method());

                private final MetaGetF43Method getF43Method = register(new MetaGetF43Method());

                private final MetaGetF44Method getF44Method = register(new MetaGetF44Method());

                private final MetaGetF45Method getF45Method = register(new MetaGetF45Method());

                private final MetaGetF46Method getF46Method = register(new MetaGetF46Method());

                private final MetaGetF47Method getF47Method = register(new MetaGetF47Method());

                private final MetaGetF48Method getF48Method = register(new MetaGetF48Method());

                private final MetaGetF49Method getF49Method = register(new MetaGetF49Method());

                private final MetaGetF50Method getF50Method = register(new MetaGetF50Method());

                private final MetaGetF51Method getF51Method = register(new MetaGetF51Method());

                private final MetaGetF52Method getF52Method = register(new MetaGetF52Method());

                private final MetaGetF53Method getF53Method = register(new MetaGetF53Method());

                private final MetaGetF54Method getF54Method = register(new MetaGetF54Method());

                private final MetaGetF55Method getF55Method = register(new MetaGetF55Method());

                private final MetaGetF56Method getF56Method = register(new MetaGetF56Method());

                private final MetaGetF57Method getF57Method = register(new MetaGetF57Method());

                private final MetaGetF58Method getF58Method = register(new MetaGetF58Method());

                private final MetaGetF59Method getF59Method = register(new MetaGetF59Method());

                private final MetaGetF60Method getF60Method = register(new MetaGetF60Method());

                private final MetaGetF61Method getF61Method = register(new MetaGetF61Method());

                private final MetaModelBuilderClass modelBuilderClass = register(new MetaModelBuilderClass());

                private MetaModelClass() {
                  super(metaType(io.art.message.pack.test.model.Model.class));
                }

                public MetaConstructorConstructor constructor() {
                  return constructor;
                }

                public MetaField<reactor.core.publisher.Mono<java.lang.String>> mono1Field() {
                  return mono1Field;
                }

                public MetaField<reactor.core.publisher.Flux<java.lang.String>> fluxField() {
                  return fluxField;
                }

                public MetaField<java.util.stream.Stream<java.lang.String>> streamField() {
                  return streamField;
                }

                public MetaField<reactor.core.publisher.Mono<java.lang.String[]>> mono2Field() {
                  return mono2Field;
                }

                public MetaField<java.lang.String> nullFieldField() {
                  return nullFieldField;
                }

                public MetaField<java.lang.String> emptyStringField() {
                  return emptyStringField;
                }

                public MetaField<java.lang.Integer> f1Field() {
                  return f1Field;
                }

                public MetaField<java.lang.Short> f2Field() {
                  return f2Field;
                }

                public MetaField<java.lang.Double> f3Field() {
                  return f3Field;
                }

                public MetaField<java.lang.Float> f4Field() {
                  return f4Field;
                }

                public MetaField<java.lang.Long> f5Field() {
                  return f5Field;
                }

                public MetaField<java.lang.Boolean> f6Field() {
                  return f6Field;
                }

                public MetaField<java.lang.Character> f7Field() {
                  return f7Field;
                }

                public MetaField<Byte> f8Field() {
                  return f8Field;
                }

                public MetaField<java.lang.Integer> f9Field() {
                  return f9Field;
                }

                public MetaField<java.lang.Short> f10Field() {
                  return f10Field;
                }

                public MetaField<java.lang.Double> f11Field() {
                  return f11Field;
                }

                public MetaField<java.lang.Float> f12Field() {
                  return f12Field;
                }

                public MetaField<java.lang.Long> f13Field() {
                  return f13Field;
                }

                public MetaField<java.lang.Boolean> f14Field() {
                  return f14Field;
                }

                public MetaField<java.lang.Character> f15Field() {
                  return f15Field;
                }

                public MetaField<java.lang.String> f16Field() {
                  return f16Field;
                }

                public MetaField<int[]> f17Field() {
                  return f17Field;
                }

                public MetaField<short[]> f18Field() {
                  return f18Field;
                }

                public MetaField<double[]> f19Field() {
                  return f19Field;
                }

                public MetaField<float[]> f20Field() {
                  return f20Field;
                }

                public MetaField<long[]> f21Field() {
                  return f21Field;
                }

                public MetaField<boolean[]> f22Field() {
                  return f22Field;
                }

                public MetaField<char[]> f23Field() {
                  return f23Field;
                }

                public MetaField<byte[]> f24Field() {
                  return f24Field;
                }

                public MetaField<java.lang.Integer[]> f25Field() {
                  return f25Field;
                }

                public MetaField<java.lang.Short[]> f26Field() {
                  return f26Field;
                }

                public MetaField<java.lang.Double[]> f27Field() {
                  return f27Field;
                }

                public MetaField<java.lang.Float[]> f28Field() {
                  return f28Field;
                }

                public MetaField<java.lang.Long[]> f29Field() {
                  return f29Field;
                }

                public MetaField<java.lang.Boolean[]> f30Field() {
                  return f30Field;
                }

                public MetaField<java.lang.Character[]> f31Field() {
                  return f31Field;
                }

                public MetaField<java.lang.String[]> f32Field() {
                  return f32Field;
                }

                public MetaField<java.util.List<java.lang.String>> f33Field() {
                  return f33Field;
                }

                public MetaField<java.util.Set<java.lang.String>> f34Field() {
                  return f34Field;
                }

                public MetaField<java.util.Collection<java.lang.String>> f35Field() {
                  return f35Field;
                }

                public MetaField<io.art.core.collection.ImmutableArray<java.lang.String>> f36Field(
                    ) {
                  return f36Field;
                }

                public MetaField<io.art.core.collection.ImmutableSet<java.lang.String>> f37Field() {
                  return f37Field;
                }

                public MetaField<java.util.function.Supplier<java.lang.String>> f38Field() {
                  return f38Field;
                }

                public MetaField<io.art.core.property.LazyProperty<java.lang.String>> f39Field() {
                  return f39Field;
                }

                public MetaField<java.util.List<int[]>> f40Field() {
                  return f40Field;
                }

                public MetaField<java.util.List<byte[]>> f41Field() {
                  return f41Field;
                }

                public MetaField<java.util.List<java.lang.String[]>> f42Field() {
                  return f42Field;
                }

                public MetaField<java.util.List<byte[]>[]> f43Field() {
                  return f43Field;
                }

                public MetaField<java.util.List<int[]>[]> f44Field() {
                  return f44Field;
                }

                public MetaField<java.util.List<java.lang.String[]>[]> f45Field() {
                  return f45Field;
                }

                public MetaField<java.util.List<java.util.List<java.lang.String>>> f46Field() {
                  return f46Field;
                }

                public MetaField<java.util.List<java.util.List<java.lang.String>[]>> f47Field() {
                  return f47Field;
                }

                public MetaField<java.util.Map<java.lang.Integer, java.lang.String>> f48Field() {
                  return f48Field;
                }

                public MetaField<java.util.Map<java.lang.String, java.lang.String[]>> f49Field() {
                  return f49Field;
                }

                public MetaField<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field(
                    ) {
                  return f50Field;
                }

                public MetaField<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field(
                    ) {
                  return f51Field;
                }

                public MetaField<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field(
                    ) {
                  return f52Field;
                }

                public MetaField<io.art.message.pack.test.model.Model> f53Field() {
                  return f53Field;
                }

                public MetaField<java.time.LocalDateTime> f54Field() {
                  return f54Field;
                }

                public MetaField<java.time.ZonedDateTime> f55Field() {
                  return f55Field;
                }

                public MetaField<java.time.Duration> f56Field() {
                  return f56Field;
                }

                public MetaField<io.art.message.pack.test.model.Model.ModelEnum> f57Field() {
                  return f57Field;
                }

                public MetaField<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field(
                    ) {
                  return f58Field;
                }

                public MetaField<java.util.List<io.art.message.pack.test.model.Model>> f59Field() {
                  return f59Field;
                }

                public MetaField<java.util.Set<io.art.message.pack.test.model.Model>> f60Field() {
                  return f60Field;
                }

                public MetaField<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Field(
                    ) {
                  return f61Field;
                }

                public MetaToBuilderMethod toBuilderMethod() {
                  return toBuilderMethod;
                }

                public MetaGetMono1Method getMono1Method() {
                  return getMono1Method;
                }

                public MetaGetFluxMethod getFluxMethod() {
                  return getFluxMethod;
                }

                public MetaGetStreamMethod getStreamMethod() {
                  return getStreamMethod;
                }

                public MetaGetMono2Method getMono2Method() {
                  return getMono2Method;
                }

                public MetaGetNullFieldMethod getNullFieldMethod() {
                  return getNullFieldMethod;
                }

                public MetaGetEmptyStringMethod getEmptyStringMethod() {
                  return getEmptyStringMethod;
                }

                public MetaGetF1Method getF1Method() {
                  return getF1Method;
                }

                public MetaGetF2Method getF2Method() {
                  return getF2Method;
                }

                public MetaGetF3Method getF3Method() {
                  return getF3Method;
                }

                public MetaGetF4Method getF4Method() {
                  return getF4Method;
                }

                public MetaGetF5Method getF5Method() {
                  return getF5Method;
                }

                public MetaIsF6Method isF6Method() {
                  return isF6Method;
                }

                public MetaGetF7Method getF7Method() {
                  return getF7Method;
                }

                public MetaGetF8Method getF8Method() {
                  return getF8Method;
                }

                public MetaGetF9Method getF9Method() {
                  return getF9Method;
                }

                public MetaGetF10Method getF10Method() {
                  return getF10Method;
                }

                public MetaGetF11Method getF11Method() {
                  return getF11Method;
                }

                public MetaGetF12Method getF12Method() {
                  return getF12Method;
                }

                public MetaGetF13Method getF13Method() {
                  return getF13Method;
                }

                public MetaGetF14Method getF14Method() {
                  return getF14Method;
                }

                public MetaGetF15Method getF15Method() {
                  return getF15Method;
                }

                public MetaGetF16Method getF16Method() {
                  return getF16Method;
                }

                public MetaGetF17Method getF17Method() {
                  return getF17Method;
                }

                public MetaGetF18Method getF18Method() {
                  return getF18Method;
                }

                public MetaGetF19Method getF19Method() {
                  return getF19Method;
                }

                public MetaGetF20Method getF20Method() {
                  return getF20Method;
                }

                public MetaGetF21Method getF21Method() {
                  return getF21Method;
                }

                public MetaGetF22Method getF22Method() {
                  return getF22Method;
                }

                public MetaGetF23Method getF23Method() {
                  return getF23Method;
                }

                public MetaGetF24Method getF24Method() {
                  return getF24Method;
                }

                public MetaGetF25Method getF25Method() {
                  return getF25Method;
                }

                public MetaGetF26Method getF26Method() {
                  return getF26Method;
                }

                public MetaGetF27Method getF27Method() {
                  return getF27Method;
                }

                public MetaGetF28Method getF28Method() {
                  return getF28Method;
                }

                public MetaGetF29Method getF29Method() {
                  return getF29Method;
                }

                public MetaGetF30Method getF30Method() {
                  return getF30Method;
                }

                public MetaGetF31Method getF31Method() {
                  return getF31Method;
                }

                public MetaGetF32Method getF32Method() {
                  return getF32Method;
                }

                public MetaGetF33Method getF33Method() {
                  return getF33Method;
                }

                public MetaGetF34Method getF34Method() {
                  return getF34Method;
                }

                public MetaGetF35Method getF35Method() {
                  return getF35Method;
                }

                public MetaGetF36Method getF36Method() {
                  return getF36Method;
                }

                public MetaGetF37Method getF37Method() {
                  return getF37Method;
                }

                public MetaGetF38Method getF38Method() {
                  return getF38Method;
                }

                public MetaGetF39Method getF39Method() {
                  return getF39Method;
                }

                public MetaGetF40Method getF40Method() {
                  return getF40Method;
                }

                public MetaGetF41Method getF41Method() {
                  return getF41Method;
                }

                public MetaGetF42Method getF42Method() {
                  return getF42Method;
                }

                public MetaGetF43Method getF43Method() {
                  return getF43Method;
                }

                public MetaGetF44Method getF44Method() {
                  return getF44Method;
                }

                public MetaGetF45Method getF45Method() {
                  return getF45Method;
                }

                public MetaGetF46Method getF46Method() {
                  return getF46Method;
                }

                public MetaGetF47Method getF47Method() {
                  return getF47Method;
                }

                public MetaGetF48Method getF48Method() {
                  return getF48Method;
                }

                public MetaGetF49Method getF49Method() {
                  return getF49Method;
                }

                public MetaGetF50Method getF50Method() {
                  return getF50Method;
                }

                public MetaGetF51Method getF51Method() {
                  return getF51Method;
                }

                public MetaGetF52Method getF52Method() {
                  return getF52Method;
                }

                public MetaGetF53Method getF53Method() {
                  return getF53Method;
                }

                public MetaGetF54Method getF54Method() {
                  return getF54Method;
                }

                public MetaGetF55Method getF55Method() {
                  return getF55Method;
                }

                public MetaGetF56Method getF56Method() {
                  return getF56Method;
                }

                public MetaGetF57Method getF57Method() {
                  return getF57Method;
                }

                public MetaGetF58Method getF58Method() {
                  return getF58Method;
                }

                public MetaGetF59Method getF59Method() {
                  return getF59Method;
                }

                public MetaGetF60Method getF60Method() {
                  return getF60Method;
                }

                public MetaGetF61Method getF61Method() {
                  return getF61Method;
                }

                public MetaModelBuilderClass modelBuilderClass() {
                  return modelBuilderClass;
                }

                public static final class MetaConstructorConstructor extends MetaConstructor<io.art.message.pack.test.model.Model> {
                  private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> mono1Parameter = register(new MetaParameter<>(0, "mono1",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                  private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> fluxParameter = register(new MetaParameter<>(1, "flux",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                  private final MetaParameter<java.util.stream.Stream<java.lang.String>> streamParameter = register(new MetaParameter<>(2, "stream",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class))));

                  private final MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> mono2Parameter = register(new MetaParameter<>(3, "mono2",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                  private final MetaParameter<java.lang.String> nullFieldParameter = register(new MetaParameter<>(4, "nullField",metaType(java.lang.String.class)));

                  private final MetaParameter<java.lang.String> emptyStringParameter = register(new MetaParameter<>(5, "emptyString",metaType(java.lang.String.class)));

                  private final MetaParameter<java.lang.Integer> f1Parameter = register(new MetaParameter<>(6, "f1",metaType(int.class)));

                  private final MetaParameter<java.lang.Short> f2Parameter = register(new MetaParameter<>(7, "f2",metaType(short.class)));

                  private final MetaParameter<java.lang.Double> f3Parameter = register(new MetaParameter<>(8, "f3",metaType(double.class)));

                  private final MetaParameter<java.lang.Float> f4Parameter = register(new MetaParameter<>(9, "f4",metaType(float.class)));

                  private final MetaParameter<java.lang.Long> f5Parameter = register(new MetaParameter<>(10, "f5",metaType(long.class)));

                  private final MetaParameter<java.lang.Boolean> f6Parameter = register(new MetaParameter<>(11, "f6",metaType(boolean.class)));

                  private final MetaParameter<java.lang.Character> f7Parameter = register(new MetaParameter<>(12, "f7",metaType(char.class)));

                  private final MetaParameter<Byte> f8Parameter = register(new MetaParameter<>(13, "f8",metaType(byte.class)));

                  private final MetaParameter<java.lang.Integer> f9Parameter = register(new MetaParameter<>(14, "f9",metaType(java.lang.Integer.class)));

                  private final MetaParameter<java.lang.Short> f10Parameter = register(new MetaParameter<>(15, "f10",metaType(java.lang.Short.class)));

                  private final MetaParameter<java.lang.Double> f11Parameter = register(new MetaParameter<>(16, "f11",metaType(java.lang.Double.class)));

                  private final MetaParameter<java.lang.Float> f12Parameter = register(new MetaParameter<>(17, "f12",metaType(java.lang.Float.class)));

                  private final MetaParameter<java.lang.Long> f13Parameter = register(new MetaParameter<>(18, "f13",metaType(java.lang.Long.class)));

                  private final MetaParameter<java.lang.Boolean> f14Parameter = register(new MetaParameter<>(19, "f14",metaType(java.lang.Boolean.class)));

                  private final MetaParameter<java.lang.Character> f15Parameter = register(new MetaParameter<>(20, "f15",metaType(java.lang.Character.class)));

                  private final MetaParameter<java.lang.String> f16Parameter = register(new MetaParameter<>(21, "f16",metaType(java.lang.String.class)));

                  private final MetaParameter<int[]> f17Parameter = register(new MetaParameter<>(22, "f17",metaArray(int[].class, int[]::new, metaType(int.class))));

                  private final MetaParameter<short[]> f18Parameter = register(new MetaParameter<>(23, "f18",metaArray(short[].class, short[]::new, metaType(short.class))));

                  private final MetaParameter<double[]> f19Parameter = register(new MetaParameter<>(24, "f19",metaArray(double[].class, double[]::new, metaType(double.class))));

                  private final MetaParameter<float[]> f20Parameter = register(new MetaParameter<>(25, "f20",metaArray(float[].class, float[]::new, metaType(float.class))));

                  private final MetaParameter<long[]> f21Parameter = register(new MetaParameter<>(26, "f21",metaArray(long[].class, long[]::new, metaType(long.class))));

                  private final MetaParameter<boolean[]> f22Parameter = register(new MetaParameter<>(27, "f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class))));

                  private final MetaParameter<char[]> f23Parameter = register(new MetaParameter<>(28, "f23",metaArray(char[].class, char[]::new, metaType(char.class))));

                  private final MetaParameter<byte[]> f24Parameter = register(new MetaParameter<>(29, "f24",metaArray(byte[].class, byte[]::new, metaType(byte.class))));

                  private final MetaParameter<java.lang.Integer[]> f25Parameter = register(new MetaParameter<>(30, "f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class))));

                  private final MetaParameter<java.lang.Short[]> f26Parameter = register(new MetaParameter<>(31, "f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class))));

                  private final MetaParameter<java.lang.Double[]> f27Parameter = register(new MetaParameter<>(32, "f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class))));

                  private final MetaParameter<java.lang.Float[]> f28Parameter = register(new MetaParameter<>(33, "f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class))));

                  private final MetaParameter<java.lang.Long[]> f29Parameter = register(new MetaParameter<>(34, "f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class))));

                  private final MetaParameter<java.lang.Boolean[]> f30Parameter = register(new MetaParameter<>(35, "f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class))));

                  private final MetaParameter<java.lang.Character[]> f31Parameter = register(new MetaParameter<>(36, "f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class))));

                  private final MetaParameter<java.lang.String[]> f32Parameter = register(new MetaParameter<>(37, "f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));

                  private final MetaParameter<java.util.List<java.lang.String>> f33Parameter = register(new MetaParameter<>(38, "f33",metaType(java.util.List.class,metaType(java.lang.String.class))));

                  private final MetaParameter<java.util.Set<java.lang.String>> f34Parameter = register(new MetaParameter<>(39, "f34",metaType(java.util.Set.class,metaType(java.lang.String.class))));

                  private final MetaParameter<java.util.Collection<java.lang.String>> f35Parameter = register(new MetaParameter<>(40, "f35",metaType(java.util.Collection.class,metaType(java.lang.String.class))));

                  private final MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f36Parameter = register(new MetaParameter<>(41, "f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class))));

                  private final MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f37Parameter = register(new MetaParameter<>(42, "f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class))));

                  private final MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter = register(new MetaParameter<>(43, "f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class))));

                  private final MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f39Parameter = register(new MetaParameter<>(44, "f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))));

                  private final MetaParameter<java.util.List<int[]>> f40Parameter = register(new MetaParameter<>(45, "f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))));

                  private final MetaParameter<java.util.List<byte[]>> f41Parameter = register(new MetaParameter<>(46, "f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                  private final MetaParameter<java.util.List<java.lang.String[]>> f42Parameter = register(new MetaParameter<>(47, "f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                  private final MetaParameter<java.util.List<byte[]>[]> f43Parameter = register(new MetaParameter<>(48, "f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))))));

                  private final MetaParameter<java.util.List<int[]>[]> f44Parameter = register(new MetaParameter<>(49, "f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))))));

                  private final MetaParameter<java.util.List<java.lang.String[]>[]> f45Parameter = register(new MetaParameter<>(50, "f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))))));

                  private final MetaParameter<java.util.List<java.util.List<java.lang.String>>> f46Parameter = register(new MetaParameter<>(51, "f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class)))));

                  private final MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f47Parameter = register(new MetaParameter<>(52, "f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class))))));

                  private final MetaParameter<java.util.Map<java.lang.Integer, java.lang.String>> f48Parameter = register(new MetaParameter<>(53, "f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class))));

                  private final MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f49Parameter = register(new MetaParameter<>(54, "f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                  private final MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Parameter = register(new MetaParameter<>(55, "f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class)))));

                  private final MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Parameter = register(new MetaParameter<>(56, "f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                  private final MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Parameter = register(new MetaParameter<>(57, "f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                  private final MetaParameter<io.art.message.pack.test.model.Model> f53Parameter = register(new MetaParameter<>(58, "f53",metaType(io.art.message.pack.test.model.Model.class)));

                  private final MetaParameter<java.time.LocalDateTime> f54Parameter = register(new MetaParameter<>(59, "f54",metaType(java.time.LocalDateTime.class)));

                  private final MetaParameter<java.time.ZonedDateTime> f55Parameter = register(new MetaParameter<>(60, "f55",metaType(java.time.ZonedDateTime.class)));

                  private final MetaParameter<java.time.Duration> f56Parameter = register(new MetaParameter<>(61, "f56",metaType(java.time.Duration.class)));

                  private final MetaParameter<io.art.message.pack.test.model.Model.ModelEnum> f57Parameter = register(new MetaParameter<>(62, "f57",metaEnum(io.art.message.pack.test.model.Model.ModelEnum.class, io.art.message.pack.test.model.Model.ModelEnum::valueOf)));

                  private final MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter = register(new MetaParameter<>(63, "f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))))));

                  private final MetaParameter<java.util.List<io.art.message.pack.test.model.Model>> f59Parameter = register(new MetaParameter<>(64, "f59",metaType(java.util.List.class,metaType(io.art.message.pack.test.model.Model.class))));

                  private final MetaParameter<java.util.Set<io.art.message.pack.test.model.Model>> f60Parameter = register(new MetaParameter<>(65, "f60",metaType(java.util.Set.class,metaType(io.art.message.pack.test.model.Model.class))));

                  private final MetaParameter<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Parameter = register(new MetaParameter<>(66, "f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.message.pack.test.model.Model.class))));

                  private MetaConstructorConstructor() {
                    super(metaType(io.art.message.pack.test.model.Model.class));
                  }

                  @Override
                  public io.art.message.pack.test.model.Model invoke(java.lang.Object[] arguments)
                      throws Throwable {
                    return new io.art.message.pack.test.model.Model((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]),(reactor.core.publisher.Flux<java.lang.String>)(arguments[1]),(java.util.stream.Stream<java.lang.String>)(arguments[2]),(reactor.core.publisher.Mono<java.lang.String[]>)(arguments[3]),(java.lang.String)(arguments[4]),(java.lang.String)(arguments[5]),(int)(arguments[6]),(short)(arguments[7]),(double)(arguments[8]),(float)(arguments[9]),(long)(arguments[10]),(boolean)(arguments[11]),(char)(arguments[12]),(byte)(arguments[13]),(java.lang.Integer)(arguments[14]),(java.lang.Short)(arguments[15]),(java.lang.Double)(arguments[16]),(java.lang.Float)(arguments[17]),(java.lang.Long)(arguments[18]),(java.lang.Boolean)(arguments[19]),(java.lang.Character)(arguments[20]),(java.lang.String)(arguments[21]),(int[])(arguments[22]),(short[])(arguments[23]),(double[])(arguments[24]),(float[])(arguments[25]),(long[])(arguments[26]),(boolean[])(arguments[27]),(char[])(arguments[28]),(byte[])(arguments[29]),(java.lang.Integer[])(arguments[30]),(java.lang.Short[])(arguments[31]),(java.lang.Double[])(arguments[32]),(java.lang.Float[])(arguments[33]),(java.lang.Long[])(arguments[34]),(java.lang.Boolean[])(arguments[35]),(java.lang.Character[])(arguments[36]),(java.lang.String[])(arguments[37]),(java.util.List<java.lang.String>)(arguments[38]),(java.util.Set<java.lang.String>)(arguments[39]),(java.util.Collection<java.lang.String>)(arguments[40]),(io.art.core.collection.ImmutableArray<java.lang.String>)(arguments[41]),(io.art.core.collection.ImmutableSet<java.lang.String>)(arguments[42]),(java.util.function.Supplier<java.lang.String>)(arguments[43]),(io.art.core.property.LazyProperty<java.lang.String>)(arguments[44]),(java.util.List<int[]>)(arguments[45]),(java.util.List<byte[]>)(arguments[46]),(java.util.List<java.lang.String[]>)(arguments[47]),(java.util.List<byte[]>[])(arguments[48]),(java.util.List<int[]>[])(arguments[49]),(java.util.List<java.lang.String[]>[])(arguments[50]),(java.util.List<java.util.List<java.lang.String>>)(arguments[51]),(java.util.List<java.util.List<java.lang.String>[]>)(arguments[52]),(java.util.Map<java.lang.Integer, java.lang.String>)(arguments[53]),(java.util.Map<java.lang.String, java.lang.String[]>)(arguments[54]),(java.util.Map<java.lang.String, java.util.List<java.lang.String>>)(arguments[55]),(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[56]),(io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[57]),(io.art.message.pack.test.model.Model)(arguments[58]),(java.time.LocalDateTime)(arguments[59]),(java.time.ZonedDateTime)(arguments[60]),(java.time.Duration)(arguments[61]),(io.art.message.pack.test.model.Model.ModelEnum)(arguments[62]),(java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>)(arguments[63]),(java.util.List<io.art.message.pack.test.model.Model>)(arguments[64]),(java.util.Set<io.art.message.pack.test.model.Model>)(arguments[65]),(java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>)(arguments[66]));
                  }

                  public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> mono1Parameter(
                      ) {
                    return mono1Parameter;
                  }

                  public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> fluxParameter(
                      ) {
                    return fluxParameter;
                  }

                  public MetaParameter<java.util.stream.Stream<java.lang.String>> streamParameter(
                      ) {
                    return streamParameter;
                  }

                  public MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> mono2Parameter(
                      ) {
                    return mono2Parameter;
                  }

                  public MetaParameter<java.lang.String> nullFieldParameter() {
                    return nullFieldParameter;
                  }

                  public MetaParameter<java.lang.String> emptyStringParameter() {
                    return emptyStringParameter;
                  }

                  public MetaParameter<java.lang.Integer> f1Parameter() {
                    return f1Parameter;
                  }

                  public MetaParameter<java.lang.Short> f2Parameter() {
                    return f2Parameter;
                  }

                  public MetaParameter<java.lang.Double> f3Parameter() {
                    return f3Parameter;
                  }

                  public MetaParameter<java.lang.Float> f4Parameter() {
                    return f4Parameter;
                  }

                  public MetaParameter<java.lang.Long> f5Parameter() {
                    return f5Parameter;
                  }

                  public MetaParameter<java.lang.Boolean> f6Parameter() {
                    return f6Parameter;
                  }

                  public MetaParameter<java.lang.Character> f7Parameter() {
                    return f7Parameter;
                  }

                  public MetaParameter<Byte> f8Parameter() {
                    return f8Parameter;
                  }

                  public MetaParameter<java.lang.Integer> f9Parameter() {
                    return f9Parameter;
                  }

                  public MetaParameter<java.lang.Short> f10Parameter() {
                    return f10Parameter;
                  }

                  public MetaParameter<java.lang.Double> f11Parameter() {
                    return f11Parameter;
                  }

                  public MetaParameter<java.lang.Float> f12Parameter() {
                    return f12Parameter;
                  }

                  public MetaParameter<java.lang.Long> f13Parameter() {
                    return f13Parameter;
                  }

                  public MetaParameter<java.lang.Boolean> f14Parameter() {
                    return f14Parameter;
                  }

                  public MetaParameter<java.lang.Character> f15Parameter() {
                    return f15Parameter;
                  }

                  public MetaParameter<java.lang.String> f16Parameter() {
                    return f16Parameter;
                  }

                  public MetaParameter<int[]> f17Parameter() {
                    return f17Parameter;
                  }

                  public MetaParameter<short[]> f18Parameter() {
                    return f18Parameter;
                  }

                  public MetaParameter<double[]> f19Parameter() {
                    return f19Parameter;
                  }

                  public MetaParameter<float[]> f20Parameter() {
                    return f20Parameter;
                  }

                  public MetaParameter<long[]> f21Parameter() {
                    return f21Parameter;
                  }

                  public MetaParameter<boolean[]> f22Parameter() {
                    return f22Parameter;
                  }

                  public MetaParameter<char[]> f23Parameter() {
                    return f23Parameter;
                  }

                  public MetaParameter<byte[]> f24Parameter() {
                    return f24Parameter;
                  }

                  public MetaParameter<java.lang.Integer[]> f25Parameter() {
                    return f25Parameter;
                  }

                  public MetaParameter<java.lang.Short[]> f26Parameter() {
                    return f26Parameter;
                  }

                  public MetaParameter<java.lang.Double[]> f27Parameter() {
                    return f27Parameter;
                  }

                  public MetaParameter<java.lang.Float[]> f28Parameter() {
                    return f28Parameter;
                  }

                  public MetaParameter<java.lang.Long[]> f29Parameter() {
                    return f29Parameter;
                  }

                  public MetaParameter<java.lang.Boolean[]> f30Parameter() {
                    return f30Parameter;
                  }

                  public MetaParameter<java.lang.Character[]> f31Parameter() {
                    return f31Parameter;
                  }

                  public MetaParameter<java.lang.String[]> f32Parameter() {
                    return f32Parameter;
                  }

                  public MetaParameter<java.util.List<java.lang.String>> f33Parameter() {
                    return f33Parameter;
                  }

                  public MetaParameter<java.util.Set<java.lang.String>> f34Parameter() {
                    return f34Parameter;
                  }

                  public MetaParameter<java.util.Collection<java.lang.String>> f35Parameter() {
                    return f35Parameter;
                  }

                  public MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f36Parameter(
                      ) {
                    return f36Parameter;
                  }

                  public MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f37Parameter(
                      ) {
                    return f37Parameter;
                  }

                  public MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter(
                      ) {
                    return f38Parameter;
                  }

                  public MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f39Parameter(
                      ) {
                    return f39Parameter;
                  }

                  public MetaParameter<java.util.List<int[]>> f40Parameter() {
                    return f40Parameter;
                  }

                  public MetaParameter<java.util.List<byte[]>> f41Parameter() {
                    return f41Parameter;
                  }

                  public MetaParameter<java.util.List<java.lang.String[]>> f42Parameter() {
                    return f42Parameter;
                  }

                  public MetaParameter<java.util.List<byte[]>[]> f43Parameter() {
                    return f43Parameter;
                  }

                  public MetaParameter<java.util.List<int[]>[]> f44Parameter() {
                    return f44Parameter;
                  }

                  public MetaParameter<java.util.List<java.lang.String[]>[]> f45Parameter() {
                    return f45Parameter;
                  }

                  public MetaParameter<java.util.List<java.util.List<java.lang.String>>> f46Parameter(
                      ) {
                    return f46Parameter;
                  }

                  public MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f47Parameter(
                      ) {
                    return f47Parameter;
                  }

                  public MetaParameter<java.util.Map<java.lang.Integer, java.lang.String>> f48Parameter(
                      ) {
                    return f48Parameter;
                  }

                  public MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f49Parameter(
                      ) {
                    return f49Parameter;
                  }

                  public MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Parameter(
                      ) {
                    return f50Parameter;
                  }

                  public MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Parameter(
                      ) {
                    return f51Parameter;
                  }

                  public MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Parameter(
                      ) {
                    return f52Parameter;
                  }

                  public MetaParameter<io.art.message.pack.test.model.Model> f53Parameter() {
                    return f53Parameter;
                  }

                  public MetaParameter<java.time.LocalDateTime> f54Parameter() {
                    return f54Parameter;
                  }

                  public MetaParameter<java.time.ZonedDateTime> f55Parameter() {
                    return f55Parameter;
                  }

                  public MetaParameter<java.time.Duration> f56Parameter() {
                    return f56Parameter;
                  }

                  public MetaParameter<io.art.message.pack.test.model.Model.ModelEnum> f57Parameter(
                      ) {
                    return f57Parameter;
                  }

                  public MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter(
                      ) {
                    return f58Parameter;
                  }

                  public MetaParameter<java.util.List<io.art.message.pack.test.model.Model>> f59Parameter(
                      ) {
                    return f59Parameter;
                  }

                  public MetaParameter<java.util.Set<io.art.message.pack.test.model.Model>> f60Parameter(
                      ) {
                    return f60Parameter;
                  }

                  public MetaParameter<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Parameter(
                      ) {
                    return f61Parameter;
                  }
                }

                public static final class MetaToBuilderMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model, io.art.message.pack.test.model.Model.ModelBuilder> {
                  private MetaToBuilderMethod() {
                    super("toBuilder",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.toBuilder();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.toBuilder();
                  }
                }

                public static final class MetaGetMono1Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, reactor.core.publisher.Mono<java.lang.String>> {
                  private MetaGetMono1Method() {
                    super("getMono1",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getMono1();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getMono1();
                  }
                }

                public static final class MetaGetFluxMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model, reactor.core.publisher.Flux<java.lang.String>> {
                  private MetaGetFluxMethod() {
                    super("getFlux",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getFlux();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getFlux();
                  }
                }

                public static final class MetaGetStreamMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.stream.Stream<java.lang.String>> {
                  private MetaGetStreamMethod() {
                    super("getStream",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getStream();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getStream();
                  }
                }

                public static final class MetaGetMono2Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, reactor.core.publisher.Mono<java.lang.String[]>> {
                  private MetaGetMono2Method() {
                    super("getMono2",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getMono2();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getMono2();
                  }
                }

                public static final class MetaGetNullFieldMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.String> {
                  private MetaGetNullFieldMethod() {
                    super("getNullField",metaType(java.lang.String.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getNullField();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getNullField();
                  }
                }

                public static final class MetaGetEmptyStringMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.String> {
                  private MetaGetEmptyStringMethod() {
                    super("getEmptyString",metaType(java.lang.String.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getEmptyString();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getEmptyString();
                  }
                }

                public static final class MetaGetF1Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Integer> {
                  private MetaGetF1Method() {
                    super("getF1",metaType(int.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF1();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF1();
                  }
                }

                public static final class MetaGetF2Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Short> {
                  private MetaGetF2Method() {
                    super("getF2",metaType(short.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF2();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF2();
                  }
                }

                public static final class MetaGetF3Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Double> {
                  private MetaGetF3Method() {
                    super("getF3",metaType(double.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF3();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF3();
                  }
                }

                public static final class MetaGetF4Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Float> {
                  private MetaGetF4Method() {
                    super("getF4",metaType(float.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF4();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF4();
                  }
                }

                public static final class MetaGetF5Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Long> {
                  private MetaGetF5Method() {
                    super("getF5",metaType(long.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF5();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF5();
                  }
                }

                public static final class MetaIsF6Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Boolean> {
                  private MetaIsF6Method() {
                    super("isF6",metaType(boolean.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.isF6();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.isF6();
                  }
                }

                public static final class MetaGetF7Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Character> {
                  private MetaGetF7Method() {
                    super("getF7",metaType(char.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF7();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF7();
                  }
                }

                public static final class MetaGetF8Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, Byte> {
                  private MetaGetF8Method() {
                    super("getF8",metaType(byte.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF8();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF8();
                  }
                }

                public static final class MetaGetF9Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Integer> {
                  private MetaGetF9Method() {
                    super("getF9",metaType(java.lang.Integer.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF9();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF9();
                  }
                }

                public static final class MetaGetF10Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Short> {
                  private MetaGetF10Method() {
                    super("getF10",metaType(java.lang.Short.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF10();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF10();
                  }
                }

                public static final class MetaGetF11Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Double> {
                  private MetaGetF11Method() {
                    super("getF11",metaType(java.lang.Double.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF11();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF11();
                  }
                }

                public static final class MetaGetF12Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Float> {
                  private MetaGetF12Method() {
                    super("getF12",metaType(java.lang.Float.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF12();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF12();
                  }
                }

                public static final class MetaGetF13Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Long> {
                  private MetaGetF13Method() {
                    super("getF13",metaType(java.lang.Long.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF13();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF13();
                  }
                }

                public static final class MetaGetF14Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Boolean> {
                  private MetaGetF14Method() {
                    super("getF14",metaType(java.lang.Boolean.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF14();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF14();
                  }
                }

                public static final class MetaGetF15Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Character> {
                  private MetaGetF15Method() {
                    super("getF15",metaType(java.lang.Character.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF15();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF15();
                  }
                }

                public static final class MetaGetF16Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.String> {
                  private MetaGetF16Method() {
                    super("getF16",metaType(java.lang.String.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF16();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF16();
                  }
                }

                public static final class MetaGetF17Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, int[]> {
                  private MetaGetF17Method() {
                    super("getF17",metaArray(int[].class, int[]::new, metaType(int.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF17();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF17();
                  }
                }

                public static final class MetaGetF18Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, short[]> {
                  private MetaGetF18Method() {
                    super("getF18",metaArray(short[].class, short[]::new, metaType(short.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF18();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF18();
                  }
                }

                public static final class MetaGetF19Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, double[]> {
                  private MetaGetF19Method() {
                    super("getF19",metaArray(double[].class, double[]::new, metaType(double.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF19();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF19();
                  }
                }

                public static final class MetaGetF20Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, float[]> {
                  private MetaGetF20Method() {
                    super("getF20",metaArray(float[].class, float[]::new, metaType(float.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF20();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF20();
                  }
                }

                public static final class MetaGetF21Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, long[]> {
                  private MetaGetF21Method() {
                    super("getF21",metaArray(long[].class, long[]::new, metaType(long.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF21();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF21();
                  }
                }

                public static final class MetaGetF22Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, boolean[]> {
                  private MetaGetF22Method() {
                    super("getF22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF22();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF22();
                  }
                }

                public static final class MetaGetF23Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, char[]> {
                  private MetaGetF23Method() {
                    super("getF23",metaArray(char[].class, char[]::new, metaType(char.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF23();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF23();
                  }
                }

                public static final class MetaGetF24Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, byte[]> {
                  private MetaGetF24Method() {
                    super("getF24",metaArray(byte[].class, byte[]::new, metaType(byte.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF24();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF24();
                  }
                }

                public static final class MetaGetF25Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Integer[]> {
                  private MetaGetF25Method() {
                    super("getF25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF25();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF25();
                  }
                }

                public static final class MetaGetF26Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Short[]> {
                  private MetaGetF26Method() {
                    super("getF26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF26();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF26();
                  }
                }

                public static final class MetaGetF27Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Double[]> {
                  private MetaGetF27Method() {
                    super("getF27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF27();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF27();
                  }
                }

                public static final class MetaGetF28Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Float[]> {
                  private MetaGetF28Method() {
                    super("getF28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF28();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF28();
                  }
                }

                public static final class MetaGetF29Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Long[]> {
                  private MetaGetF29Method() {
                    super("getF29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF29();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF29();
                  }
                }

                public static final class MetaGetF30Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Boolean[]> {
                  private MetaGetF30Method() {
                    super("getF30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF30();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF30();
                  }
                }

                public static final class MetaGetF31Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.Character[]> {
                  private MetaGetF31Method() {
                    super("getF31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF31();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF31();
                  }
                }

                public static final class MetaGetF32Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.lang.String[]> {
                  private MetaGetF32Method() {
                    super("getF32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF32();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF32();
                  }
                }

                public static final class MetaGetF33Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<java.lang.String>> {
                  private MetaGetF33Method() {
                    super("getF33",metaType(java.util.List.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF33();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF33();
                  }
                }

                public static final class MetaGetF34Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Set<java.lang.String>> {
                  private MetaGetF34Method() {
                    super("getF34",metaType(java.util.Set.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF34();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF34();
                  }
                }

                public static final class MetaGetF35Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Collection<java.lang.String>> {
                  private MetaGetF35Method() {
                    super("getF35",metaType(java.util.Collection.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF35();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF35();
                  }
                }

                public static final class MetaGetF36Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, io.art.core.collection.ImmutableArray<java.lang.String>> {
                  private MetaGetF36Method() {
                    super("getF36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF36();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF36();
                  }
                }

                public static final class MetaGetF37Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, io.art.core.collection.ImmutableSet<java.lang.String>> {
                  private MetaGetF37Method() {
                    super("getF37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF37();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF37();
                  }
                }

                public static final class MetaGetF38Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.function.Supplier<java.lang.String>> {
                  private MetaGetF38Method() {
                    super("getF38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF38();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF38();
                  }
                }

                public static final class MetaGetF39Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, io.art.core.property.LazyProperty<java.lang.String>> {
                  private MetaGetF39Method() {
                    super("getF39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF39();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF39();
                  }
                }

                public static final class MetaGetF40Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<int[]>> {
                  private MetaGetF40Method() {
                    super("getF40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF40();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF40();
                  }
                }

                public static final class MetaGetF41Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<byte[]>> {
                  private MetaGetF41Method() {
                    super("getF41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF41();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF41();
                  }
                }

                public static final class MetaGetF42Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<java.lang.String[]>> {
                  private MetaGetF42Method() {
                    super("getF42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF42();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF42();
                  }
                }

                public static final class MetaGetF43Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<byte[]>[]> {
                  private MetaGetF43Method() {
                    super("getF43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF43();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF43();
                  }
                }

                public static final class MetaGetF44Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<int[]>[]> {
                  private MetaGetF44Method() {
                    super("getF44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF44();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF44();
                  }
                }

                public static final class MetaGetF45Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<java.lang.String[]>[]> {
                  private MetaGetF45Method() {
                    super("getF45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF45();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF45();
                  }
                }

                public static final class MetaGetF46Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<java.util.List<java.lang.String>>> {
                  private MetaGetF46Method() {
                    super("getF46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF46();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF46();
                  }
                }

                public static final class MetaGetF47Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<java.util.List<java.lang.String>[]>> {
                  private MetaGetF47Method() {
                    super("getF47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF47();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF47();
                  }
                }

                public static final class MetaGetF48Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Map<java.lang.Integer, java.lang.String>> {
                  private MetaGetF48Method() {
                    super("getF48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF48();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF48();
                  }
                }

                public static final class MetaGetF49Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Map<java.lang.String, java.lang.String[]>> {
                  private MetaGetF49Method() {
                    super("getF49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF49();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF49();
                  }
                }

                public static final class MetaGetF50Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> {
                  private MetaGetF50Method() {
                    super("getF50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF50();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF50();
                  }
                }

                public static final class MetaGetF51Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> {
                  private MetaGetF51Method() {
                    super("getF51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF51();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF51();
                  }
                }

                public static final class MetaGetF52Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> {
                  private MetaGetF52Method() {
                    super("getF52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF52();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF52();
                  }
                }

                public static final class MetaGetF53Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, io.art.message.pack.test.model.Model> {
                  private MetaGetF53Method() {
                    super("getF53",metaType(io.art.message.pack.test.model.Model.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF53();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF53();
                  }
                }

                public static final class MetaGetF54Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.time.LocalDateTime> {
                  private MetaGetF54Method() {
                    super("getF54",metaType(java.time.LocalDateTime.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF54();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF54();
                  }
                }

                public static final class MetaGetF55Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.time.ZonedDateTime> {
                  private MetaGetF55Method() {
                    super("getF55",metaType(java.time.ZonedDateTime.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF55();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF55();
                  }
                }

                public static final class MetaGetF56Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.time.Duration> {
                  private MetaGetF56Method() {
                    super("getF56",metaType(java.time.Duration.class));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF56();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF56();
                  }
                }

                public static final class MetaGetF57Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, io.art.message.pack.test.model.Model.ModelEnum> {
                  private MetaGetF57Method() {
                    super("getF57",metaEnum(io.art.message.pack.test.model.Model.ModelEnum.class, io.art.message.pack.test.model.Model.ModelEnum::valueOf));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF57();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF57();
                  }
                }

                public static final class MetaGetF58Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> {
                  private MetaGetF58Method() {
                    super("getF58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF58();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF58();
                  }
                }

                public static final class MetaGetF59Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.List<io.art.message.pack.test.model.Model>> {
                  private MetaGetF59Method() {
                    super("getF59",metaType(java.util.List.class,metaType(io.art.message.pack.test.model.Model.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF59();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF59();
                  }
                }

                public static final class MetaGetF60Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Set<io.art.message.pack.test.model.Model>> {
                  private MetaGetF60Method() {
                    super("getF60",metaType(java.util.Set.class,metaType(io.art.message.pack.test.model.Model.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF60();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF60();
                  }
                }

                public static final class MetaGetF61Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model, java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> {
                  private MetaGetF61Method() {
                    super("getF61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.message.pack.test.model.Model.class)));
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance,
                      java.lang.Object[] arguments) throws Throwable {
                    return instance.getF61();
                  }

                  @Override
                  public java.lang.Object invoke(io.art.message.pack.test.model.Model instance)
                      throws Throwable {
                    return instance.getF61();
                  }
                }

                public static final class MetaModelBuilderClass extends MetaClass<io.art.message.pack.test.model.Model.ModelBuilder> {
                  private final MetaField<reactor.core.publisher.Mono<java.lang.String>> mono1Field = register(new MetaField<>("mono1",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),false));

                  private final MetaField<reactor.core.publisher.Flux<java.lang.String>> fluxField = register(new MetaField<>("flux",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),false));

                  private final MetaField<java.util.stream.Stream<java.lang.String>> streamField = register(new MetaField<>("stream",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),false));

                  private final MetaField<reactor.core.publisher.Mono<java.lang.String[]>> mono2Field = register(new MetaField<>("mono2",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false));

                  private final MetaField<java.lang.String> nullFieldField = register(new MetaField<>("nullField",metaType(java.lang.String.class),false));

                  private final MetaField<java.lang.String> emptyStringField = register(new MetaField<>("emptyString",metaType(java.lang.String.class),false));

                  private final MetaField<java.lang.Integer> f1Field = register(new MetaField<>("f1",metaType(int.class),false));

                  private final MetaField<java.lang.Short> f2Field = register(new MetaField<>("f2",metaType(short.class),false));

                  private final MetaField<java.lang.Double> f3Field = register(new MetaField<>("f3",metaType(double.class),false));

                  private final MetaField<java.lang.Float> f4Field = register(new MetaField<>("f4",metaType(float.class),false));

                  private final MetaField<java.lang.Long> f5Field = register(new MetaField<>("f5",metaType(long.class),false));

                  private final MetaField<java.lang.Boolean> f6Field = register(new MetaField<>("f6",metaType(boolean.class),false));

                  private final MetaField<java.lang.Character> f7Field = register(new MetaField<>("f7",metaType(char.class),false));

                  private final MetaField<Byte> f8Field = register(new MetaField<>("f8",metaType(byte.class),false));

                  private final MetaField<java.lang.Integer> f9Field = register(new MetaField<>("f9",metaType(java.lang.Integer.class),false));

                  private final MetaField<java.lang.Short> f10Field = register(new MetaField<>("f10",metaType(java.lang.Short.class),false));

                  private final MetaField<java.lang.Double> f11Field = register(new MetaField<>("f11",metaType(java.lang.Double.class),false));

                  private final MetaField<java.lang.Float> f12Field = register(new MetaField<>("f12",metaType(java.lang.Float.class),false));

                  private final MetaField<java.lang.Long> f13Field = register(new MetaField<>("f13",metaType(java.lang.Long.class),false));

                  private final MetaField<java.lang.Boolean> f14Field = register(new MetaField<>("f14",metaType(java.lang.Boolean.class),false));

                  private final MetaField<java.lang.Character> f15Field = register(new MetaField<>("f15",metaType(java.lang.Character.class),false));

                  private final MetaField<java.lang.String> f16Field = register(new MetaField<>("f16",metaType(java.lang.String.class),false));

                  private final MetaField<int[]> f17Field = register(new MetaField<>("f17",metaArray(int[].class, int[]::new, metaType(int.class)),false));

                  private final MetaField<short[]> f18Field = register(new MetaField<>("f18",metaArray(short[].class, short[]::new, metaType(short.class)),false));

                  private final MetaField<double[]> f19Field = register(new MetaField<>("f19",metaArray(double[].class, double[]::new, metaType(double.class)),false));

                  private final MetaField<float[]> f20Field = register(new MetaField<>("f20",metaArray(float[].class, float[]::new, metaType(float.class)),false));

                  private final MetaField<long[]> f21Field = register(new MetaField<>("f21",metaArray(long[].class, long[]::new, metaType(long.class)),false));

                  private final MetaField<boolean[]> f22Field = register(new MetaField<>("f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),false));

                  private final MetaField<char[]> f23Field = register(new MetaField<>("f23",metaArray(char[].class, char[]::new, metaType(char.class)),false));

                  private final MetaField<byte[]> f24Field = register(new MetaField<>("f24",metaArray(byte[].class, byte[]::new, metaType(byte.class)),false));

                  private final MetaField<java.lang.Integer[]> f25Field = register(new MetaField<>("f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),false));

                  private final MetaField<java.lang.Short[]> f26Field = register(new MetaField<>("f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),false));

                  private final MetaField<java.lang.Double[]> f27Field = register(new MetaField<>("f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),false));

                  private final MetaField<java.lang.Float[]> f28Field = register(new MetaField<>("f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),false));

                  private final MetaField<java.lang.Long[]> f29Field = register(new MetaField<>("f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),false));

                  private final MetaField<java.lang.Boolean[]> f30Field = register(new MetaField<>("f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),false));

                  private final MetaField<java.lang.Character[]> f31Field = register(new MetaField<>("f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),false));

                  private final MetaField<java.lang.String[]> f32Field = register(new MetaField<>("f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),false));

                  private final MetaField<java.util.List<java.lang.String>> f33Field = register(new MetaField<>("f33",metaType(java.util.List.class,metaType(java.lang.String.class)),false));

                  private final MetaField<java.util.Set<java.lang.String>> f34Field = register(new MetaField<>("f34",metaType(java.util.Set.class,metaType(java.lang.String.class)),false));

                  private final MetaField<java.util.Collection<java.lang.String>> f35Field = register(new MetaField<>("f35",metaType(java.util.Collection.class,metaType(java.lang.String.class)),false));

                  private final MetaField<io.art.core.collection.ImmutableArray<java.lang.String>> f36Field = register(new MetaField<>("f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),false));

                  private final MetaField<io.art.core.collection.ImmutableSet<java.lang.String>> f37Field = register(new MetaField<>("f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),false));

                  private final MetaField<java.util.function.Supplier<java.lang.String>> f38Field = register(new MetaField<>("f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),false));

                  private final MetaField<io.art.core.property.LazyProperty<java.lang.String>> f39Field = register(new MetaField<>("f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),false));

                  private final MetaField<java.util.List<int[]>> f40Field = register(new MetaField<>("f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),false));

                  private final MetaField<java.util.List<byte[]>> f41Field = register(new MetaField<>("f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))),false));

                  private final MetaField<java.util.List<java.lang.String[]>> f42Field = register(new MetaField<>("f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false));

                  private final MetaField<java.util.List<byte[]>[]> f43Field = register(new MetaField<>("f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))),false));

                  private final MetaField<java.util.List<int[]>[]> f44Field = register(new MetaField<>("f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),false));

                  private final MetaField<java.util.List<java.lang.String[]>[]> f45Field = register(new MetaField<>("f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),false));

                  private final MetaField<java.util.List<java.util.List<java.lang.String>>> f46Field = register(new MetaField<>("f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),false));

                  private final MetaField<java.util.List<java.util.List<java.lang.String>[]>> f47Field = register(new MetaField<>("f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),false));

                  private final MetaField<java.util.Map<java.lang.Integer, java.lang.String>> f48Field = register(new MetaField<>("f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class)),false));

                  private final MetaField<java.util.Map<java.lang.String, java.lang.String[]>> f49Field = register(new MetaField<>("f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false));

                  private final MetaField<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field = register(new MetaField<>("f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),false));

                  private final MetaField<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field = register(new MetaField<>("f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false));

                  private final MetaField<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field = register(new MetaField<>("f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false));

                  private final MetaField<io.art.message.pack.test.model.Model> f53Field = register(new MetaField<>("f53",metaType(io.art.message.pack.test.model.Model.class),false));

                  private final MetaField<java.time.LocalDateTime> f54Field = register(new MetaField<>("f54",metaType(java.time.LocalDateTime.class),false));

                  private final MetaField<java.time.ZonedDateTime> f55Field = register(new MetaField<>("f55",metaType(java.time.ZonedDateTime.class),false));

                  private final MetaField<java.time.Duration> f56Field = register(new MetaField<>("f56",metaType(java.time.Duration.class),false));

                  private final MetaField<io.art.message.pack.test.model.Model.ModelEnum> f57Field = register(new MetaField<>("f57",metaEnum(io.art.message.pack.test.model.Model.ModelEnum.class, io.art.message.pack.test.model.Model.ModelEnum::valueOf),false));

                  private final MetaField<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field = register(new MetaField<>("f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),false));

                  private final MetaField<java.util.List<io.art.message.pack.test.model.Model>> f59Field = register(new MetaField<>("f59",metaType(java.util.List.class,metaType(io.art.message.pack.test.model.Model.class)),false));

                  private final MetaField<java.util.Set<io.art.message.pack.test.model.Model>> f60Field = register(new MetaField<>("f60",metaType(java.util.Set.class,metaType(io.art.message.pack.test.model.Model.class)),false));

                  private final MetaField<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Field = register(new MetaField<>("f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.message.pack.test.model.Model.class)),false));

                  private final MetaMono1Method mono1Method = register(new MetaMono1Method());

                  private final MetaFluxMethod fluxMethod = register(new MetaFluxMethod());

                  private final MetaStreamMethod streamMethod = register(new MetaStreamMethod());

                  private final MetaMono2Method mono2Method = register(new MetaMono2Method());

                  private final MetaNullFieldMethod nullFieldMethod = register(new MetaNullFieldMethod());

                  private final MetaEmptyStringMethod emptyStringMethod = register(new MetaEmptyStringMethod());

                  private final MetaF1Method f1Method = register(new MetaF1Method());

                  private final MetaF2Method f2Method = register(new MetaF2Method());

                  private final MetaF3Method f3Method = register(new MetaF3Method());

                  private final MetaF4Method f4Method = register(new MetaF4Method());

                  private final MetaF5Method f5Method = register(new MetaF5Method());

                  private final MetaF6Method f6Method = register(new MetaF6Method());

                  private final MetaF7Method f7Method = register(new MetaF7Method());

                  private final MetaF8Method f8Method = register(new MetaF8Method());

                  private final MetaF9Method f9Method = register(new MetaF9Method());

                  private final MetaF10Method f10Method = register(new MetaF10Method());

                  private final MetaF11Method f11Method = register(new MetaF11Method());

                  private final MetaF12Method f12Method = register(new MetaF12Method());

                  private final MetaF13Method f13Method = register(new MetaF13Method());

                  private final MetaF14Method f14Method = register(new MetaF14Method());

                  private final MetaF15Method f15Method = register(new MetaF15Method());

                  private final MetaF16Method f16Method = register(new MetaF16Method());

                  private final MetaF17Method f17Method = register(new MetaF17Method());

                  private final MetaF18Method f18Method = register(new MetaF18Method());

                  private final MetaF19Method f19Method = register(new MetaF19Method());

                  private final MetaF20Method f20Method = register(new MetaF20Method());

                  private final MetaF21Method f21Method = register(new MetaF21Method());

                  private final MetaF22Method f22Method = register(new MetaF22Method());

                  private final MetaF23Method f23Method = register(new MetaF23Method());

                  private final MetaF24Method f24Method = register(new MetaF24Method());

                  private final MetaF25Method f25Method = register(new MetaF25Method());

                  private final MetaF26Method f26Method = register(new MetaF26Method());

                  private final MetaF27Method f27Method = register(new MetaF27Method());

                  private final MetaF28Method f28Method = register(new MetaF28Method());

                  private final MetaF29Method f29Method = register(new MetaF29Method());

                  private final MetaF30Method f30Method = register(new MetaF30Method());

                  private final MetaF31Method f31Method = register(new MetaF31Method());

                  private final MetaF32Method f32Method = register(new MetaF32Method());

                  private final MetaF33Method f33Method = register(new MetaF33Method());

                  private final MetaF34Method f34Method = register(new MetaF34Method());

                  private final MetaF35Method f35Method = register(new MetaF35Method());

                  private final MetaF36Method f36Method = register(new MetaF36Method());

                  private final MetaF37Method f37Method = register(new MetaF37Method());

                  private final MetaF38Method f38Method = register(new MetaF38Method());

                  private final MetaF39Method f39Method = register(new MetaF39Method());

                  private final MetaF40Method f40Method = register(new MetaF40Method());

                  private final MetaF41Method f41Method = register(new MetaF41Method());

                  private final MetaF42Method f42Method = register(new MetaF42Method());

                  private final MetaF43Method f43Method = register(new MetaF43Method());

                  private final MetaF44Method f44Method = register(new MetaF44Method());

                  private final MetaF45Method f45Method = register(new MetaF45Method());

                  private final MetaF46Method f46Method = register(new MetaF46Method());

                  private final MetaF47Method f47Method = register(new MetaF47Method());

                  private final MetaF48Method f48Method = register(new MetaF48Method());

                  private final MetaF49Method f49Method = register(new MetaF49Method());

                  private final MetaF50Method f50Method = register(new MetaF50Method());

                  private final MetaF51Method f51Method = register(new MetaF51Method());

                  private final MetaF52Method f52Method = register(new MetaF52Method());

                  private final MetaF53Method f53Method = register(new MetaF53Method());

                  private final MetaF54Method f54Method = register(new MetaF54Method());

                  private final MetaF55Method f55Method = register(new MetaF55Method());

                  private final MetaF56Method f56Method = register(new MetaF56Method());

                  private final MetaF57Method f57Method = register(new MetaF57Method());

                  private final MetaF58Method f58Method = register(new MetaF58Method());

                  private final MetaF59Method f59Method = register(new MetaF59Method());

                  private final MetaF60Method f60Method = register(new MetaF60Method());

                  private final MetaF61Method f61Method = register(new MetaF61Method());

                  private final MetaBuildMethod buildMethod = register(new MetaBuildMethod());

                  private MetaModelBuilderClass() {
                    super(metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                  }

                  public MetaField<reactor.core.publisher.Mono<java.lang.String>> mono1Field() {
                    return mono1Field;
                  }

                  public MetaField<reactor.core.publisher.Flux<java.lang.String>> fluxField() {
                    return fluxField;
                  }

                  public MetaField<java.util.stream.Stream<java.lang.String>> streamField() {
                    return streamField;
                  }

                  public MetaField<reactor.core.publisher.Mono<java.lang.String[]>> mono2Field() {
                    return mono2Field;
                  }

                  public MetaField<java.lang.String> nullFieldField() {
                    return nullFieldField;
                  }

                  public MetaField<java.lang.String> emptyStringField() {
                    return emptyStringField;
                  }

                  public MetaField<java.lang.Integer> f1Field() {
                    return f1Field;
                  }

                  public MetaField<java.lang.Short> f2Field() {
                    return f2Field;
                  }

                  public MetaField<java.lang.Double> f3Field() {
                    return f3Field;
                  }

                  public MetaField<java.lang.Float> f4Field() {
                    return f4Field;
                  }

                  public MetaField<java.lang.Long> f5Field() {
                    return f5Field;
                  }

                  public MetaField<java.lang.Boolean> f6Field() {
                    return f6Field;
                  }

                  public MetaField<java.lang.Character> f7Field() {
                    return f7Field;
                  }

                  public MetaField<Byte> f8Field() {
                    return f8Field;
                  }

                  public MetaField<java.lang.Integer> f9Field() {
                    return f9Field;
                  }

                  public MetaField<java.lang.Short> f10Field() {
                    return f10Field;
                  }

                  public MetaField<java.lang.Double> f11Field() {
                    return f11Field;
                  }

                  public MetaField<java.lang.Float> f12Field() {
                    return f12Field;
                  }

                  public MetaField<java.lang.Long> f13Field() {
                    return f13Field;
                  }

                  public MetaField<java.lang.Boolean> f14Field() {
                    return f14Field;
                  }

                  public MetaField<java.lang.Character> f15Field() {
                    return f15Field;
                  }

                  public MetaField<java.lang.String> f16Field() {
                    return f16Field;
                  }

                  public MetaField<int[]> f17Field() {
                    return f17Field;
                  }

                  public MetaField<short[]> f18Field() {
                    return f18Field;
                  }

                  public MetaField<double[]> f19Field() {
                    return f19Field;
                  }

                  public MetaField<float[]> f20Field() {
                    return f20Field;
                  }

                  public MetaField<long[]> f21Field() {
                    return f21Field;
                  }

                  public MetaField<boolean[]> f22Field() {
                    return f22Field;
                  }

                  public MetaField<char[]> f23Field() {
                    return f23Field;
                  }

                  public MetaField<byte[]> f24Field() {
                    return f24Field;
                  }

                  public MetaField<java.lang.Integer[]> f25Field() {
                    return f25Field;
                  }

                  public MetaField<java.lang.Short[]> f26Field() {
                    return f26Field;
                  }

                  public MetaField<java.lang.Double[]> f27Field() {
                    return f27Field;
                  }

                  public MetaField<java.lang.Float[]> f28Field() {
                    return f28Field;
                  }

                  public MetaField<java.lang.Long[]> f29Field() {
                    return f29Field;
                  }

                  public MetaField<java.lang.Boolean[]> f30Field() {
                    return f30Field;
                  }

                  public MetaField<java.lang.Character[]> f31Field() {
                    return f31Field;
                  }

                  public MetaField<java.lang.String[]> f32Field() {
                    return f32Field;
                  }

                  public MetaField<java.util.List<java.lang.String>> f33Field() {
                    return f33Field;
                  }

                  public MetaField<java.util.Set<java.lang.String>> f34Field() {
                    return f34Field;
                  }

                  public MetaField<java.util.Collection<java.lang.String>> f35Field() {
                    return f35Field;
                  }

                  public MetaField<io.art.core.collection.ImmutableArray<java.lang.String>> f36Field(
                      ) {
                    return f36Field;
                  }

                  public MetaField<io.art.core.collection.ImmutableSet<java.lang.String>> f37Field(
                      ) {
                    return f37Field;
                  }

                  public MetaField<java.util.function.Supplier<java.lang.String>> f38Field() {
                    return f38Field;
                  }

                  public MetaField<io.art.core.property.LazyProperty<java.lang.String>> f39Field() {
                    return f39Field;
                  }

                  public MetaField<java.util.List<int[]>> f40Field() {
                    return f40Field;
                  }

                  public MetaField<java.util.List<byte[]>> f41Field() {
                    return f41Field;
                  }

                  public MetaField<java.util.List<java.lang.String[]>> f42Field() {
                    return f42Field;
                  }

                  public MetaField<java.util.List<byte[]>[]> f43Field() {
                    return f43Field;
                  }

                  public MetaField<java.util.List<int[]>[]> f44Field() {
                    return f44Field;
                  }

                  public MetaField<java.util.List<java.lang.String[]>[]> f45Field() {
                    return f45Field;
                  }

                  public MetaField<java.util.List<java.util.List<java.lang.String>>> f46Field() {
                    return f46Field;
                  }

                  public MetaField<java.util.List<java.util.List<java.lang.String>[]>> f47Field() {
                    return f47Field;
                  }

                  public MetaField<java.util.Map<java.lang.Integer, java.lang.String>> f48Field() {
                    return f48Field;
                  }

                  public MetaField<java.util.Map<java.lang.String, java.lang.String[]>> f49Field() {
                    return f49Field;
                  }

                  public MetaField<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field(
                      ) {
                    return f50Field;
                  }

                  public MetaField<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field(
                      ) {
                    return f51Field;
                  }

                  public MetaField<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field(
                      ) {
                    return f52Field;
                  }

                  public MetaField<io.art.message.pack.test.model.Model> f53Field() {
                    return f53Field;
                  }

                  public MetaField<java.time.LocalDateTime> f54Field() {
                    return f54Field;
                  }

                  public MetaField<java.time.ZonedDateTime> f55Field() {
                    return f55Field;
                  }

                  public MetaField<java.time.Duration> f56Field() {
                    return f56Field;
                  }

                  public MetaField<io.art.message.pack.test.model.Model.ModelEnum> f57Field() {
                    return f57Field;
                  }

                  public MetaField<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field(
                      ) {
                    return f58Field;
                  }

                  public MetaField<java.util.List<io.art.message.pack.test.model.Model>> f59Field(
                      ) {
                    return f59Field;
                  }

                  public MetaField<java.util.Set<io.art.message.pack.test.model.Model>> f60Field() {
                    return f60Field;
                  }

                  public MetaField<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Field(
                      ) {
                    return f61Field;
                  }

                  public MetaMono1Method mono1Method() {
                    return mono1Method;
                  }

                  public MetaFluxMethod fluxMethod() {
                    return fluxMethod;
                  }

                  public MetaStreamMethod streamMethod() {
                    return streamMethod;
                  }

                  public MetaMono2Method mono2Method() {
                    return mono2Method;
                  }

                  public MetaNullFieldMethod nullFieldMethod() {
                    return nullFieldMethod;
                  }

                  public MetaEmptyStringMethod emptyStringMethod() {
                    return emptyStringMethod;
                  }

                  public MetaF1Method f1Method() {
                    return f1Method;
                  }

                  public MetaF2Method f2Method() {
                    return f2Method;
                  }

                  public MetaF3Method f3Method() {
                    return f3Method;
                  }

                  public MetaF4Method f4Method() {
                    return f4Method;
                  }

                  public MetaF5Method f5Method() {
                    return f5Method;
                  }

                  public MetaF6Method f6Method() {
                    return f6Method;
                  }

                  public MetaF7Method f7Method() {
                    return f7Method;
                  }

                  public MetaF8Method f8Method() {
                    return f8Method;
                  }

                  public MetaF9Method f9Method() {
                    return f9Method;
                  }

                  public MetaF10Method f10Method() {
                    return f10Method;
                  }

                  public MetaF11Method f11Method() {
                    return f11Method;
                  }

                  public MetaF12Method f12Method() {
                    return f12Method;
                  }

                  public MetaF13Method f13Method() {
                    return f13Method;
                  }

                  public MetaF14Method f14Method() {
                    return f14Method;
                  }

                  public MetaF15Method f15Method() {
                    return f15Method;
                  }

                  public MetaF16Method f16Method() {
                    return f16Method;
                  }

                  public MetaF17Method f17Method() {
                    return f17Method;
                  }

                  public MetaF18Method f18Method() {
                    return f18Method;
                  }

                  public MetaF19Method f19Method() {
                    return f19Method;
                  }

                  public MetaF20Method f20Method() {
                    return f20Method;
                  }

                  public MetaF21Method f21Method() {
                    return f21Method;
                  }

                  public MetaF22Method f22Method() {
                    return f22Method;
                  }

                  public MetaF23Method f23Method() {
                    return f23Method;
                  }

                  public MetaF24Method f24Method() {
                    return f24Method;
                  }

                  public MetaF25Method f25Method() {
                    return f25Method;
                  }

                  public MetaF26Method f26Method() {
                    return f26Method;
                  }

                  public MetaF27Method f27Method() {
                    return f27Method;
                  }

                  public MetaF28Method f28Method() {
                    return f28Method;
                  }

                  public MetaF29Method f29Method() {
                    return f29Method;
                  }

                  public MetaF30Method f30Method() {
                    return f30Method;
                  }

                  public MetaF31Method f31Method() {
                    return f31Method;
                  }

                  public MetaF32Method f32Method() {
                    return f32Method;
                  }

                  public MetaF33Method f33Method() {
                    return f33Method;
                  }

                  public MetaF34Method f34Method() {
                    return f34Method;
                  }

                  public MetaF35Method f35Method() {
                    return f35Method;
                  }

                  public MetaF36Method f36Method() {
                    return f36Method;
                  }

                  public MetaF37Method f37Method() {
                    return f37Method;
                  }

                  public MetaF38Method f38Method() {
                    return f38Method;
                  }

                  public MetaF39Method f39Method() {
                    return f39Method;
                  }

                  public MetaF40Method f40Method() {
                    return f40Method;
                  }

                  public MetaF41Method f41Method() {
                    return f41Method;
                  }

                  public MetaF42Method f42Method() {
                    return f42Method;
                  }

                  public MetaF43Method f43Method() {
                    return f43Method;
                  }

                  public MetaF44Method f44Method() {
                    return f44Method;
                  }

                  public MetaF45Method f45Method() {
                    return f45Method;
                  }

                  public MetaF46Method f46Method() {
                    return f46Method;
                  }

                  public MetaF47Method f47Method() {
                    return f47Method;
                  }

                  public MetaF48Method f48Method() {
                    return f48Method;
                  }

                  public MetaF49Method f49Method() {
                    return f49Method;
                  }

                  public MetaF50Method f50Method() {
                    return f50Method;
                  }

                  public MetaF51Method f51Method() {
                    return f51Method;
                  }

                  public MetaF52Method f52Method() {
                    return f52Method;
                  }

                  public MetaF53Method f53Method() {
                    return f53Method;
                  }

                  public MetaF54Method f54Method() {
                    return f54Method;
                  }

                  public MetaF55Method f55Method() {
                    return f55Method;
                  }

                  public MetaF56Method f56Method() {
                    return f56Method;
                  }

                  public MetaF57Method f57Method() {
                    return f57Method;
                  }

                  public MetaF58Method f58Method() {
                    return f58Method;
                  }

                  public MetaF59Method f59Method() {
                    return f59Method;
                  }

                  public MetaF60Method f60Method() {
                    return f60Method;
                  }

                  public MetaF61Method f61Method() {
                    return f61Method;
                  }

                  public MetaBuildMethod buildMethod() {
                    return buildMethod;
                  }

                  public static final class MetaMono1Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> mono1Parameter = register(new MetaParameter<>(0, "mono1",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                    private MetaMono1Method() {
                      super("mono1",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.mono1((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.mono1((reactor.core.publisher.Mono)(argument));
                    }

                    public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> mono1Parameter(
                        ) {
                      return mono1Parameter;
                    }
                  }

                  public static final class MetaFluxMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> fluxParameter = register(new MetaParameter<>(0, "flux",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                    private MetaFluxMethod() {
                      super("flux",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.flux((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.flux((reactor.core.publisher.Flux)(argument));
                    }

                    public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> fluxParameter(
                        ) {
                      return fluxParameter;
                    }
                  }

                  public static final class MetaStreamMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.stream.Stream<java.lang.String>> streamParameter = register(new MetaParameter<>(0, "stream",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class))));

                    private MetaStreamMethod() {
                      super("stream",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.stream((java.util.stream.Stream<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.stream((java.util.stream.Stream)(argument));
                    }

                    public MetaParameter<java.util.stream.Stream<java.lang.String>> streamParameter(
                        ) {
                      return streamParameter;
                    }
                  }

                  public static final class MetaMono2Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> mono2Parameter = register(new MetaParameter<>(0, "mono2",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                    private MetaMono2Method() {
                      super("mono2",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.mono2((reactor.core.publisher.Mono<java.lang.String[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.mono2((reactor.core.publisher.Mono)(argument));
                    }

                    public MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> mono2Parameter(
                        ) {
                      return mono2Parameter;
                    }
                  }

                  public static final class MetaNullFieldMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.String> nullFieldParameter = register(new MetaParameter<>(0, "nullField",metaType(java.lang.String.class)));

                    private MetaNullFieldMethod() {
                      super("nullField",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.nullField((java.lang.String)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.nullField((java.lang.String)(argument));
                    }

                    public MetaParameter<java.lang.String> nullFieldParameter() {
                      return nullFieldParameter;
                    }
                  }

                  public static final class MetaEmptyStringMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.String> emptyStringParameter = register(new MetaParameter<>(0, "emptyString",metaType(java.lang.String.class)));

                    private MetaEmptyStringMethod() {
                      super("emptyString",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.emptyString((java.lang.String)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.emptyString((java.lang.String)(argument));
                    }

                    public MetaParameter<java.lang.String> emptyStringParameter() {
                      return emptyStringParameter;
                    }
                  }

                  public static final class MetaF1Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Integer> f1Parameter = register(new MetaParameter<>(0, "f1",metaType(int.class)));

                    private MetaF1Method() {
                      super("f1",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f1((int)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f1((int)(argument));
                    }

                    public MetaParameter<java.lang.Integer> f1Parameter() {
                      return f1Parameter;
                    }
                  }

                  public static final class MetaF2Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Short> f2Parameter = register(new MetaParameter<>(0, "f2",metaType(short.class)));

                    private MetaF2Method() {
                      super("f2",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f2((short)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f2((short)(argument));
                    }

                    public MetaParameter<java.lang.Short> f2Parameter() {
                      return f2Parameter;
                    }
                  }

                  public static final class MetaF3Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Double> f3Parameter = register(new MetaParameter<>(0, "f3",metaType(double.class)));

                    private MetaF3Method() {
                      super("f3",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f3((double)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f3((double)(argument));
                    }

                    public MetaParameter<java.lang.Double> f3Parameter() {
                      return f3Parameter;
                    }
                  }

                  public static final class MetaF4Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Float> f4Parameter = register(new MetaParameter<>(0, "f4",metaType(float.class)));

                    private MetaF4Method() {
                      super("f4",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f4((float)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f4((float)(argument));
                    }

                    public MetaParameter<java.lang.Float> f4Parameter() {
                      return f4Parameter;
                    }
                  }

                  public static final class MetaF5Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Long> f5Parameter = register(new MetaParameter<>(0, "f5",metaType(long.class)));

                    private MetaF5Method() {
                      super("f5",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f5((long)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f5((long)(argument));
                    }

                    public MetaParameter<java.lang.Long> f5Parameter() {
                      return f5Parameter;
                    }
                  }

                  public static final class MetaF6Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Boolean> f6Parameter = register(new MetaParameter<>(0, "f6",metaType(boolean.class)));

                    private MetaF6Method() {
                      super("f6",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f6((boolean)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f6((boolean)(argument));
                    }

                    public MetaParameter<java.lang.Boolean> f6Parameter() {
                      return f6Parameter;
                    }
                  }

                  public static final class MetaF7Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Character> f7Parameter = register(new MetaParameter<>(0, "f7",metaType(char.class)));

                    private MetaF7Method() {
                      super("f7",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f7((char)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f7((char)(argument));
                    }

                    public MetaParameter<java.lang.Character> f7Parameter() {
                      return f7Parameter;
                    }
                  }

                  public static final class MetaF8Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<Byte> f8Parameter = register(new MetaParameter<>(0, "f8",metaType(byte.class)));

                    private MetaF8Method() {
                      super("f8",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f8((byte)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f8((byte)(argument));
                    }

                    public MetaParameter<Byte> f8Parameter() {
                      return f8Parameter;
                    }
                  }

                  public static final class MetaF9Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Integer> f9Parameter = register(new MetaParameter<>(0, "f9",metaType(java.lang.Integer.class)));

                    private MetaF9Method() {
                      super("f9",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f9((java.lang.Integer)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f9((java.lang.Integer)(argument));
                    }

                    public MetaParameter<java.lang.Integer> f9Parameter() {
                      return f9Parameter;
                    }
                  }

                  public static final class MetaF10Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Short> f10Parameter = register(new MetaParameter<>(0, "f10",metaType(java.lang.Short.class)));

                    private MetaF10Method() {
                      super("f10",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f10((java.lang.Short)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f10((java.lang.Short)(argument));
                    }

                    public MetaParameter<java.lang.Short> f10Parameter() {
                      return f10Parameter;
                    }
                  }

                  public static final class MetaF11Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Double> f11Parameter = register(new MetaParameter<>(0, "f11",metaType(java.lang.Double.class)));

                    private MetaF11Method() {
                      super("f11",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f11((java.lang.Double)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f11((java.lang.Double)(argument));
                    }

                    public MetaParameter<java.lang.Double> f11Parameter() {
                      return f11Parameter;
                    }
                  }

                  public static final class MetaF12Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Float> f12Parameter = register(new MetaParameter<>(0, "f12",metaType(java.lang.Float.class)));

                    private MetaF12Method() {
                      super("f12",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f12((java.lang.Float)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f12((java.lang.Float)(argument));
                    }

                    public MetaParameter<java.lang.Float> f12Parameter() {
                      return f12Parameter;
                    }
                  }

                  public static final class MetaF13Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Long> f13Parameter = register(new MetaParameter<>(0, "f13",metaType(java.lang.Long.class)));

                    private MetaF13Method() {
                      super("f13",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f13((java.lang.Long)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f13((java.lang.Long)(argument));
                    }

                    public MetaParameter<java.lang.Long> f13Parameter() {
                      return f13Parameter;
                    }
                  }

                  public static final class MetaF14Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Boolean> f14Parameter = register(new MetaParameter<>(0, "f14",metaType(java.lang.Boolean.class)));

                    private MetaF14Method() {
                      super("f14",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f14((java.lang.Boolean)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f14((java.lang.Boolean)(argument));
                    }

                    public MetaParameter<java.lang.Boolean> f14Parameter() {
                      return f14Parameter;
                    }
                  }

                  public static final class MetaF15Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Character> f15Parameter = register(new MetaParameter<>(0, "f15",metaType(java.lang.Character.class)));

                    private MetaF15Method() {
                      super("f15",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f15((java.lang.Character)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f15((java.lang.Character)(argument));
                    }

                    public MetaParameter<java.lang.Character> f15Parameter() {
                      return f15Parameter;
                    }
                  }

                  public static final class MetaF16Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.String> f16Parameter = register(new MetaParameter<>(0, "f16",metaType(java.lang.String.class)));

                    private MetaF16Method() {
                      super("f16",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f16((java.lang.String)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f16((java.lang.String)(argument));
                    }

                    public MetaParameter<java.lang.String> f16Parameter() {
                      return f16Parameter;
                    }
                  }

                  public static final class MetaF17Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<int[]> f17Parameter = register(new MetaParameter<>(0, "f17",metaArray(int[].class, int[]::new, metaType(int.class))));

                    private MetaF17Method() {
                      super("f17",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f17((int[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f17((int[])(argument));
                    }

                    public MetaParameter<int[]> f17Parameter() {
                      return f17Parameter;
                    }
                  }

                  public static final class MetaF18Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<short[]> f18Parameter = register(new MetaParameter<>(0, "f18",metaArray(short[].class, short[]::new, metaType(short.class))));

                    private MetaF18Method() {
                      super("f18",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f18((short[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f18((short[])(argument));
                    }

                    public MetaParameter<short[]> f18Parameter() {
                      return f18Parameter;
                    }
                  }

                  public static final class MetaF19Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<double[]> f19Parameter = register(new MetaParameter<>(0, "f19",metaArray(double[].class, double[]::new, metaType(double.class))));

                    private MetaF19Method() {
                      super("f19",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f19((double[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f19((double[])(argument));
                    }

                    public MetaParameter<double[]> f19Parameter() {
                      return f19Parameter;
                    }
                  }

                  public static final class MetaF20Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<float[]> f20Parameter = register(new MetaParameter<>(0, "f20",metaArray(float[].class, float[]::new, metaType(float.class))));

                    private MetaF20Method() {
                      super("f20",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f20((float[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f20((float[])(argument));
                    }

                    public MetaParameter<float[]> f20Parameter() {
                      return f20Parameter;
                    }
                  }

                  public static final class MetaF21Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<long[]> f21Parameter = register(new MetaParameter<>(0, "f21",metaArray(long[].class, long[]::new, metaType(long.class))));

                    private MetaF21Method() {
                      super("f21",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f21((long[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f21((long[])(argument));
                    }

                    public MetaParameter<long[]> f21Parameter() {
                      return f21Parameter;
                    }
                  }

                  public static final class MetaF22Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<boolean[]> f22Parameter = register(new MetaParameter<>(0, "f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class))));

                    private MetaF22Method() {
                      super("f22",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f22((boolean[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f22((boolean[])(argument));
                    }

                    public MetaParameter<boolean[]> f22Parameter() {
                      return f22Parameter;
                    }
                  }

                  public static final class MetaF23Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<char[]> f23Parameter = register(new MetaParameter<>(0, "f23",metaArray(char[].class, char[]::new, metaType(char.class))));

                    private MetaF23Method() {
                      super("f23",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f23((char[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f23((char[])(argument));
                    }

                    public MetaParameter<char[]> f23Parameter() {
                      return f23Parameter;
                    }
                  }

                  public static final class MetaF24Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<byte[]> f24Parameter = register(new MetaParameter<>(0, "f24",metaArray(byte[].class, byte[]::new, metaType(byte.class))));

                    private MetaF24Method() {
                      super("f24",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f24((byte[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f24((byte[])(argument));
                    }

                    public MetaParameter<byte[]> f24Parameter() {
                      return f24Parameter;
                    }
                  }

                  public static final class MetaF25Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Integer[]> f25Parameter = register(new MetaParameter<>(0, "f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class))));

                    private MetaF25Method() {
                      super("f25",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f25((java.lang.Integer[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f25((java.lang.Integer[])(argument));
                    }

                    public MetaParameter<java.lang.Integer[]> f25Parameter() {
                      return f25Parameter;
                    }
                  }

                  public static final class MetaF26Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Short[]> f26Parameter = register(new MetaParameter<>(0, "f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class))));

                    private MetaF26Method() {
                      super("f26",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f26((java.lang.Short[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f26((java.lang.Short[])(argument));
                    }

                    public MetaParameter<java.lang.Short[]> f26Parameter() {
                      return f26Parameter;
                    }
                  }

                  public static final class MetaF27Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Double[]> f27Parameter = register(new MetaParameter<>(0, "f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class))));

                    private MetaF27Method() {
                      super("f27",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f27((java.lang.Double[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f27((java.lang.Double[])(argument));
                    }

                    public MetaParameter<java.lang.Double[]> f27Parameter() {
                      return f27Parameter;
                    }
                  }

                  public static final class MetaF28Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Float[]> f28Parameter = register(new MetaParameter<>(0, "f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class))));

                    private MetaF28Method() {
                      super("f28",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f28((java.lang.Float[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f28((java.lang.Float[])(argument));
                    }

                    public MetaParameter<java.lang.Float[]> f28Parameter() {
                      return f28Parameter;
                    }
                  }

                  public static final class MetaF29Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Long[]> f29Parameter = register(new MetaParameter<>(0, "f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class))));

                    private MetaF29Method() {
                      super("f29",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f29((java.lang.Long[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f29((java.lang.Long[])(argument));
                    }

                    public MetaParameter<java.lang.Long[]> f29Parameter() {
                      return f29Parameter;
                    }
                  }

                  public static final class MetaF30Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Boolean[]> f30Parameter = register(new MetaParameter<>(0, "f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class))));

                    private MetaF30Method() {
                      super("f30",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f30((java.lang.Boolean[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f30((java.lang.Boolean[])(argument));
                    }

                    public MetaParameter<java.lang.Boolean[]> f30Parameter() {
                      return f30Parameter;
                    }
                  }

                  public static final class MetaF31Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.Character[]> f31Parameter = register(new MetaParameter<>(0, "f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class))));

                    private MetaF31Method() {
                      super("f31",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f31((java.lang.Character[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f31((java.lang.Character[])(argument));
                    }

                    public MetaParameter<java.lang.Character[]> f31Parameter() {
                      return f31Parameter;
                    }
                  }

                  public static final class MetaF32Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.lang.String[]> f32Parameter = register(new MetaParameter<>(0, "f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));

                    private MetaF32Method() {
                      super("f32",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f32((java.lang.String[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f32((java.lang.String[])(argument));
                    }

                    public MetaParameter<java.lang.String[]> f32Parameter() {
                      return f32Parameter;
                    }
                  }

                  public static final class MetaF33Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<java.lang.String>> f33Parameter = register(new MetaParameter<>(0, "f33",metaType(java.util.List.class,metaType(java.lang.String.class))));

                    private MetaF33Method() {
                      super("f33",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f33((java.util.List<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f33((java.util.List)(argument));
                    }

                    public MetaParameter<java.util.List<java.lang.String>> f33Parameter() {
                      return f33Parameter;
                    }
                  }

                  public static final class MetaF34Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Set<java.lang.String>> f34Parameter = register(new MetaParameter<>(0, "f34",metaType(java.util.Set.class,metaType(java.lang.String.class))));

                    private MetaF34Method() {
                      super("f34",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f34((java.util.Set<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f34((java.util.Set)(argument));
                    }

                    public MetaParameter<java.util.Set<java.lang.String>> f34Parameter() {
                      return f34Parameter;
                    }
                  }

                  public static final class MetaF35Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Collection<java.lang.String>> f35Parameter = register(new MetaParameter<>(0, "f35",metaType(java.util.Collection.class,metaType(java.lang.String.class))));

                    private MetaF35Method() {
                      super("f35",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f35((java.util.Collection<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f35((java.util.Collection)(argument));
                    }

                    public MetaParameter<java.util.Collection<java.lang.String>> f35Parameter() {
                      return f35Parameter;
                    }
                  }

                  public static final class MetaF36Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f36Parameter = register(new MetaParameter<>(0, "f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class))));

                    private MetaF36Method() {
                      super("f36",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f36((io.art.core.collection.ImmutableArray<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f36((io.art.core.collection.ImmutableArray)(argument));
                    }

                    public MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f36Parameter(
                        ) {
                      return f36Parameter;
                    }
                  }

                  public static final class MetaF37Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f37Parameter = register(new MetaParameter<>(0, "f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class))));

                    private MetaF37Method() {
                      super("f37",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f37((io.art.core.collection.ImmutableSet<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f37((io.art.core.collection.ImmutableSet)(argument));
                    }

                    public MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f37Parameter(
                        ) {
                      return f37Parameter;
                    }
                  }

                  public static final class MetaF38Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter = register(new MetaParameter<>(0, "f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class))));

                    private MetaF38Method() {
                      super("f38",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f38((java.util.function.Supplier<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f38((java.util.function.Supplier)(argument));
                    }

                    public MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter(
                        ) {
                      return f38Parameter;
                    }
                  }

                  public static final class MetaF39Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f39Parameter = register(new MetaParameter<>(0, "f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))));

                    private MetaF39Method() {
                      super("f39",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f39((io.art.core.property.LazyProperty<java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f39((io.art.core.property.LazyProperty)(argument));
                    }

                    public MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f39Parameter(
                        ) {
                      return f39Parameter;
                    }
                  }

                  public static final class MetaF40Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<int[]>> f40Parameter = register(new MetaParameter<>(0, "f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))));

                    private MetaF40Method() {
                      super("f40",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f40((java.util.List<int[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f40((java.util.List)(argument));
                    }

                    public MetaParameter<java.util.List<int[]>> f40Parameter() {
                      return f40Parameter;
                    }
                  }

                  public static final class MetaF41Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<byte[]>> f41Parameter = register(new MetaParameter<>(0, "f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                    private MetaF41Method() {
                      super("f41",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f41((java.util.List<byte[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f41((java.util.List)(argument));
                    }

                    public MetaParameter<java.util.List<byte[]>> f41Parameter() {
                      return f41Parameter;
                    }
                  }

                  public static final class MetaF42Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<java.lang.String[]>> f42Parameter = register(new MetaParameter<>(0, "f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                    private MetaF42Method() {
                      super("f42",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f42((java.util.List<java.lang.String[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f42((java.util.List)(argument));
                    }

                    public MetaParameter<java.util.List<java.lang.String[]>> f42Parameter() {
                      return f42Parameter;
                    }
                  }

                  public static final class MetaF43Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<byte[]>[]> f43Parameter = register(new MetaParameter<>(0, "f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))))));

                    private MetaF43Method() {
                      super("f43",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f43((java.util.List<byte[]>[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f43((java.util.List[])(argument));
                    }

                    public MetaParameter<java.util.List<byte[]>[]> f43Parameter() {
                      return f43Parameter;
                    }
                  }

                  public static final class MetaF44Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<int[]>[]> f44Parameter = register(new MetaParameter<>(0, "f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))))));

                    private MetaF44Method() {
                      super("f44",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f44((java.util.List<int[]>[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f44((java.util.List[])(argument));
                    }

                    public MetaParameter<java.util.List<int[]>[]> f44Parameter() {
                      return f44Parameter;
                    }
                  }

                  public static final class MetaF45Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<java.lang.String[]>[]> f45Parameter = register(new MetaParameter<>(0, "f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))))));

                    private MetaF45Method() {
                      super("f45",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f45((java.util.List<java.lang.String[]>[])(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f45((java.util.List[])(argument));
                    }

                    public MetaParameter<java.util.List<java.lang.String[]>[]> f45Parameter() {
                      return f45Parameter;
                    }
                  }

                  public static final class MetaF46Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<java.util.List<java.lang.String>>> f46Parameter = register(new MetaParameter<>(0, "f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class)))));

                    private MetaF46Method() {
                      super("f46",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f46((java.util.List<java.util.List<java.lang.String>>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f46((java.util.List)(argument));
                    }

                    public MetaParameter<java.util.List<java.util.List<java.lang.String>>> f46Parameter(
                        ) {
                      return f46Parameter;
                    }
                  }

                  public static final class MetaF47Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f47Parameter = register(new MetaParameter<>(0, "f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class))))));

                    private MetaF47Method() {
                      super("f47",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f47((java.util.List<java.util.List<java.lang.String>[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f47((java.util.List)(argument));
                    }

                    public MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f47Parameter(
                        ) {
                      return f47Parameter;
                    }
                  }

                  public static final class MetaF48Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Map<java.lang.Integer, java.lang.String>> f48Parameter = register(new MetaParameter<>(0, "f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class))));

                    private MetaF48Method() {
                      super("f48",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f48((java.util.Map<java.lang.Integer, java.lang.String>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f48((java.util.Map)(argument));
                    }

                    public MetaParameter<java.util.Map<java.lang.Integer, java.lang.String>> f48Parameter(
                        ) {
                      return f48Parameter;
                    }
                  }

                  public static final class MetaF49Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f49Parameter = register(new MetaParameter<>(0, "f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                    private MetaF49Method() {
                      super("f49",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f49((java.util.Map<java.lang.String, java.lang.String[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f49((java.util.Map)(argument));
                    }

                    public MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f49Parameter(
                        ) {
                      return f49Parameter;
                    }
                  }

                  public static final class MetaF50Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Parameter = register(new MetaParameter<>(0, "f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class)))));

                    private MetaF50Method() {
                      super("f50",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f50((java.util.Map<java.lang.String, java.util.List<java.lang.String>>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f50((java.util.Map)(argument));
                    }

                    public MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Parameter(
                        ) {
                      return f50Parameter;
                    }
                  }

                  public static final class MetaF51Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Parameter = register(new MetaParameter<>(0, "f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                    private MetaF51Method() {
                      super("f51",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f51((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f51((java.util.Map)(argument));
                    }

                    public MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Parameter(
                        ) {
                      return f51Parameter;
                    }
                  }

                  public static final class MetaF52Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Parameter = register(new MetaParameter<>(0, "f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                    private MetaF52Method() {
                      super("f52",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f52((io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f52((io.art.core.collection.ImmutableMap)(argument));
                    }

                    public MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Parameter(
                        ) {
                      return f52Parameter;
                    }
                  }

                  public static final class MetaF53Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<io.art.message.pack.test.model.Model> f53Parameter = register(new MetaParameter<>(0, "f53",metaType(io.art.message.pack.test.model.Model.class)));

                    private MetaF53Method() {
                      super("f53",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f53((io.art.message.pack.test.model.Model)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f53((io.art.message.pack.test.model.Model)(argument));
                    }

                    public MetaParameter<io.art.message.pack.test.model.Model> f53Parameter() {
                      return f53Parameter;
                    }
                  }

                  public static final class MetaF54Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.time.LocalDateTime> f54Parameter = register(new MetaParameter<>(0, "f54",metaType(java.time.LocalDateTime.class)));

                    private MetaF54Method() {
                      super("f54",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f54((java.time.LocalDateTime)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f54((java.time.LocalDateTime)(argument));
                    }

                    public MetaParameter<java.time.LocalDateTime> f54Parameter() {
                      return f54Parameter;
                    }
                  }

                  public static final class MetaF55Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.time.ZonedDateTime> f55Parameter = register(new MetaParameter<>(0, "f55",metaType(java.time.ZonedDateTime.class)));

                    private MetaF55Method() {
                      super("f55",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f55((java.time.ZonedDateTime)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f55((java.time.ZonedDateTime)(argument));
                    }

                    public MetaParameter<java.time.ZonedDateTime> f55Parameter() {
                      return f55Parameter;
                    }
                  }

                  public static final class MetaF56Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.time.Duration> f56Parameter = register(new MetaParameter<>(0, "f56",metaType(java.time.Duration.class)));

                    private MetaF56Method() {
                      super("f56",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f56((java.time.Duration)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f56((java.time.Duration)(argument));
                    }

                    public MetaParameter<java.time.Duration> f56Parameter() {
                      return f56Parameter;
                    }
                  }

                  public static final class MetaF57Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<io.art.message.pack.test.model.Model.ModelEnum> f57Parameter = register(new MetaParameter<>(0, "f57",metaEnum(io.art.message.pack.test.model.Model.ModelEnum.class, io.art.message.pack.test.model.Model.ModelEnum::valueOf)));

                    private MetaF57Method() {
                      super("f57",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f57((io.art.message.pack.test.model.Model.ModelEnum)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f57((io.art.message.pack.test.model.Model.ModelEnum)(argument));
                    }

                    public MetaParameter<io.art.message.pack.test.model.Model.ModelEnum> f57Parameter(
                        ) {
                      return f57Parameter;
                    }
                  }

                  public static final class MetaF58Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter = register(new MetaParameter<>(0, "f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))))));

                    private MetaF58Method() {
                      super("f58",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f58((java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f58((java.util.Optional)(argument));
                    }

                    public MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter(
                        ) {
                      return f58Parameter;
                    }
                  }

                  public static final class MetaF59Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.List<io.art.message.pack.test.model.Model>> f59Parameter = register(new MetaParameter<>(0, "f59",metaType(java.util.List.class,metaType(io.art.message.pack.test.model.Model.class))));

                    private MetaF59Method() {
                      super("f59",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f59((java.util.List<io.art.message.pack.test.model.Model>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f59((java.util.List)(argument));
                    }

                    public MetaParameter<java.util.List<io.art.message.pack.test.model.Model>> f59Parameter(
                        ) {
                      return f59Parameter;
                    }
                  }

                  public static final class MetaF60Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Set<io.art.message.pack.test.model.Model>> f60Parameter = register(new MetaParameter<>(0, "f60",metaType(java.util.Set.class,metaType(io.art.message.pack.test.model.Model.class))));

                    private MetaF60Method() {
                      super("f60",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f60((java.util.Set<io.art.message.pack.test.model.Model>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f60((java.util.Set)(argument));
                    }

                    public MetaParameter<java.util.Set<io.art.message.pack.test.model.Model>> f60Parameter(
                        ) {
                      return f60Parameter;
                    }
                  }

                  public static final class MetaF61Method extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model.ModelBuilder> {
                    private final MetaParameter<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Parameter = register(new MetaParameter<>(0, "f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.message.pack.test.model.Model.class))));

                    private MetaF61Method() {
                      super("f61",metaType(io.art.message.pack.test.model.Model.ModelBuilder.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.f61((java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>)(arguments[0]));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object argument) throws Throwable {
                      return instance.f61((java.util.Map)(argument));
                    }

                    public MetaParameter<java.util.Map<java.lang.String, io.art.message.pack.test.model.Model>> f61Parameter(
                        ) {
                      return f61Parameter;
                    }
                  }

                  public static final class MetaBuildMethod extends InstanceMetaMethod<io.art.message.pack.test.model.Model.ModelBuilder, io.art.message.pack.test.model.Model> {
                    private MetaBuildMethod() {
                      super("build",metaType(io.art.message.pack.test.model.Model.class));
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance,
                        java.lang.Object[] arguments) throws Throwable {
                      return instance.build();
                    }

                    @Override
                    public java.lang.Object invoke(
                        io.art.message.pack.test.model.Model.ModelBuilder instance) throws
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
