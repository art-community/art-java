package io.art.meta.test.meta;

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
public class MetaMetaTest extends MetaLibrary {
  private final MetaIoPackage ioPackage = registerPackage(new MetaIoPackage());

  public MetaMetaTest(MetaLibrary... dependencies) {
    super(dependencies);
  }

  public MetaIoPackage ioPackage() {
    return ioPackage;
  }

  public static final class MetaIoPackage extends MetaPackage {
    private final MetaArtPackage artPackage = registerPackage(new MetaArtPackage());

    private MetaIoPackage() {
      super("io");
    }

    public MetaArtPackage artPackage() {
      return artPackage;
    }

    public static final class MetaArtPackage extends MetaPackage {
      private final MetaMetaPackage metaPackage = registerPackage(new MetaMetaPackage());

      private MetaArtPackage() {
        super("art");
      }

      public MetaMetaPackage metaPackage() {
        return metaPackage;
      }

      public static final class MetaMetaPackage extends MetaPackage {
        private final MetaTestPackage testPackage = registerPackage(new MetaTestPackage());

        private MetaMetaPackage() {
          super("meta");
        }

        public MetaTestPackage testPackage() {
          return testPackage;
        }

        public static final class MetaTestPackage extends MetaPackage {
          private final MetaTestingMetaConfigurationGeneratorClass testingMetaConfigurationGeneratorClass = registerClass(new MetaTestingMetaConfigurationGeneratorClass());

          private final MetaTestingMetaConfigurationClass testingMetaConfigurationClass = registerClass(new MetaTestingMetaConfigurationClass());

          private final MetaTestingShortMetaModelClass testingShortMetaModelClass = registerClass(new MetaTestingShortMetaModelClass());

          private final MetaTestingMetaModelGeneratorClass testingMetaModelGeneratorClass = registerClass(new MetaTestingMetaModelGeneratorClass());

          private final MetaTestingMetaModelClass testingMetaModelClass = registerClass(new MetaTestingMetaModelClass());

          private MetaTestPackage() {
            super("test");
          }

          public MetaTestingMetaConfigurationGeneratorClass testingMetaConfigurationGeneratorClass(
              ) {
            return testingMetaConfigurationGeneratorClass;
          }

          public MetaTestingMetaConfigurationClass testingMetaConfigurationClass() {
            return testingMetaConfigurationClass;
          }

          public MetaTestingShortMetaModelClass testingShortMetaModelClass() {
            return testingShortMetaModelClass;
          }

          public MetaTestingMetaModelGeneratorClass testingMetaModelGeneratorClass() {
            return testingMetaModelGeneratorClass;
          }

          public MetaTestingMetaModelClass testingMetaModelClass() {
            return testingMetaModelClass;
          }

          public static final class MetaTestingMetaConfigurationGeneratorClass extends MetaClass<io.art.meta.test.TestingMetaConfigurationGenerator> {
            private static final io.art.core.property.LazyProperty<MetaTestingMetaConfigurationGeneratorClass> self = MetaClass.self(io.art.meta.test.TestingMetaConfigurationGenerator.class);

            private final MetaGenerateTestingConfigurationMethod generateTestingConfigurationMethod = registerMethod(new MetaGenerateTestingConfigurationMethod(this));

            private MetaTestingMetaConfigurationGeneratorClass() {
              super(metaType(io.art.meta.test.TestingMetaConfigurationGenerator.class));
            }

            public static MetaTestingMetaConfigurationGeneratorClass testingMetaConfigurationGenerator(
                ) {
              return self.get();
            }

            public MetaGenerateTestingConfigurationMethod generateTestingConfigurationMethod() {
              return generateTestingConfigurationMethod;
            }

            public final class MetaGenerateTestingConfigurationMethod extends StaticMetaMethod<MetaTestingMetaConfigurationGeneratorClass, io.art.meta.test.TestingMetaConfiguration> {
              private MetaGenerateTestingConfigurationMethod(
                  MetaTestingMetaConfigurationGeneratorClass owner) {
                super("generateTestingConfiguration",metaType(io.art.meta.test.TestingMetaConfiguration.class),owner);
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                return io.art.meta.test.TestingMetaConfigurationGenerator.generateTestingConfiguration();
              }

              @Override
              public Object invoke() throws Throwable {
                return io.art.meta.test.TestingMetaConfigurationGenerator.generateTestingConfiguration();
              }
            }
          }

          public static final class MetaTestingMetaConfigurationClass extends MetaClass<io.art.meta.test.TestingMetaConfiguration> {
            private static final io.art.core.property.LazyProperty<MetaTestingMetaConfigurationClass> self = MetaClass.self(io.art.meta.test.TestingMetaConfiguration.class);

            private final MetaConstructorConstructor constructor = registerConstructor(new MetaConstructorConstructor(this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Integer> f1Field = registerField(new MetaField<>("f1",metaType(int.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Short> f2Field = registerField(new MetaField<>("f2",metaType(short.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Double> f3Field = registerField(new MetaField<>("f3",metaType(double.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Float> f4Field = registerField(new MetaField<>("f4",metaType(float.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Long> f5Field = registerField(new MetaField<>("f5",metaType(long.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Boolean> f6Field = registerField(new MetaField<>("f6",metaType(boolean.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Character> f7Field = registerField(new MetaField<>("f7",metaType(char.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, Byte> f8Field = registerField(new MetaField<>("f8",metaType(byte.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Integer> f9Field = registerField(new MetaField<>("f9",metaType(java.lang.Integer.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Short> f10Field = registerField(new MetaField<>("f10",metaType(java.lang.Short.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Double> f11Field = registerField(new MetaField<>("f11",metaType(java.lang.Double.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Float> f12Field = registerField(new MetaField<>("f12",metaType(java.lang.Float.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Long> f13Field = registerField(new MetaField<>("f13",metaType(java.lang.Long.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Boolean> f14Field = registerField(new MetaField<>("f14",metaType(java.lang.Boolean.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Character> f15Field = registerField(new MetaField<>("f15",metaType(java.lang.Character.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.String> f16Field = registerField(new MetaField<>("f16",metaType(java.lang.String.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, int[]> f17Field = registerField(new MetaField<>("f17",metaArray(int[].class, int[]::new, metaType(int.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, short[]> f18Field = registerField(new MetaField<>("f18",metaArray(short[].class, short[]::new, metaType(short.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, double[]> f19Field = registerField(new MetaField<>("f19",metaArray(double[].class, double[]::new, metaType(double.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, float[]> f20Field = registerField(new MetaField<>("f20",metaArray(float[].class, float[]::new, metaType(float.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, long[]> f21Field = registerField(new MetaField<>("f21",metaArray(long[].class, long[]::new, metaType(long.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, boolean[]> f22Field = registerField(new MetaField<>("f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, char[]> f23Field = registerField(new MetaField<>("f23",metaArray(char[].class, char[]::new, metaType(char.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Integer[]> f24Field = registerField(new MetaField<>("f24",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Short[]> f25Field = registerField(new MetaField<>("f25",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Double[]> f26Field = registerField(new MetaField<>("f26",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Float[]> f27Field = registerField(new MetaField<>("f27",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Long[]> f28Field = registerField(new MetaField<>("f28",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Boolean[]> f29Field = registerField(new MetaField<>("f29",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.Character[]> f30Field = registerField(new MetaField<>("f30",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.String[]> f31Field = registerField(new MetaField<>("f31",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.lang.String>> f32Field = registerField(new MetaField<>("f32",metaType(java.util.List.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Set<java.lang.String>> f33Field = registerField(new MetaField<>("f33",metaType(java.util.Set.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Collection<java.lang.String>> f34Field = registerField(new MetaField<>("f34",metaType(java.util.Collection.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, io.art.core.collection.ImmutableArray<java.lang.String>> f35Field = registerField(new MetaField<>("f35",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, io.art.core.collection.ImmutableSet<java.lang.String>> f36Field = registerField(new MetaField<>("f36",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.function.Supplier<java.lang.String>> f37Field = registerField(new MetaField<>("f37",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, io.art.core.property.LazyProperty<java.lang.String>> f38Field = registerField(new MetaField<>("f38",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<int[]>> f39Field = registerField(new MetaField<>("f39",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.lang.String[]>> f40Field = registerField(new MetaField<>("f40",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<int[]>[]> f41Field = registerField(new MetaField<>("f41",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.lang.String[]>[]> f42Field = registerField(new MetaField<>("f42",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.util.List<java.lang.String>>> f43Field = registerField(new MetaField<>("f43",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.util.List<java.lang.String>[]>> f44Field = registerField(new MetaField<>("f44",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, java.lang.String[]>> f45Field = registerField(new MetaField<>("f45",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Field = registerField(new MetaField<>("f46",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Field = registerField(new MetaField<>("f47",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Field = registerField(new MetaField<>("f48",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration> f49Field = registerField(new MetaField<>("f49",metaType(io.art.meta.test.TestingMetaConfiguration.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.time.LocalDateTime> f50Field = registerField(new MetaField<>("f50",metaType(java.time.LocalDateTime.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.time.ZonedDateTime> f51Field = registerField(new MetaField<>("f51",metaType(java.time.ZonedDateTime.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.time.Duration> f52Field = registerField(new MetaField<>("f52",metaType(java.time.Duration.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Field = registerField(new MetaField<>("f53",metaEnum(io.art.meta.test.TestingMetaConfiguration.ModelEnum.class, io.art.meta.test.TestingMetaConfiguration.ModelEnum::valueOf),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Field = registerField(new MetaField<>("f54",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Field = registerField(new MetaField<>("f55",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaConfiguration.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Field = registerField(new MetaField<>("f56",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaConfiguration.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Field = registerField(new MetaField<>("f57",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaConfiguration.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, reactor.core.publisher.Mono<java.lang.String>> f58Field = registerField(new MetaField<>("f58",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, reactor.core.publisher.Flux<java.lang.String>> f59Field = registerField(new MetaField<>("f59",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.util.stream.Stream<java.lang.String>> f60Field = registerField(new MetaField<>("f60",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, reactor.core.publisher.Mono<java.lang.String[]>> f61Field = registerField(new MetaField<>("f61",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.String> f62Field = registerField(new MetaField<>("f62",metaType(java.lang.String.class),false,this));

            private final MetaField<MetaTestingMetaConfigurationClass, java.lang.String> f63Field = registerField(new MetaField<>("f63",metaType(java.lang.String.class),false,this));

            private final MetaAssertEqualsMethod assertEqualsMethod = registerMethod(new MetaAssertEqualsMethod(this));

            private final MetaToBuilderMethod toBuilderMethod = registerMethod(new MetaToBuilderMethod(this));

            private final MetaGetF1Method getF1Method = registerMethod(new MetaGetF1Method(this));

            private final MetaGetF2Method getF2Method = registerMethod(new MetaGetF2Method(this));

            private final MetaGetF3Method getF3Method = registerMethod(new MetaGetF3Method(this));

            private final MetaGetF4Method getF4Method = registerMethod(new MetaGetF4Method(this));

            private final MetaGetF5Method getF5Method = registerMethod(new MetaGetF5Method(this));

            private final MetaIsF6Method isF6Method = registerMethod(new MetaIsF6Method(this));

            private final MetaGetF7Method getF7Method = registerMethod(new MetaGetF7Method(this));

            private final MetaGetF8Method getF8Method = registerMethod(new MetaGetF8Method(this));

            private final MetaGetF9Method getF9Method = registerMethod(new MetaGetF9Method(this));

            private final MetaGetF10Method getF10Method = registerMethod(new MetaGetF10Method(this));

            private final MetaGetF11Method getF11Method = registerMethod(new MetaGetF11Method(this));

            private final MetaGetF12Method getF12Method = registerMethod(new MetaGetF12Method(this));

            private final MetaGetF13Method getF13Method = registerMethod(new MetaGetF13Method(this));

            private final MetaGetF14Method getF14Method = registerMethod(new MetaGetF14Method(this));

            private final MetaGetF15Method getF15Method = registerMethod(new MetaGetF15Method(this));

            private final MetaGetF16Method getF16Method = registerMethod(new MetaGetF16Method(this));

            private final MetaGetF17Method getF17Method = registerMethod(new MetaGetF17Method(this));

            private final MetaGetF18Method getF18Method = registerMethod(new MetaGetF18Method(this));

            private final MetaGetF19Method getF19Method = registerMethod(new MetaGetF19Method(this));

            private final MetaGetF20Method getF20Method = registerMethod(new MetaGetF20Method(this));

            private final MetaGetF21Method getF21Method = registerMethod(new MetaGetF21Method(this));

            private final MetaGetF22Method getF22Method = registerMethod(new MetaGetF22Method(this));

            private final MetaGetF23Method getF23Method = registerMethod(new MetaGetF23Method(this));

            private final MetaGetF24Method getF24Method = registerMethod(new MetaGetF24Method(this));

            private final MetaGetF25Method getF25Method = registerMethod(new MetaGetF25Method(this));

            private final MetaGetF26Method getF26Method = registerMethod(new MetaGetF26Method(this));

            private final MetaGetF27Method getF27Method = registerMethod(new MetaGetF27Method(this));

            private final MetaGetF28Method getF28Method = registerMethod(new MetaGetF28Method(this));

            private final MetaGetF29Method getF29Method = registerMethod(new MetaGetF29Method(this));

            private final MetaGetF30Method getF30Method = registerMethod(new MetaGetF30Method(this));

            private final MetaGetF31Method getF31Method = registerMethod(new MetaGetF31Method(this));

            private final MetaGetF32Method getF32Method = registerMethod(new MetaGetF32Method(this));

            private final MetaGetF33Method getF33Method = registerMethod(new MetaGetF33Method(this));

            private final MetaGetF34Method getF34Method = registerMethod(new MetaGetF34Method(this));

            private final MetaGetF35Method getF35Method = registerMethod(new MetaGetF35Method(this));

            private final MetaGetF36Method getF36Method = registerMethod(new MetaGetF36Method(this));

            private final MetaGetF37Method getF37Method = registerMethod(new MetaGetF37Method(this));

            private final MetaGetF38Method getF38Method = registerMethod(new MetaGetF38Method(this));

            private final MetaGetF39Method getF39Method = registerMethod(new MetaGetF39Method(this));

            private final MetaGetF40Method getF40Method = registerMethod(new MetaGetF40Method(this));

            private final MetaGetF41Method getF41Method = registerMethod(new MetaGetF41Method(this));

            private final MetaGetF42Method getF42Method = registerMethod(new MetaGetF42Method(this));

            private final MetaGetF43Method getF43Method = registerMethod(new MetaGetF43Method(this));

            private final MetaGetF44Method getF44Method = registerMethod(new MetaGetF44Method(this));

            private final MetaGetF45Method getF45Method = registerMethod(new MetaGetF45Method(this));

            private final MetaGetF46Method getF46Method = registerMethod(new MetaGetF46Method(this));

            private final MetaGetF47Method getF47Method = registerMethod(new MetaGetF47Method(this));

            private final MetaGetF48Method getF48Method = registerMethod(new MetaGetF48Method(this));

            private final MetaGetF49Method getF49Method = registerMethod(new MetaGetF49Method(this));

            private final MetaGetF50Method getF50Method = registerMethod(new MetaGetF50Method(this));

            private final MetaGetF51Method getF51Method = registerMethod(new MetaGetF51Method(this));

            private final MetaGetF52Method getF52Method = registerMethod(new MetaGetF52Method(this));

            private final MetaGetF53Method getF53Method = registerMethod(new MetaGetF53Method(this));

            private final MetaGetF54Method getF54Method = registerMethod(new MetaGetF54Method(this));

            private final MetaGetF55Method getF55Method = registerMethod(new MetaGetF55Method(this));

            private final MetaGetF56Method getF56Method = registerMethod(new MetaGetF56Method(this));

            private final MetaGetF57Method getF57Method = registerMethod(new MetaGetF57Method(this));

            private final MetaGetF58Method getF58Method = registerMethod(new MetaGetF58Method(this));

            private final MetaGetF59Method getF59Method = registerMethod(new MetaGetF59Method(this));

            private final MetaGetF60Method getF60Method = registerMethod(new MetaGetF60Method(this));

            private final MetaGetF61Method getF61Method = registerMethod(new MetaGetF61Method(this));

            private final MetaGetF62Method getF62Method = registerMethod(new MetaGetF62Method(this));

            private final MetaGetF63Method getF63Method = registerMethod(new MetaGetF63Method(this));

            private final MetaTestingMetaConfigurationBuilderClass testingMetaConfigurationBuilderClass = registerClass(new MetaTestingMetaConfigurationBuilderClass());

            private MetaTestingMetaConfigurationClass() {
              super(metaType(io.art.meta.test.TestingMetaConfiguration.class));
            }

            public static MetaTestingMetaConfigurationClass testingMetaConfiguration() {
              return self.get();
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Integer> f1Field() {
              return f1Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Short> f2Field() {
              return f2Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Double> f3Field() {
              return f3Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Float> f4Field() {
              return f4Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Long> f5Field() {
              return f5Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Boolean> f6Field() {
              return f6Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Character> f7Field() {
              return f7Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, Byte> f8Field() {
              return f8Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Integer> f9Field() {
              return f9Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Short> f10Field() {
              return f10Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Double> f11Field() {
              return f11Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Float> f12Field() {
              return f12Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Long> f13Field() {
              return f13Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Boolean> f14Field() {
              return f14Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Character> f15Field() {
              return f15Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.String> f16Field() {
              return f16Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, int[]> f17Field() {
              return f17Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, short[]> f18Field() {
              return f18Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, double[]> f19Field() {
              return f19Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, float[]> f20Field() {
              return f20Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, long[]> f21Field() {
              return f21Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, boolean[]> f22Field() {
              return f22Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, char[]> f23Field() {
              return f23Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Integer[]> f24Field() {
              return f24Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Short[]> f25Field() {
              return f25Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Double[]> f26Field() {
              return f26Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Float[]> f27Field() {
              return f27Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Long[]> f28Field() {
              return f28Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Boolean[]> f29Field() {
              return f29Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.Character[]> f30Field() {
              return f30Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.String[]> f31Field() {
              return f31Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.lang.String>> f32Field(
                ) {
              return f32Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Set<java.lang.String>> f33Field(
                ) {
              return f33Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Collection<java.lang.String>> f34Field(
                ) {
              return f34Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, io.art.core.collection.ImmutableArray<java.lang.String>> f35Field(
                ) {
              return f35Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, io.art.core.collection.ImmutableSet<java.lang.String>> f36Field(
                ) {
              return f36Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.function.Supplier<java.lang.String>> f37Field(
                ) {
              return f37Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, io.art.core.property.LazyProperty<java.lang.String>> f38Field(
                ) {
              return f38Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<int[]>> f39Field() {
              return f39Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.lang.String[]>> f40Field(
                ) {
              return f40Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<int[]>[]> f41Field(
                ) {
              return f41Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.lang.String[]>[]> f42Field(
                ) {
              return f42Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.util.List<java.lang.String>>> f43Field(
                ) {
              return f43Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<java.util.List<java.lang.String>[]>> f44Field(
                ) {
              return f44Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, java.lang.String[]>> f45Field(
                ) {
              return f45Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Field(
                ) {
              return f46Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Field(
                ) {
              return f47Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Field(
                ) {
              return f48Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration> f49Field(
                ) {
              return f49Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.time.LocalDateTime> f50Field(
                ) {
              return f50Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.time.ZonedDateTime> f51Field(
                ) {
              return f51Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.time.Duration> f52Field() {
              return f52Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Field(
                ) {
              return f53Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Field(
                ) {
              return f54Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Field(
                ) {
              return f55Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Field(
                ) {
              return f56Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Field(
                ) {
              return f57Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, reactor.core.publisher.Mono<java.lang.String>> f58Field(
                ) {
              return f58Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, reactor.core.publisher.Flux<java.lang.String>> f59Field(
                ) {
              return f59Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.util.stream.Stream<java.lang.String>> f60Field(
                ) {
              return f60Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, reactor.core.publisher.Mono<java.lang.String[]>> f61Field(
                ) {
              return f61Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.String> f62Field() {
              return f62Field;
            }

            public MetaField<MetaTestingMetaConfigurationClass, java.lang.String> f63Field() {
              return f63Field;
            }

            public MetaAssertEqualsMethod assertEqualsMethod() {
              return assertEqualsMethod;
            }

            public MetaToBuilderMethod toBuilderMethod() {
              return toBuilderMethod;
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

            public MetaGetF62Method getF62Method() {
              return getF62Method;
            }

            public MetaGetF63Method getF63Method() {
              return getF63Method;
            }

            public MetaTestingMetaConfigurationBuilderClass testingMetaConfigurationBuilderClass() {
              return testingMetaConfigurationBuilderClass;
            }

            public final class MetaConstructorConstructor extends MetaConstructor<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration> {
              private final MetaParameter<java.lang.Integer> f1Parameter = register(new MetaParameter<>(0, "f1",metaType(int.class)));

              private final MetaParameter<java.lang.Short> f2Parameter = register(new MetaParameter<>(1, "f2",metaType(short.class)));

              private final MetaParameter<java.lang.Double> f3Parameter = register(new MetaParameter<>(2, "f3",metaType(double.class)));

              private final MetaParameter<java.lang.Float> f4Parameter = register(new MetaParameter<>(3, "f4",metaType(float.class)));

              private final MetaParameter<java.lang.Long> f5Parameter = register(new MetaParameter<>(4, "f5",metaType(long.class)));

              private final MetaParameter<java.lang.Boolean> f6Parameter = register(new MetaParameter<>(5, "f6",metaType(boolean.class)));

              private final MetaParameter<java.lang.Character> f7Parameter = register(new MetaParameter<>(6, "f7",metaType(char.class)));

              private final MetaParameter<Byte> f8Parameter = register(new MetaParameter<>(7, "f8",metaType(byte.class)));

              private final MetaParameter<java.lang.Integer> f9Parameter = register(new MetaParameter<>(8, "f9",metaType(java.lang.Integer.class)));

              private final MetaParameter<java.lang.Short> f10Parameter = register(new MetaParameter<>(9, "f10",metaType(java.lang.Short.class)));

              private final MetaParameter<java.lang.Double> f11Parameter = register(new MetaParameter<>(10, "f11",metaType(java.lang.Double.class)));

              private final MetaParameter<java.lang.Float> f12Parameter = register(new MetaParameter<>(11, "f12",metaType(java.lang.Float.class)));

              private final MetaParameter<java.lang.Long> f13Parameter = register(new MetaParameter<>(12, "f13",metaType(java.lang.Long.class)));

              private final MetaParameter<java.lang.Boolean> f14Parameter = register(new MetaParameter<>(13, "f14",metaType(java.lang.Boolean.class)));

              private final MetaParameter<java.lang.Character> f15Parameter = register(new MetaParameter<>(14, "f15",metaType(java.lang.Character.class)));

              private final MetaParameter<java.lang.String> f16Parameter = register(new MetaParameter<>(15, "f16",metaType(java.lang.String.class)));

              private final MetaParameter<int[]> f17Parameter = register(new MetaParameter<>(16, "f17",metaArray(int[].class, int[]::new, metaType(int.class))));

              private final MetaParameter<short[]> f18Parameter = register(new MetaParameter<>(17, "f18",metaArray(short[].class, short[]::new, metaType(short.class))));

              private final MetaParameter<double[]> f19Parameter = register(new MetaParameter<>(18, "f19",metaArray(double[].class, double[]::new, metaType(double.class))));

              private final MetaParameter<float[]> f20Parameter = register(new MetaParameter<>(19, "f20",metaArray(float[].class, float[]::new, metaType(float.class))));

              private final MetaParameter<long[]> f21Parameter = register(new MetaParameter<>(20, "f21",metaArray(long[].class, long[]::new, metaType(long.class))));

              private final MetaParameter<boolean[]> f22Parameter = register(new MetaParameter<>(21, "f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class))));

              private final MetaParameter<char[]> f23Parameter = register(new MetaParameter<>(22, "f23",metaArray(char[].class, char[]::new, metaType(char.class))));

              private final MetaParameter<java.lang.Integer[]> f24Parameter = register(new MetaParameter<>(23, "f24",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class))));

              private final MetaParameter<java.lang.Short[]> f25Parameter = register(new MetaParameter<>(24, "f25",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class))));

              private final MetaParameter<java.lang.Double[]> f26Parameter = register(new MetaParameter<>(25, "f26",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class))));

              private final MetaParameter<java.lang.Float[]> f27Parameter = register(new MetaParameter<>(26, "f27",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class))));

              private final MetaParameter<java.lang.Long[]> f28Parameter = register(new MetaParameter<>(27, "f28",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class))));

              private final MetaParameter<java.lang.Boolean[]> f29Parameter = register(new MetaParameter<>(28, "f29",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class))));

              private final MetaParameter<java.lang.Character[]> f30Parameter = register(new MetaParameter<>(29, "f30",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class))));

              private final MetaParameter<java.lang.String[]> f31Parameter = register(new MetaParameter<>(30, "f31",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));

              private final MetaParameter<java.util.List<java.lang.String>> f32Parameter = register(new MetaParameter<>(31, "f32",metaType(java.util.List.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.Set<java.lang.String>> f33Parameter = register(new MetaParameter<>(32, "f33",metaType(java.util.Set.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.Collection<java.lang.String>> f34Parameter = register(new MetaParameter<>(33, "f34",metaType(java.util.Collection.class,metaType(java.lang.String.class))));

              private final MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f35Parameter = register(new MetaParameter<>(34, "f35",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class))));

              private final MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f36Parameter = register(new MetaParameter<>(35, "f36",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.function.Supplier<java.lang.String>> f37Parameter = register(new MetaParameter<>(36, "f37",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class))));

              private final MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f38Parameter = register(new MetaParameter<>(37, "f38",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.List<int[]>> f39Parameter = register(new MetaParameter<>(38, "f39",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))));

              private final MetaParameter<java.util.List<java.lang.String[]>> f40Parameter = register(new MetaParameter<>(39, "f40",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.List<int[]>[]> f41Parameter = register(new MetaParameter<>(40, "f41",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))))));

              private final MetaParameter<java.util.List<java.lang.String[]>[]> f42Parameter = register(new MetaParameter<>(41, "f42",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))))));

              private final MetaParameter<java.util.List<java.util.List<java.lang.String>>> f43Parameter = register(new MetaParameter<>(42, "f43",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f44Parameter = register(new MetaParameter<>(43, "f44",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class))))));

              private final MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f45Parameter = register(new MetaParameter<>(44, "f45",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Parameter = register(new MetaParameter<>(45, "f46",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Parameter = register(new MetaParameter<>(46, "f47",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

              private final MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Parameter = register(new MetaParameter<>(47, "f48",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

              private final MetaParameter<io.art.meta.test.TestingMetaConfiguration> f49Parameter = register(new MetaParameter<>(48, "f49",metaType(io.art.meta.test.TestingMetaConfiguration.class)));

              private final MetaParameter<java.time.LocalDateTime> f50Parameter = register(new MetaParameter<>(49, "f50",metaType(java.time.LocalDateTime.class)));

              private final MetaParameter<java.time.ZonedDateTime> f51Parameter = register(new MetaParameter<>(50, "f51",metaType(java.time.ZonedDateTime.class)));

              private final MetaParameter<java.time.Duration> f52Parameter = register(new MetaParameter<>(51, "f52",metaType(java.time.Duration.class)));

              private final MetaParameter<io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Parameter = register(new MetaParameter<>(52, "f53",metaEnum(io.art.meta.test.TestingMetaConfiguration.ModelEnum.class, io.art.meta.test.TestingMetaConfiguration.ModelEnum::valueOf)));

              private final MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Parameter = register(new MetaParameter<>(53, "f54",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))))));

              private final MetaParameter<java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Parameter = register(new MetaParameter<>(54, "f55",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaConfiguration.class))));

              private final MetaParameter<java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Parameter = register(new MetaParameter<>(55, "f56",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaConfiguration.class))));

              private final MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Parameter = register(new MetaParameter<>(56, "f57",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaConfiguration.class))));

              private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f58Parameter = register(new MetaParameter<>(57, "f58",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

              private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f59Parameter = register(new MetaParameter<>(58, "f59",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.stream.Stream<java.lang.String>> f60Parameter = register(new MetaParameter<>(59, "f60",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class))));

              private final MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f61Parameter = register(new MetaParameter<>(60, "f61",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

              private final MetaParameter<java.lang.String> f62Parameter = register(new MetaParameter<>(61, "f62",metaType(java.lang.String.class)));

              private final MetaParameter<java.lang.String> f63Parameter = register(new MetaParameter<>(62, "f63",metaType(java.lang.String.class)));

              private MetaConstructorConstructor(MetaTestingMetaConfigurationClass owner) {
                super(metaType(io.art.meta.test.TestingMetaConfiguration.class),owner);
              }

              @Override
              public io.art.meta.test.TestingMetaConfiguration invoke(Object[] arguments) throws
                  Throwable {
                return new io.art.meta.test.TestingMetaConfiguration((int)(arguments[0]),(short)(arguments[1]),(double)(arguments[2]),(float)(arguments[3]),(long)(arguments[4]),(boolean)(arguments[5]),(char)(arguments[6]),(byte)(arguments[7]),(java.lang.Integer)(arguments[8]),(java.lang.Short)(arguments[9]),(java.lang.Double)(arguments[10]),(java.lang.Float)(arguments[11]),(java.lang.Long)(arguments[12]),(java.lang.Boolean)(arguments[13]),(java.lang.Character)(arguments[14]),(java.lang.String)(arguments[15]),(int[])(arguments[16]),(short[])(arguments[17]),(double[])(arguments[18]),(float[])(arguments[19]),(long[])(arguments[20]),(boolean[])(arguments[21]),(char[])(arguments[22]),(java.lang.Integer[])(arguments[23]),(java.lang.Short[])(arguments[24]),(java.lang.Double[])(arguments[25]),(java.lang.Float[])(arguments[26]),(java.lang.Long[])(arguments[27]),(java.lang.Boolean[])(arguments[28]),(java.lang.Character[])(arguments[29]),(java.lang.String[])(arguments[30]),(java.util.List<java.lang.String>)(arguments[31]),(java.util.Set<java.lang.String>)(arguments[32]),(java.util.Collection<java.lang.String>)(arguments[33]),(io.art.core.collection.ImmutableArray<java.lang.String>)(arguments[34]),(io.art.core.collection.ImmutableSet<java.lang.String>)(arguments[35]),(java.util.function.Supplier<java.lang.String>)(arguments[36]),(io.art.core.property.LazyProperty<java.lang.String>)(arguments[37]),(java.util.List<int[]>)(arguments[38]),(java.util.List<java.lang.String[]>)(arguments[39]),(java.util.List<int[]>[])(arguments[40]),(java.util.List<java.lang.String[]>[])(arguments[41]),(java.util.List<java.util.List<java.lang.String>>)(arguments[42]),(java.util.List<java.util.List<java.lang.String>[]>)(arguments[43]),(java.util.Map<java.lang.String, java.lang.String[]>)(arguments[44]),(java.util.Map<java.lang.String, java.util.List<java.lang.String>>)(arguments[45]),(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[46]),(io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[47]),(io.art.meta.test.TestingMetaConfiguration)(arguments[48]),(java.time.LocalDateTime)(arguments[49]),(java.time.ZonedDateTime)(arguments[50]),(java.time.Duration)(arguments[51]),(io.art.meta.test.TestingMetaConfiguration.ModelEnum)(arguments[52]),(java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>)(arguments[53]),(java.util.List<io.art.meta.test.TestingMetaConfiguration>)(arguments[54]),(java.util.Set<io.art.meta.test.TestingMetaConfiguration>)(arguments[55]),(java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>)(arguments[56]),(reactor.core.publisher.Mono<java.lang.String>)(arguments[57]),(reactor.core.publisher.Flux<java.lang.String>)(arguments[58]),(java.util.stream.Stream<java.lang.String>)(arguments[59]),(reactor.core.publisher.Mono<java.lang.String[]>)(arguments[60]),(java.lang.String)(arguments[61]),(java.lang.String)(arguments[62]));
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

              public MetaParameter<java.lang.Integer[]> f24Parameter() {
                return f24Parameter;
              }

              public MetaParameter<java.lang.Short[]> f25Parameter() {
                return f25Parameter;
              }

              public MetaParameter<java.lang.Double[]> f26Parameter() {
                return f26Parameter;
              }

              public MetaParameter<java.lang.Float[]> f27Parameter() {
                return f27Parameter;
              }

              public MetaParameter<java.lang.Long[]> f28Parameter() {
                return f28Parameter;
              }

              public MetaParameter<java.lang.Boolean[]> f29Parameter() {
                return f29Parameter;
              }

              public MetaParameter<java.lang.Character[]> f30Parameter() {
                return f30Parameter;
              }

              public MetaParameter<java.lang.String[]> f31Parameter() {
                return f31Parameter;
              }

              public MetaParameter<java.util.List<java.lang.String>> f32Parameter() {
                return f32Parameter;
              }

              public MetaParameter<java.util.Set<java.lang.String>> f33Parameter() {
                return f33Parameter;
              }

              public MetaParameter<java.util.Collection<java.lang.String>> f34Parameter() {
                return f34Parameter;
              }

              public MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f35Parameter(
                  ) {
                return f35Parameter;
              }

              public MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f36Parameter(
                  ) {
                return f36Parameter;
              }

              public MetaParameter<java.util.function.Supplier<java.lang.String>> f37Parameter() {
                return f37Parameter;
              }

              public MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f38Parameter(
                  ) {
                return f38Parameter;
              }

              public MetaParameter<java.util.List<int[]>> f39Parameter() {
                return f39Parameter;
              }

              public MetaParameter<java.util.List<java.lang.String[]>> f40Parameter() {
                return f40Parameter;
              }

              public MetaParameter<java.util.List<int[]>[]> f41Parameter() {
                return f41Parameter;
              }

              public MetaParameter<java.util.List<java.lang.String[]>[]> f42Parameter() {
                return f42Parameter;
              }

              public MetaParameter<java.util.List<java.util.List<java.lang.String>>> f43Parameter(
                  ) {
                return f43Parameter;
              }

              public MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f44Parameter(
                  ) {
                return f44Parameter;
              }

              public MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f45Parameter(
                  ) {
                return f45Parameter;
              }

              public MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Parameter(
                  ) {
                return f46Parameter;
              }

              public MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Parameter(
                  ) {
                return f47Parameter;
              }

              public MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Parameter(
                  ) {
                return f48Parameter;
              }

              public MetaParameter<io.art.meta.test.TestingMetaConfiguration> f49Parameter() {
                return f49Parameter;
              }

              public MetaParameter<java.time.LocalDateTime> f50Parameter() {
                return f50Parameter;
              }

              public MetaParameter<java.time.ZonedDateTime> f51Parameter() {
                return f51Parameter;
              }

              public MetaParameter<java.time.Duration> f52Parameter() {
                return f52Parameter;
              }

              public MetaParameter<io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Parameter(
                  ) {
                return f53Parameter;
              }

              public MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Parameter(
                  ) {
                return f54Parameter;
              }

              public MetaParameter<java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Parameter(
                  ) {
                return f55Parameter;
              }

              public MetaParameter<java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Parameter(
                  ) {
                return f56Parameter;
              }

              public MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Parameter(
                  ) {
                return f57Parameter;
              }

              public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f58Parameter() {
                return f58Parameter;
              }

              public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f59Parameter() {
                return f59Parameter;
              }

              public MetaParameter<java.util.stream.Stream<java.lang.String>> f60Parameter() {
                return f60Parameter;
              }

              public MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f61Parameter() {
                return f61Parameter;
              }

              public MetaParameter<java.lang.String> f62Parameter() {
                return f62Parameter;
              }

              public MetaParameter<java.lang.String> f63Parameter() {
                return f63Parameter;
              }
            }

            public final class MetaAssertEqualsMethod extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, Void> {
              private final MetaParameter<io.art.meta.test.TestingMetaConfiguration> modelParameter = register(new MetaParameter<>(0, "model",metaType(io.art.meta.test.TestingMetaConfiguration.class)));

              private MetaAssertEqualsMethod(MetaTestingMetaConfigurationClass owner) {
                super("assertEquals",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                instance.assertEquals((io.art.meta.test.TestingMetaConfiguration)(arguments[0]));
                return null;
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object argument) throws Throwable {
                instance.assertEquals((io.art.meta.test.TestingMetaConfiguration)(argument));
                return null;
              }

              public MetaParameter<io.art.meta.test.TestingMetaConfiguration> modelParameter() {
                return modelParameter;
              }
            }

            public final class MetaToBuilderMethod extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
              private MetaToBuilderMethod(MetaTestingMetaConfigurationClass owner) {
                super("toBuilder",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.toBuilder();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.toBuilder();
              }
            }

            public final class MetaGetF1Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Integer> {
              private MetaGetF1Method(MetaTestingMetaConfigurationClass owner) {
                super("getF1",metaType(int.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF1();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF1();
              }
            }

            public final class MetaGetF2Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Short> {
              private MetaGetF2Method(MetaTestingMetaConfigurationClass owner) {
                super("getF2",metaType(short.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF2();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF2();
              }
            }

            public final class MetaGetF3Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Double> {
              private MetaGetF3Method(MetaTestingMetaConfigurationClass owner) {
                super("getF3",metaType(double.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF3();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF3();
              }
            }

            public final class MetaGetF4Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Float> {
              private MetaGetF4Method(MetaTestingMetaConfigurationClass owner) {
                super("getF4",metaType(float.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF4();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF4();
              }
            }

            public final class MetaGetF5Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Long> {
              private MetaGetF5Method(MetaTestingMetaConfigurationClass owner) {
                super("getF5",metaType(long.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF5();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF5();
              }
            }

            public final class MetaIsF6Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Boolean> {
              private MetaIsF6Method(MetaTestingMetaConfigurationClass owner) {
                super("isF6",metaType(boolean.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.isF6();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.isF6();
              }
            }

            public final class MetaGetF7Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Character> {
              private MetaGetF7Method(MetaTestingMetaConfigurationClass owner) {
                super("getF7",metaType(char.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF7();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF7();
              }
            }

            public final class MetaGetF8Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, Byte> {
              private MetaGetF8Method(MetaTestingMetaConfigurationClass owner) {
                super("getF8",metaType(byte.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF8();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF8();
              }
            }

            public final class MetaGetF9Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Integer> {
              private MetaGetF9Method(MetaTestingMetaConfigurationClass owner) {
                super("getF9",metaType(java.lang.Integer.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF9();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF9();
              }
            }

            public final class MetaGetF10Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Short> {
              private MetaGetF10Method(MetaTestingMetaConfigurationClass owner) {
                super("getF10",metaType(java.lang.Short.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF10();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF10();
              }
            }

            public final class MetaGetF11Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Double> {
              private MetaGetF11Method(MetaTestingMetaConfigurationClass owner) {
                super("getF11",metaType(java.lang.Double.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF11();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF11();
              }
            }

            public final class MetaGetF12Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Float> {
              private MetaGetF12Method(MetaTestingMetaConfigurationClass owner) {
                super("getF12",metaType(java.lang.Float.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF12();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF12();
              }
            }

            public final class MetaGetF13Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Long> {
              private MetaGetF13Method(MetaTestingMetaConfigurationClass owner) {
                super("getF13",metaType(java.lang.Long.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF13();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF13();
              }
            }

            public final class MetaGetF14Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Boolean> {
              private MetaGetF14Method(MetaTestingMetaConfigurationClass owner) {
                super("getF14",metaType(java.lang.Boolean.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF14();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF14();
              }
            }

            public final class MetaGetF15Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Character> {
              private MetaGetF15Method(MetaTestingMetaConfigurationClass owner) {
                super("getF15",metaType(java.lang.Character.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF15();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF15();
              }
            }

            public final class MetaGetF16Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.String> {
              private MetaGetF16Method(MetaTestingMetaConfigurationClass owner) {
                super("getF16",metaType(java.lang.String.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF16();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF16();
              }
            }

            public final class MetaGetF17Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, int[]> {
              private MetaGetF17Method(MetaTestingMetaConfigurationClass owner) {
                super("getF17",metaArray(int[].class, int[]::new, metaType(int.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF17();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF17();
              }
            }

            public final class MetaGetF18Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, short[]> {
              private MetaGetF18Method(MetaTestingMetaConfigurationClass owner) {
                super("getF18",metaArray(short[].class, short[]::new, metaType(short.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF18();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF18();
              }
            }

            public final class MetaGetF19Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, double[]> {
              private MetaGetF19Method(MetaTestingMetaConfigurationClass owner) {
                super("getF19",metaArray(double[].class, double[]::new, metaType(double.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF19();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF19();
              }
            }

            public final class MetaGetF20Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, float[]> {
              private MetaGetF20Method(MetaTestingMetaConfigurationClass owner) {
                super("getF20",metaArray(float[].class, float[]::new, metaType(float.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF20();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF20();
              }
            }

            public final class MetaGetF21Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, long[]> {
              private MetaGetF21Method(MetaTestingMetaConfigurationClass owner) {
                super("getF21",metaArray(long[].class, long[]::new, metaType(long.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF21();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF21();
              }
            }

            public final class MetaGetF22Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, boolean[]> {
              private MetaGetF22Method(MetaTestingMetaConfigurationClass owner) {
                super("getF22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF22();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF22();
              }
            }

            public final class MetaGetF23Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, char[]> {
              private MetaGetF23Method(MetaTestingMetaConfigurationClass owner) {
                super("getF23",metaArray(char[].class, char[]::new, metaType(char.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF23();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF23();
              }
            }

            public final class MetaGetF24Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Integer[]> {
              private MetaGetF24Method(MetaTestingMetaConfigurationClass owner) {
                super("getF24",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF24();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF24();
              }
            }

            public final class MetaGetF25Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Short[]> {
              private MetaGetF25Method(MetaTestingMetaConfigurationClass owner) {
                super("getF25",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF25();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF25();
              }
            }

            public final class MetaGetF26Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Double[]> {
              private MetaGetF26Method(MetaTestingMetaConfigurationClass owner) {
                super("getF26",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF26();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF26();
              }
            }

            public final class MetaGetF27Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Float[]> {
              private MetaGetF27Method(MetaTestingMetaConfigurationClass owner) {
                super("getF27",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF27();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF27();
              }
            }

            public final class MetaGetF28Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Long[]> {
              private MetaGetF28Method(MetaTestingMetaConfigurationClass owner) {
                super("getF28",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF28();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF28();
              }
            }

            public final class MetaGetF29Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Boolean[]> {
              private MetaGetF29Method(MetaTestingMetaConfigurationClass owner) {
                super("getF29",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF29();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF29();
              }
            }

            public final class MetaGetF30Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.Character[]> {
              private MetaGetF30Method(MetaTestingMetaConfigurationClass owner) {
                super("getF30",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF30();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF30();
              }
            }

            public final class MetaGetF31Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.String[]> {
              private MetaGetF31Method(MetaTestingMetaConfigurationClass owner) {
                super("getF31",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF31();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF31();
              }
            }

            public final class MetaGetF32Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<java.lang.String>> {
              private MetaGetF32Method(MetaTestingMetaConfigurationClass owner) {
                super("getF32",metaType(java.util.List.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF32();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF32();
              }
            }

            public final class MetaGetF33Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Set<java.lang.String>> {
              private MetaGetF33Method(MetaTestingMetaConfigurationClass owner) {
                super("getF33",metaType(java.util.Set.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF33();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF33();
              }
            }

            public final class MetaGetF34Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Collection<java.lang.String>> {
              private MetaGetF34Method(MetaTestingMetaConfigurationClass owner) {
                super("getF34",metaType(java.util.Collection.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF34();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF34();
              }
            }

            public final class MetaGetF35Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, io.art.core.collection.ImmutableArray<java.lang.String>> {
              private MetaGetF35Method(MetaTestingMetaConfigurationClass owner) {
                super("getF35",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF35();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF35();
              }
            }

            public final class MetaGetF36Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, io.art.core.collection.ImmutableSet<java.lang.String>> {
              private MetaGetF36Method(MetaTestingMetaConfigurationClass owner) {
                super("getF36",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF36();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF36();
              }
            }

            public final class MetaGetF37Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.function.Supplier<java.lang.String>> {
              private MetaGetF37Method(MetaTestingMetaConfigurationClass owner) {
                super("getF37",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF37();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF37();
              }
            }

            public final class MetaGetF38Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, io.art.core.property.LazyProperty<java.lang.String>> {
              private MetaGetF38Method(MetaTestingMetaConfigurationClass owner) {
                super("getF38",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF38();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF38();
              }
            }

            public final class MetaGetF39Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<int[]>> {
              private MetaGetF39Method(MetaTestingMetaConfigurationClass owner) {
                super("getF39",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF39();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF39();
              }
            }

            public final class MetaGetF40Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<java.lang.String[]>> {
              private MetaGetF40Method(MetaTestingMetaConfigurationClass owner) {
                super("getF40",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF40();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF40();
              }
            }

            public final class MetaGetF41Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<int[]>[]> {
              private MetaGetF41Method(MetaTestingMetaConfigurationClass owner) {
                super("getF41",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF41();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF41();
              }
            }

            public final class MetaGetF42Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<java.lang.String[]>[]> {
              private MetaGetF42Method(MetaTestingMetaConfigurationClass owner) {
                super("getF42",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF42();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF42();
              }
            }

            public final class MetaGetF43Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<java.util.List<java.lang.String>>> {
              private MetaGetF43Method(MetaTestingMetaConfigurationClass owner) {
                super("getF43",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF43();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF43();
              }
            }

            public final class MetaGetF44Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<java.util.List<java.lang.String>[]>> {
              private MetaGetF44Method(MetaTestingMetaConfigurationClass owner) {
                super("getF44",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF44();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF44();
              }
            }

            public final class MetaGetF45Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Map<java.lang.String, java.lang.String[]>> {
              private MetaGetF45Method(MetaTestingMetaConfigurationClass owner) {
                super("getF45",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF45();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF45();
              }
            }

            public final class MetaGetF46Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> {
              private MetaGetF46Method(MetaTestingMetaConfigurationClass owner) {
                super("getF46",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF46();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF46();
              }
            }

            public final class MetaGetF47Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> {
              private MetaGetF47Method(MetaTestingMetaConfigurationClass owner) {
                super("getF47",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF47();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF47();
              }
            }

            public final class MetaGetF48Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> {
              private MetaGetF48Method(MetaTestingMetaConfigurationClass owner) {
                super("getF48",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF48();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF48();
              }
            }

            public final class MetaGetF49Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, io.art.meta.test.TestingMetaConfiguration> {
              private MetaGetF49Method(MetaTestingMetaConfigurationClass owner) {
                super("getF49",metaType(io.art.meta.test.TestingMetaConfiguration.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF49();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF49();
              }
            }

            public final class MetaGetF50Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.time.LocalDateTime> {
              private MetaGetF50Method(MetaTestingMetaConfigurationClass owner) {
                super("getF50",metaType(java.time.LocalDateTime.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF50();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF50();
              }
            }

            public final class MetaGetF51Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.time.ZonedDateTime> {
              private MetaGetF51Method(MetaTestingMetaConfigurationClass owner) {
                super("getF51",metaType(java.time.ZonedDateTime.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF51();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF51();
              }
            }

            public final class MetaGetF52Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.time.Duration> {
              private MetaGetF52Method(MetaTestingMetaConfigurationClass owner) {
                super("getF52",metaType(java.time.Duration.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF52();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF52();
              }
            }

            public final class MetaGetF53Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, io.art.meta.test.TestingMetaConfiguration.ModelEnum> {
              private MetaGetF53Method(MetaTestingMetaConfigurationClass owner) {
                super("getF53",metaEnum(io.art.meta.test.TestingMetaConfiguration.ModelEnum.class, io.art.meta.test.TestingMetaConfiguration.ModelEnum::valueOf),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF53();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF53();
              }
            }

            public final class MetaGetF54Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> {
              private MetaGetF54Method(MetaTestingMetaConfigurationClass owner) {
                super("getF54",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF54();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF54();
              }
            }

            public final class MetaGetF55Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.List<io.art.meta.test.TestingMetaConfiguration>> {
              private MetaGetF55Method(MetaTestingMetaConfigurationClass owner) {
                super("getF55",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaConfiguration.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF55();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF55();
              }
            }

            public final class MetaGetF56Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Set<io.art.meta.test.TestingMetaConfiguration>> {
              private MetaGetF56Method(MetaTestingMetaConfigurationClass owner) {
                super("getF56",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaConfiguration.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF56();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF56();
              }
            }

            public final class MetaGetF57Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> {
              private MetaGetF57Method(MetaTestingMetaConfigurationClass owner) {
                super("getF57",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaConfiguration.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF57();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF57();
              }
            }

            public final class MetaGetF58Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, reactor.core.publisher.Mono<java.lang.String>> {
              private MetaGetF58Method(MetaTestingMetaConfigurationClass owner) {
                super("getF58",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF58();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF58();
              }
            }

            public final class MetaGetF59Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, reactor.core.publisher.Flux<java.lang.String>> {
              private MetaGetF59Method(MetaTestingMetaConfigurationClass owner) {
                super("getF59",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF59();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF59();
              }
            }

            public final class MetaGetF60Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.util.stream.Stream<java.lang.String>> {
              private MetaGetF60Method(MetaTestingMetaConfigurationClass owner) {
                super("getF60",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF60();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF60();
              }
            }

            public final class MetaGetF61Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, reactor.core.publisher.Mono<java.lang.String[]>> {
              private MetaGetF61Method(MetaTestingMetaConfigurationClass owner) {
                super("getF61",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF61();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF61();
              }
            }

            public final class MetaGetF62Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.String> {
              private MetaGetF62Method(MetaTestingMetaConfigurationClass owner) {
                super("getF62",metaType(java.lang.String.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF62();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF62();
              }
            }

            public final class MetaGetF63Method extends InstanceMetaMethod<MetaTestingMetaConfigurationClass, io.art.meta.test.TestingMetaConfiguration, java.lang.String> {
              private MetaGetF63Method(MetaTestingMetaConfigurationClass owner) {
                super("getF63",metaType(java.lang.String.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance,
                  Object[] arguments) throws Throwable {
                return instance.getF63();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaConfiguration instance) throws
                  Throwable {
                return instance.getF63();
              }
            }

            public static final class MetaTestingMetaConfigurationBuilderClass extends MetaClass<io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
              private static final io.art.core.property.LazyProperty<MetaTestingMetaConfigurationBuilderClass> self = MetaClass.self(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class);

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Integer> f1Field = registerField(new MetaField<>("f1",metaType(int.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Short> f2Field = registerField(new MetaField<>("f2",metaType(short.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Double> f3Field = registerField(new MetaField<>("f3",metaType(double.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Float> f4Field = registerField(new MetaField<>("f4",metaType(float.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Long> f5Field = registerField(new MetaField<>("f5",metaType(long.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Boolean> f6Field = registerField(new MetaField<>("f6",metaType(boolean.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Character> f7Field = registerField(new MetaField<>("f7",metaType(char.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, Byte> f8Field = registerField(new MetaField<>("f8",metaType(byte.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Integer> f9Field = registerField(new MetaField<>("f9",metaType(java.lang.Integer.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Short> f10Field = registerField(new MetaField<>("f10",metaType(java.lang.Short.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Double> f11Field = registerField(new MetaField<>("f11",metaType(java.lang.Double.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Float> f12Field = registerField(new MetaField<>("f12",metaType(java.lang.Float.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Long> f13Field = registerField(new MetaField<>("f13",metaType(java.lang.Long.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Boolean> f14Field = registerField(new MetaField<>("f14",metaType(java.lang.Boolean.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Character> f15Field = registerField(new MetaField<>("f15",metaType(java.lang.Character.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String> f16Field = registerField(new MetaField<>("f16",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, int[]> f17Field = registerField(new MetaField<>("f17",metaArray(int[].class, int[]::new, metaType(int.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, short[]> f18Field = registerField(new MetaField<>("f18",metaArray(short[].class, short[]::new, metaType(short.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, double[]> f19Field = registerField(new MetaField<>("f19",metaArray(double[].class, double[]::new, metaType(double.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, float[]> f20Field = registerField(new MetaField<>("f20",metaArray(float[].class, float[]::new, metaType(float.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, long[]> f21Field = registerField(new MetaField<>("f21",metaArray(long[].class, long[]::new, metaType(long.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, boolean[]> f22Field = registerField(new MetaField<>("f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, char[]> f23Field = registerField(new MetaField<>("f23",metaArray(char[].class, char[]::new, metaType(char.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Integer[]> f24Field = registerField(new MetaField<>("f24",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Short[]> f25Field = registerField(new MetaField<>("f25",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Double[]> f26Field = registerField(new MetaField<>("f26",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Float[]> f27Field = registerField(new MetaField<>("f27",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Long[]> f28Field = registerField(new MetaField<>("f28",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Boolean[]> f29Field = registerField(new MetaField<>("f29",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Character[]> f30Field = registerField(new MetaField<>("f30",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String[]> f31Field = registerField(new MetaField<>("f31",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.lang.String>> f32Field = registerField(new MetaField<>("f32",metaType(java.util.List.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Set<java.lang.String>> f33Field = registerField(new MetaField<>("f33",metaType(java.util.Set.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Collection<java.lang.String>> f34Field = registerField(new MetaField<>("f34",metaType(java.util.Collection.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.collection.ImmutableArray<java.lang.String>> f35Field = registerField(new MetaField<>("f35",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.collection.ImmutableSet<java.lang.String>> f36Field = registerField(new MetaField<>("f36",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.function.Supplier<java.lang.String>> f37Field = registerField(new MetaField<>("f37",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.property.LazyProperty<java.lang.String>> f38Field = registerField(new MetaField<>("f38",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<int[]>> f39Field = registerField(new MetaField<>("f39",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.lang.String[]>> f40Field = registerField(new MetaField<>("f40",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<int[]>[]> f41Field = registerField(new MetaField<>("f41",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.lang.String[]>[]> f42Field = registerField(new MetaField<>("f42",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.util.List<java.lang.String>>> f43Field = registerField(new MetaField<>("f43",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.util.List<java.lang.String>[]>> f44Field = registerField(new MetaField<>("f44",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, java.lang.String[]>> f45Field = registerField(new MetaField<>("f45",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Field = registerField(new MetaField<>("f46",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Field = registerField(new MetaField<>("f47",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Field = registerField(new MetaField<>("f48",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration> f49Field = registerField(new MetaField<>("f49",metaType(io.art.meta.test.TestingMetaConfiguration.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.time.LocalDateTime> f50Field = registerField(new MetaField<>("f50",metaType(java.time.LocalDateTime.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.time.ZonedDateTime> f51Field = registerField(new MetaField<>("f51",metaType(java.time.ZonedDateTime.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.time.Duration> f52Field = registerField(new MetaField<>("f52",metaType(java.time.Duration.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Field = registerField(new MetaField<>("f53",metaEnum(io.art.meta.test.TestingMetaConfiguration.ModelEnum.class, io.art.meta.test.TestingMetaConfiguration.ModelEnum::valueOf),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Field = registerField(new MetaField<>("f54",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Field = registerField(new MetaField<>("f55",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaConfiguration.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Field = registerField(new MetaField<>("f56",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaConfiguration.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Field = registerField(new MetaField<>("f57",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaConfiguration.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, reactor.core.publisher.Mono<java.lang.String>> f58Field = registerField(new MetaField<>("f58",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, reactor.core.publisher.Flux<java.lang.String>> f59Field = registerField(new MetaField<>("f59",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.stream.Stream<java.lang.String>> f60Field = registerField(new MetaField<>("f60",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, reactor.core.publisher.Mono<java.lang.String[]>> f61Field = registerField(new MetaField<>("f61",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String> f62Field = registerField(new MetaField<>("f62",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String> f63Field = registerField(new MetaField<>("f63",metaType(java.lang.String.class),false,this));

              private final MetaF1Method f1Method = registerMethod(new MetaF1Method(this));

              private final MetaF2Method f2Method = registerMethod(new MetaF2Method(this));

              private final MetaF3Method f3Method = registerMethod(new MetaF3Method(this));

              private final MetaF4Method f4Method = registerMethod(new MetaF4Method(this));

              private final MetaF5Method f5Method = registerMethod(new MetaF5Method(this));

              private final MetaF6Method f6Method = registerMethod(new MetaF6Method(this));

              private final MetaF7Method f7Method = registerMethod(new MetaF7Method(this));

              private final MetaF8Method f8Method = registerMethod(new MetaF8Method(this));

              private final MetaF9Method f9Method = registerMethod(new MetaF9Method(this));

              private final MetaF10Method f10Method = registerMethod(new MetaF10Method(this));

              private final MetaF11Method f11Method = registerMethod(new MetaF11Method(this));

              private final MetaF12Method f12Method = registerMethod(new MetaF12Method(this));

              private final MetaF13Method f13Method = registerMethod(new MetaF13Method(this));

              private final MetaF14Method f14Method = registerMethod(new MetaF14Method(this));

              private final MetaF15Method f15Method = registerMethod(new MetaF15Method(this));

              private final MetaF16Method f16Method = registerMethod(new MetaF16Method(this));

              private final MetaF17Method f17Method = registerMethod(new MetaF17Method(this));

              private final MetaF18Method f18Method = registerMethod(new MetaF18Method(this));

              private final MetaF19Method f19Method = registerMethod(new MetaF19Method(this));

              private final MetaF20Method f20Method = registerMethod(new MetaF20Method(this));

              private final MetaF21Method f21Method = registerMethod(new MetaF21Method(this));

              private final MetaF22Method f22Method = registerMethod(new MetaF22Method(this));

              private final MetaF23Method f23Method = registerMethod(new MetaF23Method(this));

              private final MetaF24Method f24Method = registerMethod(new MetaF24Method(this));

              private final MetaF25Method f25Method = registerMethod(new MetaF25Method(this));

              private final MetaF26Method f26Method = registerMethod(new MetaF26Method(this));

              private final MetaF27Method f27Method = registerMethod(new MetaF27Method(this));

              private final MetaF28Method f28Method = registerMethod(new MetaF28Method(this));

              private final MetaF29Method f29Method = registerMethod(new MetaF29Method(this));

              private final MetaF30Method f30Method = registerMethod(new MetaF30Method(this));

              private final MetaF31Method f31Method = registerMethod(new MetaF31Method(this));

              private final MetaF32Method f32Method = registerMethod(new MetaF32Method(this));

              private final MetaF33Method f33Method = registerMethod(new MetaF33Method(this));

              private final MetaF34Method f34Method = registerMethod(new MetaF34Method(this));

              private final MetaF35Method f35Method = registerMethod(new MetaF35Method(this));

              private final MetaF36Method f36Method = registerMethod(new MetaF36Method(this));

              private final MetaF37Method f37Method = registerMethod(new MetaF37Method(this));

              private final MetaF38Method f38Method = registerMethod(new MetaF38Method(this));

              private final MetaF39Method f39Method = registerMethod(new MetaF39Method(this));

              private final MetaF40Method f40Method = registerMethod(new MetaF40Method(this));

              private final MetaF41Method f41Method = registerMethod(new MetaF41Method(this));

              private final MetaF42Method f42Method = registerMethod(new MetaF42Method(this));

              private final MetaF43Method f43Method = registerMethod(new MetaF43Method(this));

              private final MetaF44Method f44Method = registerMethod(new MetaF44Method(this));

              private final MetaF45Method f45Method = registerMethod(new MetaF45Method(this));

              private final MetaF46Method f46Method = registerMethod(new MetaF46Method(this));

              private final MetaF47Method f47Method = registerMethod(new MetaF47Method(this));

              private final MetaF48Method f48Method = registerMethod(new MetaF48Method(this));

              private final MetaF49Method f49Method = registerMethod(new MetaF49Method(this));

              private final MetaF50Method f50Method = registerMethod(new MetaF50Method(this));

              private final MetaF51Method f51Method = registerMethod(new MetaF51Method(this));

              private final MetaF52Method f52Method = registerMethod(new MetaF52Method(this));

              private final MetaF53Method f53Method = registerMethod(new MetaF53Method(this));

              private final MetaF54Method f54Method = registerMethod(new MetaF54Method(this));

              private final MetaF55Method f55Method = registerMethod(new MetaF55Method(this));

              private final MetaF56Method f56Method = registerMethod(new MetaF56Method(this));

              private final MetaF57Method f57Method = registerMethod(new MetaF57Method(this));

              private final MetaF58Method f58Method = registerMethod(new MetaF58Method(this));

              private final MetaF59Method f59Method = registerMethod(new MetaF59Method(this));

              private final MetaF60Method f60Method = registerMethod(new MetaF60Method(this));

              private final MetaF61Method f61Method = registerMethod(new MetaF61Method(this));

              private final MetaF62Method f62Method = registerMethod(new MetaF62Method(this));

              private final MetaF63Method f63Method = registerMethod(new MetaF63Method(this));

              private final MetaBuildMethod buildMethod = registerMethod(new MetaBuildMethod(this));

              private MetaTestingMetaConfigurationBuilderClass() {
                super(metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class));
              }

              public static MetaTestingMetaConfigurationBuilderClass testingMetaConfigurationBuilder(
                  ) {
                return self.get();
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Integer> f1Field(
                  ) {
                return f1Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Short> f2Field(
                  ) {
                return f2Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Double> f3Field(
                  ) {
                return f3Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Float> f4Field(
                  ) {
                return f4Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Long> f5Field() {
                return f5Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Boolean> f6Field(
                  ) {
                return f6Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Character> f7Field(
                  ) {
                return f7Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, Byte> f8Field() {
                return f8Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Integer> f9Field(
                  ) {
                return f9Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Short> f10Field(
                  ) {
                return f10Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Double> f11Field(
                  ) {
                return f11Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Float> f12Field(
                  ) {
                return f12Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Long> f13Field(
                  ) {
                return f13Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Boolean> f14Field(
                  ) {
                return f14Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Character> f15Field(
                  ) {
                return f15Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String> f16Field(
                  ) {
                return f16Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, int[]> f17Field() {
                return f17Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, short[]> f18Field() {
                return f18Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, double[]> f19Field() {
                return f19Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, float[]> f20Field() {
                return f20Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, long[]> f21Field() {
                return f21Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, boolean[]> f22Field() {
                return f22Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, char[]> f23Field() {
                return f23Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Integer[]> f24Field(
                  ) {
                return f24Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Short[]> f25Field(
                  ) {
                return f25Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Double[]> f26Field(
                  ) {
                return f26Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Float[]> f27Field(
                  ) {
                return f27Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Long[]> f28Field(
                  ) {
                return f28Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Boolean[]> f29Field(
                  ) {
                return f29Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.Character[]> f30Field(
                  ) {
                return f30Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String[]> f31Field(
                  ) {
                return f31Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.lang.String>> f32Field(
                  ) {
                return f32Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Set<java.lang.String>> f33Field(
                  ) {
                return f33Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Collection<java.lang.String>> f34Field(
                  ) {
                return f34Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.collection.ImmutableArray<java.lang.String>> f35Field(
                  ) {
                return f35Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.collection.ImmutableSet<java.lang.String>> f36Field(
                  ) {
                return f36Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.function.Supplier<java.lang.String>> f37Field(
                  ) {
                return f37Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.property.LazyProperty<java.lang.String>> f38Field(
                  ) {
                return f38Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<int[]>> f39Field(
                  ) {
                return f39Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.lang.String[]>> f40Field(
                  ) {
                return f40Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<int[]>[]> f41Field(
                  ) {
                return f41Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.lang.String[]>[]> f42Field(
                  ) {
                return f42Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.util.List<java.lang.String>>> f43Field(
                  ) {
                return f43Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<java.util.List<java.lang.String>[]>> f44Field(
                  ) {
                return f44Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, java.lang.String[]>> f45Field(
                  ) {
                return f45Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Field(
                  ) {
                return f46Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Field(
                  ) {
                return f47Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Field(
                  ) {
                return f48Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration> f49Field(
                  ) {
                return f49Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.time.LocalDateTime> f50Field(
                  ) {
                return f50Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.time.ZonedDateTime> f51Field(
                  ) {
                return f51Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.time.Duration> f52Field(
                  ) {
                return f52Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Field(
                  ) {
                return f53Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Field(
                  ) {
                return f54Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Field(
                  ) {
                return f55Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Field(
                  ) {
                return f56Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Field(
                  ) {
                return f57Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, reactor.core.publisher.Mono<java.lang.String>> f58Field(
                  ) {
                return f58Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, reactor.core.publisher.Flux<java.lang.String>> f59Field(
                  ) {
                return f59Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.util.stream.Stream<java.lang.String>> f60Field(
                  ) {
                return f60Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, reactor.core.publisher.Mono<java.lang.String[]>> f61Field(
                  ) {
                return f61Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String> f62Field(
                  ) {
                return f62Field;
              }

              public MetaField<MetaTestingMetaConfigurationBuilderClass, java.lang.String> f63Field(
                  ) {
                return f63Field;
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

              public MetaF62Method f62Method() {
                return f62Method;
              }

              public MetaF63Method f63Method() {
                return f63Method;
              }

              public MetaBuildMethod buildMethod() {
                return buildMethod;
              }

              public final class MetaF1Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Integer> f1Parameter = register(new MetaParameter<>(0, "f1",metaType(int.class)));

                private MetaF1Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f1",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f1((int)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f1((int)(argument));
                }

                public MetaParameter<java.lang.Integer> f1Parameter() {
                  return f1Parameter;
                }
              }

              public final class MetaF2Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Short> f2Parameter = register(new MetaParameter<>(0, "f2",metaType(short.class)));

                private MetaF2Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f2",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f2((short)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f2((short)(argument));
                }

                public MetaParameter<java.lang.Short> f2Parameter() {
                  return f2Parameter;
                }
              }

              public final class MetaF3Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Double> f3Parameter = register(new MetaParameter<>(0, "f3",metaType(double.class)));

                private MetaF3Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f3",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f3((double)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f3((double)(argument));
                }

                public MetaParameter<java.lang.Double> f3Parameter() {
                  return f3Parameter;
                }
              }

              public final class MetaF4Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Float> f4Parameter = register(new MetaParameter<>(0, "f4",metaType(float.class)));

                private MetaF4Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f4",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f4((float)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f4((float)(argument));
                }

                public MetaParameter<java.lang.Float> f4Parameter() {
                  return f4Parameter;
                }
              }

              public final class MetaF5Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Long> f5Parameter = register(new MetaParameter<>(0, "f5",metaType(long.class)));

                private MetaF5Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f5",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f5((long)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f5((long)(argument));
                }

                public MetaParameter<java.lang.Long> f5Parameter() {
                  return f5Parameter;
                }
              }

              public final class MetaF6Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Boolean> f6Parameter = register(new MetaParameter<>(0, "f6",metaType(boolean.class)));

                private MetaF6Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f6",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f6((boolean)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f6((boolean)(argument));
                }

                public MetaParameter<java.lang.Boolean> f6Parameter() {
                  return f6Parameter;
                }
              }

              public final class MetaF7Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Character> f7Parameter = register(new MetaParameter<>(0, "f7",metaType(char.class)));

                private MetaF7Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f7",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f7((char)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f7((char)(argument));
                }

                public MetaParameter<java.lang.Character> f7Parameter() {
                  return f7Parameter;
                }
              }

              public final class MetaF8Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<Byte> f8Parameter = register(new MetaParameter<>(0, "f8",metaType(byte.class)));

                private MetaF8Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f8",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f8((byte)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f8((byte)(argument));
                }

                public MetaParameter<Byte> f8Parameter() {
                  return f8Parameter;
                }
              }

              public final class MetaF9Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Integer> f9Parameter = register(new MetaParameter<>(0, "f9",metaType(java.lang.Integer.class)));

                private MetaF9Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f9",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f9((java.lang.Integer)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f9((java.lang.Integer)(argument));
                }

                public MetaParameter<java.lang.Integer> f9Parameter() {
                  return f9Parameter;
                }
              }

              public final class MetaF10Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Short> f10Parameter = register(new MetaParameter<>(0, "f10",metaType(java.lang.Short.class)));

                private MetaF10Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f10",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f10((java.lang.Short)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f10((java.lang.Short)(argument));
                }

                public MetaParameter<java.lang.Short> f10Parameter() {
                  return f10Parameter;
                }
              }

              public final class MetaF11Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Double> f11Parameter = register(new MetaParameter<>(0, "f11",metaType(java.lang.Double.class)));

                private MetaF11Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f11",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f11((java.lang.Double)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f11((java.lang.Double)(argument));
                }

                public MetaParameter<java.lang.Double> f11Parameter() {
                  return f11Parameter;
                }
              }

              public final class MetaF12Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Float> f12Parameter = register(new MetaParameter<>(0, "f12",metaType(java.lang.Float.class)));

                private MetaF12Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f12",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f12((java.lang.Float)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f12((java.lang.Float)(argument));
                }

                public MetaParameter<java.lang.Float> f12Parameter() {
                  return f12Parameter;
                }
              }

              public final class MetaF13Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Long> f13Parameter = register(new MetaParameter<>(0, "f13",metaType(java.lang.Long.class)));

                private MetaF13Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f13",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f13((java.lang.Long)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f13((java.lang.Long)(argument));
                }

                public MetaParameter<java.lang.Long> f13Parameter() {
                  return f13Parameter;
                }
              }

              public final class MetaF14Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Boolean> f14Parameter = register(new MetaParameter<>(0, "f14",metaType(java.lang.Boolean.class)));

                private MetaF14Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f14",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f14((java.lang.Boolean)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f14((java.lang.Boolean)(argument));
                }

                public MetaParameter<java.lang.Boolean> f14Parameter() {
                  return f14Parameter;
                }
              }

              public final class MetaF15Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Character> f15Parameter = register(new MetaParameter<>(0, "f15",metaType(java.lang.Character.class)));

                private MetaF15Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f15",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f15((java.lang.Character)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f15((java.lang.Character)(argument));
                }

                public MetaParameter<java.lang.Character> f15Parameter() {
                  return f15Parameter;
                }
              }

              public final class MetaF16Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.String> f16Parameter = register(new MetaParameter<>(0, "f16",metaType(java.lang.String.class)));

                private MetaF16Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f16",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f16((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f16((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> f16Parameter() {
                  return f16Parameter;
                }
              }

              public final class MetaF17Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<int[]> f17Parameter = register(new MetaParameter<>(0, "f17",metaArray(int[].class, int[]::new, metaType(int.class))));

                private MetaF17Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f17",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f17((int[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f17((int[])(argument));
                }

                public MetaParameter<int[]> f17Parameter() {
                  return f17Parameter;
                }
              }

              public final class MetaF18Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<short[]> f18Parameter = register(new MetaParameter<>(0, "f18",metaArray(short[].class, short[]::new, metaType(short.class))));

                private MetaF18Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f18",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f18((short[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f18((short[])(argument));
                }

                public MetaParameter<short[]> f18Parameter() {
                  return f18Parameter;
                }
              }

              public final class MetaF19Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<double[]> f19Parameter = register(new MetaParameter<>(0, "f19",metaArray(double[].class, double[]::new, metaType(double.class))));

                private MetaF19Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f19",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f19((double[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f19((double[])(argument));
                }

                public MetaParameter<double[]> f19Parameter() {
                  return f19Parameter;
                }
              }

              public final class MetaF20Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<float[]> f20Parameter = register(new MetaParameter<>(0, "f20",metaArray(float[].class, float[]::new, metaType(float.class))));

                private MetaF20Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f20",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f20((float[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f20((float[])(argument));
                }

                public MetaParameter<float[]> f20Parameter() {
                  return f20Parameter;
                }
              }

              public final class MetaF21Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<long[]> f21Parameter = register(new MetaParameter<>(0, "f21",metaArray(long[].class, long[]::new, metaType(long.class))));

                private MetaF21Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f21",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f21((long[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f21((long[])(argument));
                }

                public MetaParameter<long[]> f21Parameter() {
                  return f21Parameter;
                }
              }

              public final class MetaF22Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<boolean[]> f22Parameter = register(new MetaParameter<>(0, "f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class))));

                private MetaF22Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f22",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f22((boolean[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f22((boolean[])(argument));
                }

                public MetaParameter<boolean[]> f22Parameter() {
                  return f22Parameter;
                }
              }

              public final class MetaF23Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<char[]> f23Parameter = register(new MetaParameter<>(0, "f23",metaArray(char[].class, char[]::new, metaType(char.class))));

                private MetaF23Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f23",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f23((char[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f23((char[])(argument));
                }

                public MetaParameter<char[]> f23Parameter() {
                  return f23Parameter;
                }
              }

              public final class MetaF24Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Integer[]> f24Parameter = register(new MetaParameter<>(0, "f24",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class))));

                private MetaF24Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f24",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f24((java.lang.Integer[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f24((java.lang.Integer[])(argument));
                }

                public MetaParameter<java.lang.Integer[]> f24Parameter() {
                  return f24Parameter;
                }
              }

              public final class MetaF25Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Short[]> f25Parameter = register(new MetaParameter<>(0, "f25",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class))));

                private MetaF25Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f25",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f25((java.lang.Short[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f25((java.lang.Short[])(argument));
                }

                public MetaParameter<java.lang.Short[]> f25Parameter() {
                  return f25Parameter;
                }
              }

              public final class MetaF26Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Double[]> f26Parameter = register(new MetaParameter<>(0, "f26",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class))));

                private MetaF26Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f26",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f26((java.lang.Double[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f26((java.lang.Double[])(argument));
                }

                public MetaParameter<java.lang.Double[]> f26Parameter() {
                  return f26Parameter;
                }
              }

              public final class MetaF27Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Float[]> f27Parameter = register(new MetaParameter<>(0, "f27",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class))));

                private MetaF27Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f27",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f27((java.lang.Float[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f27((java.lang.Float[])(argument));
                }

                public MetaParameter<java.lang.Float[]> f27Parameter() {
                  return f27Parameter;
                }
              }

              public final class MetaF28Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Long[]> f28Parameter = register(new MetaParameter<>(0, "f28",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class))));

                private MetaF28Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f28",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f28((java.lang.Long[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f28((java.lang.Long[])(argument));
                }

                public MetaParameter<java.lang.Long[]> f28Parameter() {
                  return f28Parameter;
                }
              }

              public final class MetaF29Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Boolean[]> f29Parameter = register(new MetaParameter<>(0, "f29",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class))));

                private MetaF29Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f29",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f29((java.lang.Boolean[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f29((java.lang.Boolean[])(argument));
                }

                public MetaParameter<java.lang.Boolean[]> f29Parameter() {
                  return f29Parameter;
                }
              }

              public final class MetaF30Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.Character[]> f30Parameter = register(new MetaParameter<>(0, "f30",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class))));

                private MetaF30Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f30",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f30((java.lang.Character[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f30((java.lang.Character[])(argument));
                }

                public MetaParameter<java.lang.Character[]> f30Parameter() {
                  return f30Parameter;
                }
              }

              public final class MetaF31Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.String[]> f31Parameter = register(new MetaParameter<>(0, "f31",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));

                private MetaF31Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f31",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f31((java.lang.String[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f31((java.lang.String[])(argument));
                }

                public MetaParameter<java.lang.String[]> f31Parameter() {
                  return f31Parameter;
                }
              }

              public final class MetaF32Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<java.lang.String>> f32Parameter = register(new MetaParameter<>(0, "f32",metaType(java.util.List.class,metaType(java.lang.String.class))));

                private MetaF32Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f32",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f32((java.util.List<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f32((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.lang.String>> f32Parameter() {
                  return f32Parameter;
                }
              }

              public final class MetaF33Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Set<java.lang.String>> f33Parameter = register(new MetaParameter<>(0, "f33",metaType(java.util.Set.class,metaType(java.lang.String.class))));

                private MetaF33Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f33",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f33((java.util.Set<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f33((java.util.Set)(argument));
                }

                public MetaParameter<java.util.Set<java.lang.String>> f33Parameter() {
                  return f33Parameter;
                }
              }

              public final class MetaF34Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Collection<java.lang.String>> f34Parameter = register(new MetaParameter<>(0, "f34",metaType(java.util.Collection.class,metaType(java.lang.String.class))));

                private MetaF34Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f34",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f34((java.util.Collection<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f34((java.util.Collection)(argument));
                }

                public MetaParameter<java.util.Collection<java.lang.String>> f34Parameter() {
                  return f34Parameter;
                }
              }

              public final class MetaF35Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f35Parameter = register(new MetaParameter<>(0, "f35",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class))));

                private MetaF35Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f35",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f35((io.art.core.collection.ImmutableArray<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f35((io.art.core.collection.ImmutableArray)(argument));
                }

                public MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f35Parameter(
                    ) {
                  return f35Parameter;
                }
              }

              public final class MetaF36Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f36Parameter = register(new MetaParameter<>(0, "f36",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class))));

                private MetaF36Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f36",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f36((io.art.core.collection.ImmutableSet<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f36((io.art.core.collection.ImmutableSet)(argument));
                }

                public MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f36Parameter(
                    ) {
                  return f36Parameter;
                }
              }

              public final class MetaF37Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.function.Supplier<java.lang.String>> f37Parameter = register(new MetaParameter<>(0, "f37",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class))));

                private MetaF37Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f37",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f37((java.util.function.Supplier<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f37((java.util.function.Supplier)(argument));
                }

                public MetaParameter<java.util.function.Supplier<java.lang.String>> f37Parameter() {
                  return f37Parameter;
                }
              }

              public final class MetaF38Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f38Parameter = register(new MetaParameter<>(0, "f38",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))));

                private MetaF38Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f38",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f38((io.art.core.property.LazyProperty<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f38((io.art.core.property.LazyProperty)(argument));
                }

                public MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f38Parameter(
                    ) {
                  return f38Parameter;
                }
              }

              public final class MetaF39Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<int[]>> f39Parameter = register(new MetaParameter<>(0, "f39",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))));

                private MetaF39Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f39",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f39((java.util.List<int[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f39((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<int[]>> f39Parameter() {
                  return f39Parameter;
                }
              }

              public final class MetaF40Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<java.lang.String[]>> f40Parameter = register(new MetaParameter<>(0, "f40",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                private MetaF40Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f40",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f40((java.util.List<java.lang.String[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f40((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.lang.String[]>> f40Parameter() {
                  return f40Parameter;
                }
              }

              public final class MetaF41Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<int[]>[]> f41Parameter = register(new MetaParameter<>(0, "f41",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))))));

                private MetaF41Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f41",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f41((java.util.List<int[]>[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f41((java.util.List[])(argument));
                }

                public MetaParameter<java.util.List<int[]>[]> f41Parameter() {
                  return f41Parameter;
                }
              }

              public final class MetaF42Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<java.lang.String[]>[]> f42Parameter = register(new MetaParameter<>(0, "f42",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))))));

                private MetaF42Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f42",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f42((java.util.List<java.lang.String[]>[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f42((java.util.List[])(argument));
                }

                public MetaParameter<java.util.List<java.lang.String[]>[]> f42Parameter() {
                  return f42Parameter;
                }
              }

              public final class MetaF43Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<java.util.List<java.lang.String>>> f43Parameter = register(new MetaParameter<>(0, "f43",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class)))));

                private MetaF43Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f43",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f43((java.util.List<java.util.List<java.lang.String>>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f43((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.util.List<java.lang.String>>> f43Parameter(
                    ) {
                  return f43Parameter;
                }
              }

              public final class MetaF44Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f44Parameter = register(new MetaParameter<>(0, "f44",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class))))));

                private MetaF44Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f44",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f44((java.util.List<java.util.List<java.lang.String>[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f44((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f44Parameter(
                    ) {
                  return f44Parameter;
                }
              }

              public final class MetaF45Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f45Parameter = register(new MetaParameter<>(0, "f45",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                private MetaF45Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f45",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f45((java.util.Map<java.lang.String, java.lang.String[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f45((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f45Parameter(
                    ) {
                  return f45Parameter;
                }
              }

              public final class MetaF46Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Parameter = register(new MetaParameter<>(0, "f46",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class)))));

                private MetaF46Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f46",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f46((java.util.Map<java.lang.String, java.util.List<java.lang.String>>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f46((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f46Parameter(
                    ) {
                  return f46Parameter;
                }
              }

              public final class MetaF47Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Parameter = register(new MetaParameter<>(0, "f47",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                private MetaF47Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f47",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f47((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f47((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f47Parameter(
                    ) {
                  return f47Parameter;
                }
              }

              public final class MetaF48Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Parameter = register(new MetaParameter<>(0, "f48",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                private MetaF48Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f48",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f48((io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f48((io.art.core.collection.ImmutableMap)(argument));
                }

                public MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f48Parameter(
                    ) {
                  return f48Parameter;
                }
              }

              public final class MetaF49Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<io.art.meta.test.TestingMetaConfiguration> f49Parameter = register(new MetaParameter<>(0, "f49",metaType(io.art.meta.test.TestingMetaConfiguration.class)));

                private MetaF49Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f49",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f49((io.art.meta.test.TestingMetaConfiguration)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f49((io.art.meta.test.TestingMetaConfiguration)(argument));
                }

                public MetaParameter<io.art.meta.test.TestingMetaConfiguration> f49Parameter() {
                  return f49Parameter;
                }
              }

              public final class MetaF50Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.time.LocalDateTime> f50Parameter = register(new MetaParameter<>(0, "f50",metaType(java.time.LocalDateTime.class)));

                private MetaF50Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f50",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f50((java.time.LocalDateTime)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f50((java.time.LocalDateTime)(argument));
                }

                public MetaParameter<java.time.LocalDateTime> f50Parameter() {
                  return f50Parameter;
                }
              }

              public final class MetaF51Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.time.ZonedDateTime> f51Parameter = register(new MetaParameter<>(0, "f51",metaType(java.time.ZonedDateTime.class)));

                private MetaF51Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f51",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f51((java.time.ZonedDateTime)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f51((java.time.ZonedDateTime)(argument));
                }

                public MetaParameter<java.time.ZonedDateTime> f51Parameter() {
                  return f51Parameter;
                }
              }

              public final class MetaF52Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.time.Duration> f52Parameter = register(new MetaParameter<>(0, "f52",metaType(java.time.Duration.class)));

                private MetaF52Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f52",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f52((java.time.Duration)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f52((java.time.Duration)(argument));
                }

                public MetaParameter<java.time.Duration> f52Parameter() {
                  return f52Parameter;
                }
              }

              public final class MetaF53Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Parameter = register(new MetaParameter<>(0, "f53",metaEnum(io.art.meta.test.TestingMetaConfiguration.ModelEnum.class, io.art.meta.test.TestingMetaConfiguration.ModelEnum::valueOf)));

                private MetaF53Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f53",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f53((io.art.meta.test.TestingMetaConfiguration.ModelEnum)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f53((io.art.meta.test.TestingMetaConfiguration.ModelEnum)(argument));
                }

                public MetaParameter<io.art.meta.test.TestingMetaConfiguration.ModelEnum> f53Parameter(
                    ) {
                  return f53Parameter;
                }
              }

              public final class MetaF54Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Parameter = register(new MetaParameter<>(0, "f54",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))))));

                private MetaF54Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f54",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f54((java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f54((java.util.Optional)(argument));
                }

                public MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f54Parameter(
                    ) {
                  return f54Parameter;
                }
              }

              public final class MetaF55Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Parameter = register(new MetaParameter<>(0, "f55",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaConfiguration.class))));

                private MetaF55Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f55",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f55((java.util.List<io.art.meta.test.TestingMetaConfiguration>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f55((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<io.art.meta.test.TestingMetaConfiguration>> f55Parameter(
                    ) {
                  return f55Parameter;
                }
              }

              public final class MetaF56Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Parameter = register(new MetaParameter<>(0, "f56",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaConfiguration.class))));

                private MetaF56Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f56",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f56((java.util.Set<io.art.meta.test.TestingMetaConfiguration>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f56((java.util.Set)(argument));
                }

                public MetaParameter<java.util.Set<io.art.meta.test.TestingMetaConfiguration>> f56Parameter(
                    ) {
                  return f56Parameter;
                }
              }

              public final class MetaF57Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Parameter = register(new MetaParameter<>(0, "f57",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaConfiguration.class))));

                private MetaF57Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f57",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f57((java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f57((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaConfiguration>> f57Parameter(
                    ) {
                  return f57Parameter;
                }
              }

              public final class MetaF58Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f58Parameter = register(new MetaParameter<>(0, "f58",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaF58Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f58",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f58((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f58((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f58Parameter() {
                  return f58Parameter;
                }
              }

              public final class MetaF59Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f59Parameter = register(new MetaParameter<>(0, "f59",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaF59Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f59",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f59((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f59((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f59Parameter() {
                  return f59Parameter;
                }
              }

              public final class MetaF60Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.util.stream.Stream<java.lang.String>> f60Parameter = register(new MetaParameter<>(0, "f60",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class))));

                private MetaF60Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f60",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f60((java.util.stream.Stream<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f60((java.util.stream.Stream)(argument));
                }

                public MetaParameter<java.util.stream.Stream<java.lang.String>> f60Parameter() {
                  return f60Parameter;
                }
              }

              public final class MetaF61Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f61Parameter = register(new MetaParameter<>(0, "f61",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                private MetaF61Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f61",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f61((reactor.core.publisher.Mono<java.lang.String[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f61((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f61Parameter(
                    ) {
                  return f61Parameter;
                }
              }

              public final class MetaF62Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.String> f62Parameter = register(new MetaParameter<>(0, "f62",metaType(java.lang.String.class)));

                private MetaF62Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f62",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f62((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f62((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> f62Parameter() {
                  return f62Parameter;
                }
              }

              public final class MetaF63Method extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder> {
                private final MetaParameter<java.lang.String> f63Parameter = register(new MetaParameter<>(0, "f63",metaType(java.lang.String.class)));

                private MetaF63Method(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("f63",metaType(io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f63((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f63((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> f63Parameter() {
                  return f63Parameter;
                }
              }

              public final class MetaBuildMethod extends InstanceMetaMethod<MetaTestingMetaConfigurationBuilderClass, io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder, io.art.meta.test.TestingMetaConfiguration> {
                private MetaBuildMethod(MetaTestingMetaConfigurationBuilderClass owner) {
                  super("build",metaType(io.art.meta.test.TestingMetaConfiguration.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.build();
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaConfiguration.TestingMetaConfigurationBuilder instance)
                    throws Throwable {
                  return instance.build();
                }
              }
            }
          }

          public static final class MetaTestingShortMetaModelClass extends MetaClass<io.art.meta.test.TestingShortMetaModel> {
            private static final io.art.core.property.LazyProperty<MetaTestingShortMetaModelClass> self = MetaClass.self(io.art.meta.test.TestingShortMetaModel.class);

            private final MetaConstructorConstructor constructor = registerConstructor(new MetaConstructorConstructor(this));

            private final MetaField<MetaTestingShortMetaModelClass, java.lang.Integer> idField = registerField(new MetaField<>("id",metaType(int.class),false,this));

            private final MetaField<MetaTestingShortMetaModelClass, java.lang.String> nameField = registerField(new MetaField<>("name",metaType(java.lang.String.class),false,this));

            private final MetaField<MetaTestingShortMetaModelClass, io.art.meta.test.TestingShortMetaModel.Inner> innerField = registerField(new MetaField<>("inner",metaType(io.art.meta.test.TestingShortMetaModel.Inner.class),false,this));

            private final MetaGetIdMethod getIdMethod = registerMethod(new MetaGetIdMethod(this));

            private final MetaGetNameMethod getNameMethod = registerMethod(new MetaGetNameMethod(this));

            private final MetaGetInnerMethod getInnerMethod = registerMethod(new MetaGetInnerMethod(this));

            private final MetaTestingShortMetaModelBuilderClass testingShortMetaModelBuilderClass = registerClass(new MetaTestingShortMetaModelBuilderClass());

            private final MetaInnerClass innerClass = registerClass(new MetaInnerClass());

            private MetaTestingShortMetaModelClass() {
              super(metaType(io.art.meta.test.TestingShortMetaModel.class));
            }

            public static MetaTestingShortMetaModelClass testingShortMetaModel() {
              return self.get();
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<MetaTestingShortMetaModelClass, java.lang.Integer> idField() {
              return idField;
            }

            public MetaField<MetaTestingShortMetaModelClass, java.lang.String> nameField() {
              return nameField;
            }

            public MetaField<MetaTestingShortMetaModelClass, io.art.meta.test.TestingShortMetaModel.Inner> innerField(
                ) {
              return innerField;
            }

            public MetaGetIdMethod getIdMethod() {
              return getIdMethod;
            }

            public MetaGetNameMethod getNameMethod() {
              return getNameMethod;
            }

            public MetaGetInnerMethod getInnerMethod() {
              return getInnerMethod;
            }

            public MetaTestingShortMetaModelBuilderClass testingShortMetaModelBuilderClass() {
              return testingShortMetaModelBuilderClass;
            }

            public MetaInnerClass innerClass() {
              return innerClass;
            }

            public final class MetaConstructorConstructor extends MetaConstructor<MetaTestingShortMetaModelClass, io.art.meta.test.TestingShortMetaModel> {
              private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

              private final MetaParameter<java.lang.String> nameParameter = register(new MetaParameter<>(1, "name",metaType(java.lang.String.class)));

              private final MetaParameter<io.art.meta.test.TestingShortMetaModel.Inner> innerParameter = register(new MetaParameter<>(2, "inner",metaType(io.art.meta.test.TestingShortMetaModel.Inner.class)));

              private MetaConstructorConstructor(MetaTestingShortMetaModelClass owner) {
                super(metaType(io.art.meta.test.TestingShortMetaModel.class),owner);
              }

              @Override
              public io.art.meta.test.TestingShortMetaModel invoke(Object[] arguments) throws
                  Throwable {
                return new io.art.meta.test.TestingShortMetaModel((int)(arguments[0]),(java.lang.String)(arguments[1]),(io.art.meta.test.TestingShortMetaModel.Inner)(arguments[2]));
              }

              public MetaParameter<java.lang.Integer> idParameter() {
                return idParameter;
              }

              public MetaParameter<java.lang.String> nameParameter() {
                return nameParameter;
              }

              public MetaParameter<io.art.meta.test.TestingShortMetaModel.Inner> innerParameter() {
                return innerParameter;
              }
            }

            public final class MetaGetIdMethod extends InstanceMetaMethod<MetaTestingShortMetaModelClass, io.art.meta.test.TestingShortMetaModel, java.lang.Integer> {
              private MetaGetIdMethod(MetaTestingShortMetaModelClass owner) {
                super("getId",metaType(int.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingShortMetaModel instance,
                  Object[] arguments) throws Throwable {
                return instance.getId();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingShortMetaModel instance) throws
                  Throwable {
                return instance.getId();
              }
            }

            public final class MetaGetNameMethod extends InstanceMetaMethod<MetaTestingShortMetaModelClass, io.art.meta.test.TestingShortMetaModel, java.lang.String> {
              private MetaGetNameMethod(MetaTestingShortMetaModelClass owner) {
                super("getName",metaType(java.lang.String.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingShortMetaModel instance,
                  Object[] arguments) throws Throwable {
                return instance.getName();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingShortMetaModel instance) throws
                  Throwable {
                return instance.getName();
              }
            }

            public final class MetaGetInnerMethod extends InstanceMetaMethod<MetaTestingShortMetaModelClass, io.art.meta.test.TestingShortMetaModel, io.art.meta.test.TestingShortMetaModel.Inner> {
              private MetaGetInnerMethod(MetaTestingShortMetaModelClass owner) {
                super("getInner",metaType(io.art.meta.test.TestingShortMetaModel.Inner.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingShortMetaModel instance,
                  Object[] arguments) throws Throwable {
                return instance.getInner();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingShortMetaModel instance) throws
                  Throwable {
                return instance.getInner();
              }
            }

            public static final class MetaTestingShortMetaModelBuilderClass extends MetaClass<io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder> {
              private static final io.art.core.property.LazyProperty<MetaTestingShortMetaModelBuilderClass> self = MetaClass.self(io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder.class);

              private final MetaField<MetaTestingShortMetaModelBuilderClass, java.lang.Integer> idField = registerField(new MetaField<>("id",metaType(int.class),false,this));

              private final MetaField<MetaTestingShortMetaModelBuilderClass, java.lang.String> nameField = registerField(new MetaField<>("name",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaTestingShortMetaModelBuilderClass, io.art.meta.test.TestingShortMetaModel.Inner> innerField = registerField(new MetaField<>("inner",metaType(io.art.meta.test.TestingShortMetaModel.Inner.class),false,this));

              private final MetaIdMethod idMethod = registerMethod(new MetaIdMethod(this));

              private final MetaNameMethod nameMethod = registerMethod(new MetaNameMethod(this));

              private final MetaInnerMethod innerMethod = registerMethod(new MetaInnerMethod(this));

              private final MetaBuildMethod buildMethod = registerMethod(new MetaBuildMethod(this));

              private MetaTestingShortMetaModelBuilderClass() {
                super(metaType(io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder.class));
              }

              public static MetaTestingShortMetaModelBuilderClass testingShortMetaModelBuilder() {
                return self.get();
              }

              public MetaField<MetaTestingShortMetaModelBuilderClass, java.lang.Integer> idField() {
                return idField;
              }

              public MetaField<MetaTestingShortMetaModelBuilderClass, java.lang.String> nameField(
                  ) {
                return nameField;
              }

              public MetaField<MetaTestingShortMetaModelBuilderClass, io.art.meta.test.TestingShortMetaModel.Inner> innerField(
                  ) {
                return innerField;
              }

              public MetaIdMethod idMethod() {
                return idMethod;
              }

              public MetaNameMethod nameMethod() {
                return nameMethod;
              }

              public MetaInnerMethod innerMethod() {
                return innerMethod;
              }

              public MetaBuildMethod buildMethod() {
                return buildMethod;
              }

              public final class MetaIdMethod extends InstanceMetaMethod<MetaTestingShortMetaModelBuilderClass, io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder, io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder> {
                private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private MetaIdMethod(MetaTestingShortMetaModelBuilderClass owner) {
                  super("id",metaType(io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.id((int)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.id((int)(argument));
                }

                public MetaParameter<java.lang.Integer> idParameter() {
                  return idParameter;
                }
              }

              public final class MetaNameMethod extends InstanceMetaMethod<MetaTestingShortMetaModelBuilderClass, io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder, io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder> {
                private final MetaParameter<java.lang.String> nameParameter = register(new MetaParameter<>(0, "name",metaType(java.lang.String.class)));

                private MetaNameMethod(MetaTestingShortMetaModelBuilderClass owner) {
                  super("name",metaType(io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.name((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.name((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> nameParameter() {
                  return nameParameter;
                }
              }

              public final class MetaInnerMethod extends InstanceMetaMethod<MetaTestingShortMetaModelBuilderClass, io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder, io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder> {
                private final MetaParameter<io.art.meta.test.TestingShortMetaModel.Inner> innerParameter = register(new MetaParameter<>(0, "inner",metaType(io.art.meta.test.TestingShortMetaModel.Inner.class)));

                private MetaInnerMethod(MetaTestingShortMetaModelBuilderClass owner) {
                  super("inner",metaType(io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.inner((io.art.meta.test.TestingShortMetaModel.Inner)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.inner((io.art.meta.test.TestingShortMetaModel.Inner)(argument));
                }

                public MetaParameter<io.art.meta.test.TestingShortMetaModel.Inner> innerParameter(
                    ) {
                  return innerParameter;
                }
              }

              public final class MetaBuildMethod extends InstanceMetaMethod<MetaTestingShortMetaModelBuilderClass, io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder, io.art.meta.test.TestingShortMetaModel> {
                private MetaBuildMethod(MetaTestingShortMetaModelBuilderClass owner) {
                  super("build",metaType(io.art.meta.test.TestingShortMetaModel.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.build();
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingShortMetaModel.TestingShortMetaModelBuilder instance)
                    throws Throwable {
                  return instance.build();
                }
              }
            }

            public static final class MetaInnerClass extends MetaClass<io.art.meta.test.TestingShortMetaModel.Inner> {
              private static final io.art.core.property.LazyProperty<MetaInnerClass> self = MetaClass.self(io.art.meta.test.TestingShortMetaModel.Inner.class);

              private final MetaConstructorConstructor constructor = registerConstructor(new MetaConstructorConstructor(this));

              private final MetaField<MetaInnerClass, java.lang.Integer> idField = registerField(new MetaField<>("id",metaType(int.class),false,this));

              private final MetaField<MetaInnerClass, java.lang.String> nameField = registerField(new MetaField<>("name",metaType(java.lang.String.class),false,this));

              private final MetaGetIdMethod getIdMethod = registerMethod(new MetaGetIdMethod(this));

              private final MetaGetNameMethod getNameMethod = registerMethod(new MetaGetNameMethod(this));

              private final MetaInnerBuilderClass innerBuilderClass = registerClass(new MetaInnerBuilderClass());

              private MetaInnerClass() {
                super(metaType(io.art.meta.test.TestingShortMetaModel.Inner.class));
              }

              public static MetaInnerClass inner() {
                return self.get();
              }

              public MetaConstructorConstructor constructor() {
                return constructor;
              }

              public MetaField<MetaInnerClass, java.lang.Integer> idField() {
                return idField;
              }

              public MetaField<MetaInnerClass, java.lang.String> nameField() {
                return nameField;
              }

              public MetaGetIdMethod getIdMethod() {
                return getIdMethod;
              }

              public MetaGetNameMethod getNameMethod() {
                return getNameMethod;
              }

              public MetaInnerBuilderClass innerBuilderClass() {
                return innerBuilderClass;
              }

              public final class MetaConstructorConstructor extends MetaConstructor<MetaInnerClass, io.art.meta.test.TestingShortMetaModel.Inner> {
                private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                private final MetaParameter<java.lang.String> nameParameter = register(new MetaParameter<>(1, "name",metaType(java.lang.String.class)));

                private MetaConstructorConstructor(MetaInnerClass owner) {
                  super(metaType(io.art.meta.test.TestingShortMetaModel.Inner.class),owner);
                }

                @Override
                public io.art.meta.test.TestingShortMetaModel.Inner invoke(Object[] arguments)
                    throws Throwable {
                  return new io.art.meta.test.TestingShortMetaModel.Inner((int)(arguments[0]),(java.lang.String)(arguments[1]));
                }

                public MetaParameter<java.lang.Integer> idParameter() {
                  return idParameter;
                }

                public MetaParameter<java.lang.String> nameParameter() {
                  return nameParameter;
                }
              }

              public final class MetaGetIdMethod extends InstanceMetaMethod<MetaInnerClass, io.art.meta.test.TestingShortMetaModel.Inner, java.lang.Integer> {
                private MetaGetIdMethod(MetaInnerClass owner) {
                  super("getId",metaType(int.class),owner);
                }

                @Override
                public Object invoke(io.art.meta.test.TestingShortMetaModel.Inner instance,
                    Object[] arguments) throws Throwable {
                  return instance.getId();
                }

                @Override
                public Object invoke(io.art.meta.test.TestingShortMetaModel.Inner instance) throws
                    Throwable {
                  return instance.getId();
                }
              }

              public final class MetaGetNameMethod extends InstanceMetaMethod<MetaInnerClass, io.art.meta.test.TestingShortMetaModel.Inner, java.lang.String> {
                private MetaGetNameMethod(MetaInnerClass owner) {
                  super("getName",metaType(java.lang.String.class),owner);
                }

                @Override
                public Object invoke(io.art.meta.test.TestingShortMetaModel.Inner instance,
                    Object[] arguments) throws Throwable {
                  return instance.getName();
                }

                @Override
                public Object invoke(io.art.meta.test.TestingShortMetaModel.Inner instance) throws
                    Throwable {
                  return instance.getName();
                }
              }

              public static final class MetaInnerBuilderClass extends MetaClass<io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder> {
                private static final io.art.core.property.LazyProperty<MetaInnerBuilderClass> self = MetaClass.self(io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder.class);

                private final MetaField<MetaInnerBuilderClass, java.lang.Integer> idField = registerField(new MetaField<>("id",metaType(int.class),false,this));

                private final MetaField<MetaInnerBuilderClass, java.lang.String> nameField = registerField(new MetaField<>("name",metaType(java.lang.String.class),false,this));

                private final MetaIdMethod idMethod = registerMethod(new MetaIdMethod(this));

                private final MetaNameMethod nameMethod = registerMethod(new MetaNameMethod(this));

                private final MetaBuildMethod buildMethod = registerMethod(new MetaBuildMethod(this));

                private MetaInnerBuilderClass() {
                  super(metaType(io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder.class));
                }

                public static MetaInnerBuilderClass innerBuilder() {
                  return self.get();
                }

                public MetaField<MetaInnerBuilderClass, java.lang.Integer> idField() {
                  return idField;
                }

                public MetaField<MetaInnerBuilderClass, java.lang.String> nameField() {
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

                public final class MetaIdMethod extends InstanceMetaMethod<MetaInnerBuilderClass, io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder, io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder> {
                  private final MetaParameter<java.lang.Integer> idParameter = register(new MetaParameter<>(0, "id",metaType(int.class)));

                  private MetaIdMethod(MetaInnerBuilderClass owner) {
                    super("id",metaType(io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder.class),owner);
                  }

                  @Override
                  public Object invoke(
                      io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.id((int)(arguments[0]));
                  }

                  @Override
                  public Object invoke(
                      io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder instance,
                      Object argument) throws Throwable {
                    return instance.id((int)(argument));
                  }

                  public MetaParameter<java.lang.Integer> idParameter() {
                    return idParameter;
                  }
                }

                public final class MetaNameMethod extends InstanceMetaMethod<MetaInnerBuilderClass, io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder, io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder> {
                  private final MetaParameter<java.lang.String> nameParameter = register(new MetaParameter<>(0, "name",metaType(java.lang.String.class)));

                  private MetaNameMethod(MetaInnerBuilderClass owner) {
                    super("name",metaType(io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder.class),owner);
                  }

                  @Override
                  public Object invoke(
                      io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.name((java.lang.String)(arguments[0]));
                  }

                  @Override
                  public Object invoke(
                      io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder instance,
                      Object argument) throws Throwable {
                    return instance.name((java.lang.String)(argument));
                  }

                  public MetaParameter<java.lang.String> nameParameter() {
                    return nameParameter;
                  }
                }

                public final class MetaBuildMethod extends InstanceMetaMethod<MetaInnerBuilderClass, io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder, io.art.meta.test.TestingShortMetaModel.Inner> {
                  private MetaBuildMethod(MetaInnerBuilderClass owner) {
                    super("build",metaType(io.art.meta.test.TestingShortMetaModel.Inner.class),owner);
                  }

                  @Override
                  public Object invoke(
                      io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder instance,
                      Object[] arguments) throws Throwable {
                    return instance.build();
                  }

                  @Override
                  public Object invoke(
                      io.art.meta.test.TestingShortMetaModel.Inner.InnerBuilder instance) throws
                      Throwable {
                    return instance.build();
                  }
                }
              }
            }
          }

          public static final class MetaTestingMetaModelGeneratorClass extends MetaClass<io.art.meta.test.TestingMetaModelGenerator> {
            private static final io.art.core.property.LazyProperty<MetaTestingMetaModelGeneratorClass> self = MetaClass.self(io.art.meta.test.TestingMetaModelGenerator.class);

            private final MetaGenerateTestingModelMethod generateTestingModelMethod = registerMethod(new MetaGenerateTestingModelMethod(this));

            private MetaTestingMetaModelGeneratorClass() {
              super(metaType(io.art.meta.test.TestingMetaModelGenerator.class));
            }

            public static MetaTestingMetaModelGeneratorClass testingMetaModelGenerator() {
              return self.get();
            }

            public MetaGenerateTestingModelMethod generateTestingModelMethod() {
              return generateTestingModelMethod;
            }

            public final class MetaGenerateTestingModelMethod extends StaticMetaMethod<MetaTestingMetaModelGeneratorClass, io.art.meta.test.TestingMetaModel> {
              private MetaGenerateTestingModelMethod(MetaTestingMetaModelGeneratorClass owner) {
                super("generateTestingModel",metaType(io.art.meta.test.TestingMetaModel.class),owner);
              }

              @Override
              public Object invoke(Object[] arguments) throws Throwable {
                return io.art.meta.test.TestingMetaModelGenerator.generateTestingModel();
              }

              @Override
              public Object invoke() throws Throwable {
                return io.art.meta.test.TestingMetaModelGenerator.generateTestingModel();
              }
            }
          }

          public static final class MetaTestingMetaModelClass extends MetaClass<io.art.meta.test.TestingMetaModel> {
            private static final io.art.core.property.LazyProperty<MetaTestingMetaModelClass> self = MetaClass.self(io.art.meta.test.TestingMetaModel.class);

            private final MetaConstructorConstructor constructor = registerConstructor(new MetaConstructorConstructor(this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Integer> f1Field = registerField(new MetaField<>("f1",metaType(int.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Short> f2Field = registerField(new MetaField<>("f2",metaType(short.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Double> f3Field = registerField(new MetaField<>("f3",metaType(double.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Float> f4Field = registerField(new MetaField<>("f4",metaType(float.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Long> f5Field = registerField(new MetaField<>("f5",metaType(long.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Boolean> f6Field = registerField(new MetaField<>("f6",metaType(boolean.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Character> f7Field = registerField(new MetaField<>("f7",metaType(char.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, Byte> f8Field = registerField(new MetaField<>("f8",metaType(byte.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Integer> f9Field = registerField(new MetaField<>("f9",metaType(java.lang.Integer.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Short> f10Field = registerField(new MetaField<>("f10",metaType(java.lang.Short.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Double> f11Field = registerField(new MetaField<>("f11",metaType(java.lang.Double.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Float> f12Field = registerField(new MetaField<>("f12",metaType(java.lang.Float.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Long> f13Field = registerField(new MetaField<>("f13",metaType(java.lang.Long.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Boolean> f14Field = registerField(new MetaField<>("f14",metaType(java.lang.Boolean.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Character> f15Field = registerField(new MetaField<>("f15",metaType(java.lang.Character.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.String> f16Field = registerField(new MetaField<>("f16",metaType(java.lang.String.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, int[]> f17Field = registerField(new MetaField<>("f17",metaArray(int[].class, int[]::new, metaType(int.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, short[]> f18Field = registerField(new MetaField<>("f18",metaArray(short[].class, short[]::new, metaType(short.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, double[]> f19Field = registerField(new MetaField<>("f19",metaArray(double[].class, double[]::new, metaType(double.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, float[]> f20Field = registerField(new MetaField<>("f20",metaArray(float[].class, float[]::new, metaType(float.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, long[]> f21Field = registerField(new MetaField<>("f21",metaArray(long[].class, long[]::new, metaType(long.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, boolean[]> f22Field = registerField(new MetaField<>("f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, char[]> f23Field = registerField(new MetaField<>("f23",metaArray(char[].class, char[]::new, metaType(char.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, byte[]> f24Field = registerField(new MetaField<>("f24",metaArray(byte[].class, byte[]::new, metaType(byte.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Integer[]> f25Field = registerField(new MetaField<>("f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Short[]> f26Field = registerField(new MetaField<>("f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Double[]> f27Field = registerField(new MetaField<>("f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Float[]> f28Field = registerField(new MetaField<>("f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Long[]> f29Field = registerField(new MetaField<>("f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Boolean[]> f30Field = registerField(new MetaField<>("f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.Character[]> f31Field = registerField(new MetaField<>("f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.String[]> f32Field = registerField(new MetaField<>("f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<java.lang.String>> f33Field = registerField(new MetaField<>("f33",metaType(java.util.List.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Set<java.lang.String>> f34Field = registerField(new MetaField<>("f34",metaType(java.util.Set.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Collection<java.lang.String>> f35Field = registerField(new MetaField<>("f35",metaType(java.util.Collection.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, io.art.core.collection.ImmutableArray<java.lang.String>> f36Field = registerField(new MetaField<>("f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, io.art.core.collection.ImmutableSet<java.lang.String>> f37Field = registerField(new MetaField<>("f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.function.Supplier<java.lang.String>> f38Field = registerField(new MetaField<>("f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, io.art.core.property.LazyProperty<java.lang.String>> f39Field = registerField(new MetaField<>("f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<int[]>> f40Field = registerField(new MetaField<>("f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<byte[]>> f41Field = registerField(new MetaField<>("f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<java.lang.String[]>> f42Field = registerField(new MetaField<>("f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<byte[]>[]> f43Field = registerField(new MetaField<>("f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<int[]>[]> f44Field = registerField(new MetaField<>("f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<java.lang.String[]>[]> f45Field = registerField(new MetaField<>("f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<java.util.List<java.lang.String>>> f46Field = registerField(new MetaField<>("f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<java.util.List<java.lang.String>[]>> f47Field = registerField(new MetaField<>("f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.Integer, java.lang.String>> f48Field = registerField(new MetaField<>("f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, java.lang.String[]>> f49Field = registerField(new MetaField<>("f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field = registerField(new MetaField<>("f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field = registerField(new MetaField<>("f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaModelClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field = registerField(new MetaField<>("f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel> f53Field = registerField(new MetaField<>("f53",metaType(io.art.meta.test.TestingMetaModel.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.time.LocalDateTime> f54Field = registerField(new MetaField<>("f54",metaType(java.time.LocalDateTime.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.time.ZonedDateTime> f55Field = registerField(new MetaField<>("f55",metaType(java.time.ZonedDateTime.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.time.Duration> f56Field = registerField(new MetaField<>("f56",metaType(java.time.Duration.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel.ModelEnum> f57Field = registerField(new MetaField<>("f57",metaEnum(io.art.meta.test.TestingMetaModel.ModelEnum.class, io.art.meta.test.TestingMetaModel.ModelEnum::valueOf),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field = registerField(new MetaField<>("f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.List<io.art.meta.test.TestingMetaModel>> f59Field = registerField(new MetaField<>("f59",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaModel.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Set<io.art.meta.test.TestingMetaModel>> f60Field = registerField(new MetaField<>("f60",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaModel.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Field = registerField(new MetaField<>("f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaModel.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, reactor.core.publisher.Mono<java.lang.String>> f62Field = registerField(new MetaField<>("f62",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, reactor.core.publisher.Flux<java.lang.String>> f63Field = registerField(new MetaField<>("f63",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.util.stream.Stream<java.lang.String>> f64Field = registerField(new MetaField<>("f64",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),false,this));

            private final MetaField<MetaTestingMetaModelClass, reactor.core.publisher.Mono<java.lang.String[]>> f65Field = registerField(new MetaField<>("f65",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.String> f66Field = registerField(new MetaField<>("f66",metaType(java.lang.String.class),false,this));

            private final MetaField<MetaTestingMetaModelClass, java.lang.String> f67Field = registerField(new MetaField<>("f67",metaType(java.lang.String.class),false,this));

            private final MetaAssertEqualsMethod assertEqualsMethod = registerMethod(new MetaAssertEqualsMethod(this));

            private final MetaToBuilderMethod toBuilderMethod = registerMethod(new MetaToBuilderMethod(this));

            private final MetaGetF1Method getF1Method = registerMethod(new MetaGetF1Method(this));

            private final MetaGetF2Method getF2Method = registerMethod(new MetaGetF2Method(this));

            private final MetaGetF3Method getF3Method = registerMethod(new MetaGetF3Method(this));

            private final MetaGetF4Method getF4Method = registerMethod(new MetaGetF4Method(this));

            private final MetaGetF5Method getF5Method = registerMethod(new MetaGetF5Method(this));

            private final MetaIsF6Method isF6Method = registerMethod(new MetaIsF6Method(this));

            private final MetaGetF7Method getF7Method = registerMethod(new MetaGetF7Method(this));

            private final MetaGetF8Method getF8Method = registerMethod(new MetaGetF8Method(this));

            private final MetaGetF9Method getF9Method = registerMethod(new MetaGetF9Method(this));

            private final MetaGetF10Method getF10Method = registerMethod(new MetaGetF10Method(this));

            private final MetaGetF11Method getF11Method = registerMethod(new MetaGetF11Method(this));

            private final MetaGetF12Method getF12Method = registerMethod(new MetaGetF12Method(this));

            private final MetaGetF13Method getF13Method = registerMethod(new MetaGetF13Method(this));

            private final MetaGetF14Method getF14Method = registerMethod(new MetaGetF14Method(this));

            private final MetaGetF15Method getF15Method = registerMethod(new MetaGetF15Method(this));

            private final MetaGetF16Method getF16Method = registerMethod(new MetaGetF16Method(this));

            private final MetaGetF17Method getF17Method = registerMethod(new MetaGetF17Method(this));

            private final MetaGetF18Method getF18Method = registerMethod(new MetaGetF18Method(this));

            private final MetaGetF19Method getF19Method = registerMethod(new MetaGetF19Method(this));

            private final MetaGetF20Method getF20Method = registerMethod(new MetaGetF20Method(this));

            private final MetaGetF21Method getF21Method = registerMethod(new MetaGetF21Method(this));

            private final MetaGetF22Method getF22Method = registerMethod(new MetaGetF22Method(this));

            private final MetaGetF23Method getF23Method = registerMethod(new MetaGetF23Method(this));

            private final MetaGetF24Method getF24Method = registerMethod(new MetaGetF24Method(this));

            private final MetaGetF25Method getF25Method = registerMethod(new MetaGetF25Method(this));

            private final MetaGetF26Method getF26Method = registerMethod(new MetaGetF26Method(this));

            private final MetaGetF27Method getF27Method = registerMethod(new MetaGetF27Method(this));

            private final MetaGetF28Method getF28Method = registerMethod(new MetaGetF28Method(this));

            private final MetaGetF29Method getF29Method = registerMethod(new MetaGetF29Method(this));

            private final MetaGetF30Method getF30Method = registerMethod(new MetaGetF30Method(this));

            private final MetaGetF31Method getF31Method = registerMethod(new MetaGetF31Method(this));

            private final MetaGetF32Method getF32Method = registerMethod(new MetaGetF32Method(this));

            private final MetaGetF33Method getF33Method = registerMethod(new MetaGetF33Method(this));

            private final MetaGetF34Method getF34Method = registerMethod(new MetaGetF34Method(this));

            private final MetaGetF35Method getF35Method = registerMethod(new MetaGetF35Method(this));

            private final MetaGetF36Method getF36Method = registerMethod(new MetaGetF36Method(this));

            private final MetaGetF37Method getF37Method = registerMethod(new MetaGetF37Method(this));

            private final MetaGetF38Method getF38Method = registerMethod(new MetaGetF38Method(this));

            private final MetaGetF39Method getF39Method = registerMethod(new MetaGetF39Method(this));

            private final MetaGetF40Method getF40Method = registerMethod(new MetaGetF40Method(this));

            private final MetaGetF41Method getF41Method = registerMethod(new MetaGetF41Method(this));

            private final MetaGetF42Method getF42Method = registerMethod(new MetaGetF42Method(this));

            private final MetaGetF43Method getF43Method = registerMethod(new MetaGetF43Method(this));

            private final MetaGetF44Method getF44Method = registerMethod(new MetaGetF44Method(this));

            private final MetaGetF45Method getF45Method = registerMethod(new MetaGetF45Method(this));

            private final MetaGetF46Method getF46Method = registerMethod(new MetaGetF46Method(this));

            private final MetaGetF47Method getF47Method = registerMethod(new MetaGetF47Method(this));

            private final MetaGetF48Method getF48Method = registerMethod(new MetaGetF48Method(this));

            private final MetaGetF49Method getF49Method = registerMethod(new MetaGetF49Method(this));

            private final MetaGetF50Method getF50Method = registerMethod(new MetaGetF50Method(this));

            private final MetaGetF51Method getF51Method = registerMethod(new MetaGetF51Method(this));

            private final MetaGetF52Method getF52Method = registerMethod(new MetaGetF52Method(this));

            private final MetaGetF53Method getF53Method = registerMethod(new MetaGetF53Method(this));

            private final MetaGetF54Method getF54Method = registerMethod(new MetaGetF54Method(this));

            private final MetaGetF55Method getF55Method = registerMethod(new MetaGetF55Method(this));

            private final MetaGetF56Method getF56Method = registerMethod(new MetaGetF56Method(this));

            private final MetaGetF57Method getF57Method = registerMethod(new MetaGetF57Method(this));

            private final MetaGetF58Method getF58Method = registerMethod(new MetaGetF58Method(this));

            private final MetaGetF59Method getF59Method = registerMethod(new MetaGetF59Method(this));

            private final MetaGetF60Method getF60Method = registerMethod(new MetaGetF60Method(this));

            private final MetaGetF61Method getF61Method = registerMethod(new MetaGetF61Method(this));

            private final MetaGetF62Method getF62Method = registerMethod(new MetaGetF62Method(this));

            private final MetaGetF63Method getF63Method = registerMethod(new MetaGetF63Method(this));

            private final MetaGetF64Method getF64Method = registerMethod(new MetaGetF64Method(this));

            private final MetaGetF65Method getF65Method = registerMethod(new MetaGetF65Method(this));

            private final MetaGetF66Method getF66Method = registerMethod(new MetaGetF66Method(this));

            private final MetaGetF67Method getF67Method = registerMethod(new MetaGetF67Method(this));

            private final MetaTestingMetaModelBuilderClass testingMetaModelBuilderClass = registerClass(new MetaTestingMetaModelBuilderClass());

            private MetaTestingMetaModelClass() {
              super(metaType(io.art.meta.test.TestingMetaModel.class));
            }

            public static MetaTestingMetaModelClass testingMetaModel() {
              return self.get();
            }

            public MetaConstructorConstructor constructor() {
              return constructor;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Integer> f1Field() {
              return f1Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Short> f2Field() {
              return f2Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Double> f3Field() {
              return f3Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Float> f4Field() {
              return f4Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Long> f5Field() {
              return f5Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Boolean> f6Field() {
              return f6Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Character> f7Field() {
              return f7Field;
            }

            public MetaField<MetaTestingMetaModelClass, Byte> f8Field() {
              return f8Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Integer> f9Field() {
              return f9Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Short> f10Field() {
              return f10Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Double> f11Field() {
              return f11Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Float> f12Field() {
              return f12Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Long> f13Field() {
              return f13Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Boolean> f14Field() {
              return f14Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Character> f15Field() {
              return f15Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.String> f16Field() {
              return f16Field;
            }

            public MetaField<MetaTestingMetaModelClass, int[]> f17Field() {
              return f17Field;
            }

            public MetaField<MetaTestingMetaModelClass, short[]> f18Field() {
              return f18Field;
            }

            public MetaField<MetaTestingMetaModelClass, double[]> f19Field() {
              return f19Field;
            }

            public MetaField<MetaTestingMetaModelClass, float[]> f20Field() {
              return f20Field;
            }

            public MetaField<MetaTestingMetaModelClass, long[]> f21Field() {
              return f21Field;
            }

            public MetaField<MetaTestingMetaModelClass, boolean[]> f22Field() {
              return f22Field;
            }

            public MetaField<MetaTestingMetaModelClass, char[]> f23Field() {
              return f23Field;
            }

            public MetaField<MetaTestingMetaModelClass, byte[]> f24Field() {
              return f24Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Integer[]> f25Field() {
              return f25Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Short[]> f26Field() {
              return f26Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Double[]> f27Field() {
              return f27Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Float[]> f28Field() {
              return f28Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Long[]> f29Field() {
              return f29Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Boolean[]> f30Field() {
              return f30Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.Character[]> f31Field() {
              return f31Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.String[]> f32Field() {
              return f32Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<java.lang.String>> f33Field(
                ) {
              return f33Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Set<java.lang.String>> f34Field(
                ) {
              return f34Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Collection<java.lang.String>> f35Field(
                ) {
              return f35Field;
            }

            public MetaField<MetaTestingMetaModelClass, io.art.core.collection.ImmutableArray<java.lang.String>> f36Field(
                ) {
              return f36Field;
            }

            public MetaField<MetaTestingMetaModelClass, io.art.core.collection.ImmutableSet<java.lang.String>> f37Field(
                ) {
              return f37Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.function.Supplier<java.lang.String>> f38Field(
                ) {
              return f38Field;
            }

            public MetaField<MetaTestingMetaModelClass, io.art.core.property.LazyProperty<java.lang.String>> f39Field(
                ) {
              return f39Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<int[]>> f40Field() {
              return f40Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<byte[]>> f41Field() {
              return f41Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<java.lang.String[]>> f42Field(
                ) {
              return f42Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<byte[]>[]> f43Field() {
              return f43Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<int[]>[]> f44Field() {
              return f44Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<java.lang.String[]>[]> f45Field(
                ) {
              return f45Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<java.util.List<java.lang.String>>> f46Field(
                ) {
              return f46Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<java.util.List<java.lang.String>[]>> f47Field(
                ) {
              return f47Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.Integer, java.lang.String>> f48Field(
                ) {
              return f48Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, java.lang.String[]>> f49Field(
                ) {
              return f49Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field(
                ) {
              return f50Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field(
                ) {
              return f51Field;
            }

            public MetaField<MetaTestingMetaModelClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field(
                ) {
              return f52Field;
            }

            public MetaField<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel> f53Field(
                ) {
              return f53Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.time.LocalDateTime> f54Field() {
              return f54Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.time.ZonedDateTime> f55Field() {
              return f55Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.time.Duration> f56Field() {
              return f56Field;
            }

            public MetaField<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel.ModelEnum> f57Field(
                ) {
              return f57Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field(
                ) {
              return f58Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.List<io.art.meta.test.TestingMetaModel>> f59Field(
                ) {
              return f59Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Set<io.art.meta.test.TestingMetaModel>> f60Field(
                ) {
              return f60Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Field(
                ) {
              return f61Field;
            }

            public MetaField<MetaTestingMetaModelClass, reactor.core.publisher.Mono<java.lang.String>> f62Field(
                ) {
              return f62Field;
            }

            public MetaField<MetaTestingMetaModelClass, reactor.core.publisher.Flux<java.lang.String>> f63Field(
                ) {
              return f63Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.util.stream.Stream<java.lang.String>> f64Field(
                ) {
              return f64Field;
            }

            public MetaField<MetaTestingMetaModelClass, reactor.core.publisher.Mono<java.lang.String[]>> f65Field(
                ) {
              return f65Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.String> f66Field() {
              return f66Field;
            }

            public MetaField<MetaTestingMetaModelClass, java.lang.String> f67Field() {
              return f67Field;
            }

            public MetaAssertEqualsMethod assertEqualsMethod() {
              return assertEqualsMethod;
            }

            public MetaToBuilderMethod toBuilderMethod() {
              return toBuilderMethod;
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

            public MetaGetF62Method getF62Method() {
              return getF62Method;
            }

            public MetaGetF63Method getF63Method() {
              return getF63Method;
            }

            public MetaGetF64Method getF64Method() {
              return getF64Method;
            }

            public MetaGetF65Method getF65Method() {
              return getF65Method;
            }

            public MetaGetF66Method getF66Method() {
              return getF66Method;
            }

            public MetaGetF67Method getF67Method() {
              return getF67Method;
            }

            public MetaTestingMetaModelBuilderClass testingMetaModelBuilderClass() {
              return testingMetaModelBuilderClass;
            }

            public final class MetaConstructorConstructor extends MetaConstructor<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel> {
              private final MetaParameter<java.lang.Integer> f1Parameter = register(new MetaParameter<>(0, "f1",metaType(int.class)));

              private final MetaParameter<java.lang.Short> f2Parameter = register(new MetaParameter<>(1, "f2",metaType(short.class)));

              private final MetaParameter<java.lang.Double> f3Parameter = register(new MetaParameter<>(2, "f3",metaType(double.class)));

              private final MetaParameter<java.lang.Float> f4Parameter = register(new MetaParameter<>(3, "f4",metaType(float.class)));

              private final MetaParameter<java.lang.Long> f5Parameter = register(new MetaParameter<>(4, "f5",metaType(long.class)));

              private final MetaParameter<java.lang.Boolean> f6Parameter = register(new MetaParameter<>(5, "f6",metaType(boolean.class)));

              private final MetaParameter<java.lang.Character> f7Parameter = register(new MetaParameter<>(6, "f7",metaType(char.class)));

              private final MetaParameter<Byte> f8Parameter = register(new MetaParameter<>(7, "f8",metaType(byte.class)));

              private final MetaParameter<java.lang.Integer> f9Parameter = register(new MetaParameter<>(8, "f9",metaType(java.lang.Integer.class)));

              private final MetaParameter<java.lang.Short> f10Parameter = register(new MetaParameter<>(9, "f10",metaType(java.lang.Short.class)));

              private final MetaParameter<java.lang.Double> f11Parameter = register(new MetaParameter<>(10, "f11",metaType(java.lang.Double.class)));

              private final MetaParameter<java.lang.Float> f12Parameter = register(new MetaParameter<>(11, "f12",metaType(java.lang.Float.class)));

              private final MetaParameter<java.lang.Long> f13Parameter = register(new MetaParameter<>(12, "f13",metaType(java.lang.Long.class)));

              private final MetaParameter<java.lang.Boolean> f14Parameter = register(new MetaParameter<>(13, "f14",metaType(java.lang.Boolean.class)));

              private final MetaParameter<java.lang.Character> f15Parameter = register(new MetaParameter<>(14, "f15",metaType(java.lang.Character.class)));

              private final MetaParameter<java.lang.String> f16Parameter = register(new MetaParameter<>(15, "f16",metaType(java.lang.String.class)));

              private final MetaParameter<int[]> f17Parameter = register(new MetaParameter<>(16, "f17",metaArray(int[].class, int[]::new, metaType(int.class))));

              private final MetaParameter<short[]> f18Parameter = register(new MetaParameter<>(17, "f18",metaArray(short[].class, short[]::new, metaType(short.class))));

              private final MetaParameter<double[]> f19Parameter = register(new MetaParameter<>(18, "f19",metaArray(double[].class, double[]::new, metaType(double.class))));

              private final MetaParameter<float[]> f20Parameter = register(new MetaParameter<>(19, "f20",metaArray(float[].class, float[]::new, metaType(float.class))));

              private final MetaParameter<long[]> f21Parameter = register(new MetaParameter<>(20, "f21",metaArray(long[].class, long[]::new, metaType(long.class))));

              private final MetaParameter<boolean[]> f22Parameter = register(new MetaParameter<>(21, "f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class))));

              private final MetaParameter<char[]> f23Parameter = register(new MetaParameter<>(22, "f23",metaArray(char[].class, char[]::new, metaType(char.class))));

              private final MetaParameter<byte[]> f24Parameter = register(new MetaParameter<>(23, "f24",metaArray(byte[].class, byte[]::new, metaType(byte.class))));

              private final MetaParameter<java.lang.Integer[]> f25Parameter = register(new MetaParameter<>(24, "f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class))));

              private final MetaParameter<java.lang.Short[]> f26Parameter = register(new MetaParameter<>(25, "f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class))));

              private final MetaParameter<java.lang.Double[]> f27Parameter = register(new MetaParameter<>(26, "f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class))));

              private final MetaParameter<java.lang.Float[]> f28Parameter = register(new MetaParameter<>(27, "f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class))));

              private final MetaParameter<java.lang.Long[]> f29Parameter = register(new MetaParameter<>(28, "f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class))));

              private final MetaParameter<java.lang.Boolean[]> f30Parameter = register(new MetaParameter<>(29, "f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class))));

              private final MetaParameter<java.lang.Character[]> f31Parameter = register(new MetaParameter<>(30, "f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class))));

              private final MetaParameter<java.lang.String[]> f32Parameter = register(new MetaParameter<>(31, "f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));

              private final MetaParameter<java.util.List<java.lang.String>> f33Parameter = register(new MetaParameter<>(32, "f33",metaType(java.util.List.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.Set<java.lang.String>> f34Parameter = register(new MetaParameter<>(33, "f34",metaType(java.util.Set.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.Collection<java.lang.String>> f35Parameter = register(new MetaParameter<>(34, "f35",metaType(java.util.Collection.class,metaType(java.lang.String.class))));

              private final MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f36Parameter = register(new MetaParameter<>(35, "f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class))));

              private final MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f37Parameter = register(new MetaParameter<>(36, "f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter = register(new MetaParameter<>(37, "f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class))));

              private final MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f39Parameter = register(new MetaParameter<>(38, "f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.List<int[]>> f40Parameter = register(new MetaParameter<>(39, "f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))));

              private final MetaParameter<java.util.List<byte[]>> f41Parameter = register(new MetaParameter<>(40, "f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

              private final MetaParameter<java.util.List<java.lang.String[]>> f42Parameter = register(new MetaParameter<>(41, "f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.List<byte[]>[]> f43Parameter = register(new MetaParameter<>(42, "f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))))));

              private final MetaParameter<java.util.List<int[]>[]> f44Parameter = register(new MetaParameter<>(43, "f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))))));

              private final MetaParameter<java.util.List<java.lang.String[]>[]> f45Parameter = register(new MetaParameter<>(44, "f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))))));

              private final MetaParameter<java.util.List<java.util.List<java.lang.String>>> f46Parameter = register(new MetaParameter<>(45, "f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f47Parameter = register(new MetaParameter<>(46, "f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class))))));

              private final MetaParameter<java.util.Map<java.lang.Integer, java.lang.String>> f48Parameter = register(new MetaParameter<>(47, "f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class))));

              private final MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f49Parameter = register(new MetaParameter<>(48, "f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Parameter = register(new MetaParameter<>(49, "f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class)))));

              private final MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Parameter = register(new MetaParameter<>(50, "f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

              private final MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Parameter = register(new MetaParameter<>(51, "f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

              private final MetaParameter<io.art.meta.test.TestingMetaModel> f53Parameter = register(new MetaParameter<>(52, "f53",metaType(io.art.meta.test.TestingMetaModel.class)));

              private final MetaParameter<java.time.LocalDateTime> f54Parameter = register(new MetaParameter<>(53, "f54",metaType(java.time.LocalDateTime.class)));

              private final MetaParameter<java.time.ZonedDateTime> f55Parameter = register(new MetaParameter<>(54, "f55",metaType(java.time.ZonedDateTime.class)));

              private final MetaParameter<java.time.Duration> f56Parameter = register(new MetaParameter<>(55, "f56",metaType(java.time.Duration.class)));

              private final MetaParameter<io.art.meta.test.TestingMetaModel.ModelEnum> f57Parameter = register(new MetaParameter<>(56, "f57",metaEnum(io.art.meta.test.TestingMetaModel.ModelEnum.class, io.art.meta.test.TestingMetaModel.ModelEnum::valueOf)));

              private final MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter = register(new MetaParameter<>(57, "f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))))));

              private final MetaParameter<java.util.List<io.art.meta.test.TestingMetaModel>> f59Parameter = register(new MetaParameter<>(58, "f59",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaModel.class))));

              private final MetaParameter<java.util.Set<io.art.meta.test.TestingMetaModel>> f60Parameter = register(new MetaParameter<>(59, "f60",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaModel.class))));

              private final MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Parameter = register(new MetaParameter<>(60, "f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaModel.class))));

              private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f62Parameter = register(new MetaParameter<>(61, "f62",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

              private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f63Parameter = register(new MetaParameter<>(62, "f63",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

              private final MetaParameter<java.util.stream.Stream<java.lang.String>> f64Parameter = register(new MetaParameter<>(63, "f64",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class))));

              private final MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f65Parameter = register(new MetaParameter<>(64, "f65",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

              private final MetaParameter<java.lang.String> f66Parameter = register(new MetaParameter<>(65, "f66",metaType(java.lang.String.class)));

              private final MetaParameter<java.lang.String> f67Parameter = register(new MetaParameter<>(66, "f67",metaType(java.lang.String.class)));

              private MetaConstructorConstructor(MetaTestingMetaModelClass owner) {
                super(metaType(io.art.meta.test.TestingMetaModel.class),owner);
              }

              @Override
              public io.art.meta.test.TestingMetaModel invoke(Object[] arguments) throws Throwable {
                return new io.art.meta.test.TestingMetaModel((int)(arguments[0]),(short)(arguments[1]),(double)(arguments[2]),(float)(arguments[3]),(long)(arguments[4]),(boolean)(arguments[5]),(char)(arguments[6]),(byte)(arguments[7]),(java.lang.Integer)(arguments[8]),(java.lang.Short)(arguments[9]),(java.lang.Double)(arguments[10]),(java.lang.Float)(arguments[11]),(java.lang.Long)(arguments[12]),(java.lang.Boolean)(arguments[13]),(java.lang.Character)(arguments[14]),(java.lang.String)(arguments[15]),(int[])(arguments[16]),(short[])(arguments[17]),(double[])(arguments[18]),(float[])(arguments[19]),(long[])(arguments[20]),(boolean[])(arguments[21]),(char[])(arguments[22]),(byte[])(arguments[23]),(java.lang.Integer[])(arguments[24]),(java.lang.Short[])(arguments[25]),(java.lang.Double[])(arguments[26]),(java.lang.Float[])(arguments[27]),(java.lang.Long[])(arguments[28]),(java.lang.Boolean[])(arguments[29]),(java.lang.Character[])(arguments[30]),(java.lang.String[])(arguments[31]),(java.util.List<java.lang.String>)(arguments[32]),(java.util.Set<java.lang.String>)(arguments[33]),(java.util.Collection<java.lang.String>)(arguments[34]),(io.art.core.collection.ImmutableArray<java.lang.String>)(arguments[35]),(io.art.core.collection.ImmutableSet<java.lang.String>)(arguments[36]),(java.util.function.Supplier<java.lang.String>)(arguments[37]),(io.art.core.property.LazyProperty<java.lang.String>)(arguments[38]),(java.util.List<int[]>)(arguments[39]),(java.util.List<byte[]>)(arguments[40]),(java.util.List<java.lang.String[]>)(arguments[41]),(java.util.List<byte[]>[])(arguments[42]),(java.util.List<int[]>[])(arguments[43]),(java.util.List<java.lang.String[]>[])(arguments[44]),(java.util.List<java.util.List<java.lang.String>>)(arguments[45]),(java.util.List<java.util.List<java.lang.String>[]>)(arguments[46]),(java.util.Map<java.lang.Integer, java.lang.String>)(arguments[47]),(java.util.Map<java.lang.String, java.lang.String[]>)(arguments[48]),(java.util.Map<java.lang.String, java.util.List<java.lang.String>>)(arguments[49]),(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[50]),(io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[51]),(io.art.meta.test.TestingMetaModel)(arguments[52]),(java.time.LocalDateTime)(arguments[53]),(java.time.ZonedDateTime)(arguments[54]),(java.time.Duration)(arguments[55]),(io.art.meta.test.TestingMetaModel.ModelEnum)(arguments[56]),(java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>)(arguments[57]),(java.util.List<io.art.meta.test.TestingMetaModel>)(arguments[58]),(java.util.Set<io.art.meta.test.TestingMetaModel>)(arguments[59]),(java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>)(arguments[60]),(reactor.core.publisher.Mono<java.lang.String>)(arguments[61]),(reactor.core.publisher.Flux<java.lang.String>)(arguments[62]),(java.util.stream.Stream<java.lang.String>)(arguments[63]),(reactor.core.publisher.Mono<java.lang.String[]>)(arguments[64]),(java.lang.String)(arguments[65]),(java.lang.String)(arguments[66]));
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

              public MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter() {
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

              public MetaParameter<io.art.meta.test.TestingMetaModel> f53Parameter() {
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

              public MetaParameter<io.art.meta.test.TestingMetaModel.ModelEnum> f57Parameter() {
                return f57Parameter;
              }

              public MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter(
                  ) {
                return f58Parameter;
              }

              public MetaParameter<java.util.List<io.art.meta.test.TestingMetaModel>> f59Parameter(
                  ) {
                return f59Parameter;
              }

              public MetaParameter<java.util.Set<io.art.meta.test.TestingMetaModel>> f60Parameter(
                  ) {
                return f60Parameter;
              }

              public MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Parameter(
                  ) {
                return f61Parameter;
              }

              public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f62Parameter() {
                return f62Parameter;
              }

              public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f63Parameter() {
                return f63Parameter;
              }

              public MetaParameter<java.util.stream.Stream<java.lang.String>> f64Parameter() {
                return f64Parameter;
              }

              public MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f65Parameter() {
                return f65Parameter;
              }

              public MetaParameter<java.lang.String> f66Parameter() {
                return f66Parameter;
              }

              public MetaParameter<java.lang.String> f67Parameter() {
                return f67Parameter;
              }
            }

            public final class MetaAssertEqualsMethod extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, Void> {
              private final MetaParameter<io.art.meta.test.TestingMetaModel> modelParameter = register(new MetaParameter<>(0, "model",metaType(io.art.meta.test.TestingMetaModel.class)));

              private MetaAssertEqualsMethod(MetaTestingMetaModelClass owner) {
                super("assertEquals",metaType(Void.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                instance.assertEquals((io.art.meta.test.TestingMetaModel)(arguments[0]));
                return null;
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object argument)
                  throws Throwable {
                instance.assertEquals((io.art.meta.test.TestingMetaModel)(argument));
                return null;
              }

              public MetaParameter<io.art.meta.test.TestingMetaModel> modelParameter() {
                return modelParameter;
              }
            }

            public final class MetaToBuilderMethod extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
              private MetaToBuilderMethod(MetaTestingMetaModelClass owner) {
                super("toBuilder",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.toBuilder();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.toBuilder();
              }
            }

            public final class MetaGetF1Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Integer> {
              private MetaGetF1Method(MetaTestingMetaModelClass owner) {
                super("getF1",metaType(int.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF1();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF1();
              }
            }

            public final class MetaGetF2Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Short> {
              private MetaGetF2Method(MetaTestingMetaModelClass owner) {
                super("getF2",metaType(short.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF2();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF2();
              }
            }

            public final class MetaGetF3Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Double> {
              private MetaGetF3Method(MetaTestingMetaModelClass owner) {
                super("getF3",metaType(double.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF3();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF3();
              }
            }

            public final class MetaGetF4Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Float> {
              private MetaGetF4Method(MetaTestingMetaModelClass owner) {
                super("getF4",metaType(float.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF4();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF4();
              }
            }

            public final class MetaGetF5Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Long> {
              private MetaGetF5Method(MetaTestingMetaModelClass owner) {
                super("getF5",metaType(long.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF5();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF5();
              }
            }

            public final class MetaIsF6Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Boolean> {
              private MetaIsF6Method(MetaTestingMetaModelClass owner) {
                super("isF6",metaType(boolean.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.isF6();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.isF6();
              }
            }

            public final class MetaGetF7Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Character> {
              private MetaGetF7Method(MetaTestingMetaModelClass owner) {
                super("getF7",metaType(char.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF7();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF7();
              }
            }

            public final class MetaGetF8Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, Byte> {
              private MetaGetF8Method(MetaTestingMetaModelClass owner) {
                super("getF8",metaType(byte.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF8();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF8();
              }
            }

            public final class MetaGetF9Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Integer> {
              private MetaGetF9Method(MetaTestingMetaModelClass owner) {
                super("getF9",metaType(java.lang.Integer.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF9();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF9();
              }
            }

            public final class MetaGetF10Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Short> {
              private MetaGetF10Method(MetaTestingMetaModelClass owner) {
                super("getF10",metaType(java.lang.Short.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF10();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF10();
              }
            }

            public final class MetaGetF11Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Double> {
              private MetaGetF11Method(MetaTestingMetaModelClass owner) {
                super("getF11",metaType(java.lang.Double.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF11();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF11();
              }
            }

            public final class MetaGetF12Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Float> {
              private MetaGetF12Method(MetaTestingMetaModelClass owner) {
                super("getF12",metaType(java.lang.Float.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF12();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF12();
              }
            }

            public final class MetaGetF13Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Long> {
              private MetaGetF13Method(MetaTestingMetaModelClass owner) {
                super("getF13",metaType(java.lang.Long.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF13();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF13();
              }
            }

            public final class MetaGetF14Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Boolean> {
              private MetaGetF14Method(MetaTestingMetaModelClass owner) {
                super("getF14",metaType(java.lang.Boolean.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF14();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF14();
              }
            }

            public final class MetaGetF15Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Character> {
              private MetaGetF15Method(MetaTestingMetaModelClass owner) {
                super("getF15",metaType(java.lang.Character.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF15();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF15();
              }
            }

            public final class MetaGetF16Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.String> {
              private MetaGetF16Method(MetaTestingMetaModelClass owner) {
                super("getF16",metaType(java.lang.String.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF16();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF16();
              }
            }

            public final class MetaGetF17Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, int[]> {
              private MetaGetF17Method(MetaTestingMetaModelClass owner) {
                super("getF17",metaArray(int[].class, int[]::new, metaType(int.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF17();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF17();
              }
            }

            public final class MetaGetF18Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, short[]> {
              private MetaGetF18Method(MetaTestingMetaModelClass owner) {
                super("getF18",metaArray(short[].class, short[]::new, metaType(short.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF18();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF18();
              }
            }

            public final class MetaGetF19Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, double[]> {
              private MetaGetF19Method(MetaTestingMetaModelClass owner) {
                super("getF19",metaArray(double[].class, double[]::new, metaType(double.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF19();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF19();
              }
            }

            public final class MetaGetF20Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, float[]> {
              private MetaGetF20Method(MetaTestingMetaModelClass owner) {
                super("getF20",metaArray(float[].class, float[]::new, metaType(float.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF20();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF20();
              }
            }

            public final class MetaGetF21Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, long[]> {
              private MetaGetF21Method(MetaTestingMetaModelClass owner) {
                super("getF21",metaArray(long[].class, long[]::new, metaType(long.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF21();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF21();
              }
            }

            public final class MetaGetF22Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, boolean[]> {
              private MetaGetF22Method(MetaTestingMetaModelClass owner) {
                super("getF22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF22();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF22();
              }
            }

            public final class MetaGetF23Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, char[]> {
              private MetaGetF23Method(MetaTestingMetaModelClass owner) {
                super("getF23",metaArray(char[].class, char[]::new, metaType(char.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF23();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF23();
              }
            }

            public final class MetaGetF24Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, byte[]> {
              private MetaGetF24Method(MetaTestingMetaModelClass owner) {
                super("getF24",metaArray(byte[].class, byte[]::new, metaType(byte.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF24();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF24();
              }
            }

            public final class MetaGetF25Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Integer[]> {
              private MetaGetF25Method(MetaTestingMetaModelClass owner) {
                super("getF25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF25();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF25();
              }
            }

            public final class MetaGetF26Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Short[]> {
              private MetaGetF26Method(MetaTestingMetaModelClass owner) {
                super("getF26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF26();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF26();
              }
            }

            public final class MetaGetF27Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Double[]> {
              private MetaGetF27Method(MetaTestingMetaModelClass owner) {
                super("getF27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF27();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF27();
              }
            }

            public final class MetaGetF28Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Float[]> {
              private MetaGetF28Method(MetaTestingMetaModelClass owner) {
                super("getF28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF28();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF28();
              }
            }

            public final class MetaGetF29Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Long[]> {
              private MetaGetF29Method(MetaTestingMetaModelClass owner) {
                super("getF29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF29();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF29();
              }
            }

            public final class MetaGetF30Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Boolean[]> {
              private MetaGetF30Method(MetaTestingMetaModelClass owner) {
                super("getF30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF30();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF30();
              }
            }

            public final class MetaGetF31Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.Character[]> {
              private MetaGetF31Method(MetaTestingMetaModelClass owner) {
                super("getF31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF31();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF31();
              }
            }

            public final class MetaGetF32Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.String[]> {
              private MetaGetF32Method(MetaTestingMetaModelClass owner) {
                super("getF32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF32();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF32();
              }
            }

            public final class MetaGetF33Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<java.lang.String>> {
              private MetaGetF33Method(MetaTestingMetaModelClass owner) {
                super("getF33",metaType(java.util.List.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF33();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF33();
              }
            }

            public final class MetaGetF34Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Set<java.lang.String>> {
              private MetaGetF34Method(MetaTestingMetaModelClass owner) {
                super("getF34",metaType(java.util.Set.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF34();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF34();
              }
            }

            public final class MetaGetF35Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Collection<java.lang.String>> {
              private MetaGetF35Method(MetaTestingMetaModelClass owner) {
                super("getF35",metaType(java.util.Collection.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF35();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF35();
              }
            }

            public final class MetaGetF36Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, io.art.core.collection.ImmutableArray<java.lang.String>> {
              private MetaGetF36Method(MetaTestingMetaModelClass owner) {
                super("getF36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF36();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF36();
              }
            }

            public final class MetaGetF37Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, io.art.core.collection.ImmutableSet<java.lang.String>> {
              private MetaGetF37Method(MetaTestingMetaModelClass owner) {
                super("getF37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF37();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF37();
              }
            }

            public final class MetaGetF38Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.function.Supplier<java.lang.String>> {
              private MetaGetF38Method(MetaTestingMetaModelClass owner) {
                super("getF38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF38();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF38();
              }
            }

            public final class MetaGetF39Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, io.art.core.property.LazyProperty<java.lang.String>> {
              private MetaGetF39Method(MetaTestingMetaModelClass owner) {
                super("getF39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF39();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF39();
              }
            }

            public final class MetaGetF40Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<int[]>> {
              private MetaGetF40Method(MetaTestingMetaModelClass owner) {
                super("getF40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF40();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF40();
              }
            }

            public final class MetaGetF41Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<byte[]>> {
              private MetaGetF41Method(MetaTestingMetaModelClass owner) {
                super("getF41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF41();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF41();
              }
            }

            public final class MetaGetF42Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<java.lang.String[]>> {
              private MetaGetF42Method(MetaTestingMetaModelClass owner) {
                super("getF42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF42();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF42();
              }
            }

            public final class MetaGetF43Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<byte[]>[]> {
              private MetaGetF43Method(MetaTestingMetaModelClass owner) {
                super("getF43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF43();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF43();
              }
            }

            public final class MetaGetF44Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<int[]>[]> {
              private MetaGetF44Method(MetaTestingMetaModelClass owner) {
                super("getF44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF44();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF44();
              }
            }

            public final class MetaGetF45Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<java.lang.String[]>[]> {
              private MetaGetF45Method(MetaTestingMetaModelClass owner) {
                super("getF45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF45();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF45();
              }
            }

            public final class MetaGetF46Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<java.util.List<java.lang.String>>> {
              private MetaGetF46Method(MetaTestingMetaModelClass owner) {
                super("getF46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF46();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF46();
              }
            }

            public final class MetaGetF47Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<java.util.List<java.lang.String>[]>> {
              private MetaGetF47Method(MetaTestingMetaModelClass owner) {
                super("getF47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF47();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF47();
              }
            }

            public final class MetaGetF48Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Map<java.lang.Integer, java.lang.String>> {
              private MetaGetF48Method(MetaTestingMetaModelClass owner) {
                super("getF48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF48();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF48();
              }
            }

            public final class MetaGetF49Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Map<java.lang.String, java.lang.String[]>> {
              private MetaGetF49Method(MetaTestingMetaModelClass owner) {
                super("getF49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF49();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF49();
              }
            }

            public final class MetaGetF50Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> {
              private MetaGetF50Method(MetaTestingMetaModelClass owner) {
                super("getF50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF50();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF50();
              }
            }

            public final class MetaGetF51Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> {
              private MetaGetF51Method(MetaTestingMetaModelClass owner) {
                super("getF51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF51();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF51();
              }
            }

            public final class MetaGetF52Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> {
              private MetaGetF52Method(MetaTestingMetaModelClass owner) {
                super("getF52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF52();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF52();
              }
            }

            public final class MetaGetF53Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, io.art.meta.test.TestingMetaModel> {
              private MetaGetF53Method(MetaTestingMetaModelClass owner) {
                super("getF53",metaType(io.art.meta.test.TestingMetaModel.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF53();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF53();
              }
            }

            public final class MetaGetF54Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.time.LocalDateTime> {
              private MetaGetF54Method(MetaTestingMetaModelClass owner) {
                super("getF54",metaType(java.time.LocalDateTime.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF54();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF54();
              }
            }

            public final class MetaGetF55Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.time.ZonedDateTime> {
              private MetaGetF55Method(MetaTestingMetaModelClass owner) {
                super("getF55",metaType(java.time.ZonedDateTime.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF55();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF55();
              }
            }

            public final class MetaGetF56Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.time.Duration> {
              private MetaGetF56Method(MetaTestingMetaModelClass owner) {
                super("getF56",metaType(java.time.Duration.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF56();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF56();
              }
            }

            public final class MetaGetF57Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, io.art.meta.test.TestingMetaModel.ModelEnum> {
              private MetaGetF57Method(MetaTestingMetaModelClass owner) {
                super("getF57",metaEnum(io.art.meta.test.TestingMetaModel.ModelEnum.class, io.art.meta.test.TestingMetaModel.ModelEnum::valueOf),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF57();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF57();
              }
            }

            public final class MetaGetF58Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> {
              private MetaGetF58Method(MetaTestingMetaModelClass owner) {
                super("getF58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF58();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF58();
              }
            }

            public final class MetaGetF59Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.List<io.art.meta.test.TestingMetaModel>> {
              private MetaGetF59Method(MetaTestingMetaModelClass owner) {
                super("getF59",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaModel.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF59();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF59();
              }
            }

            public final class MetaGetF60Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Set<io.art.meta.test.TestingMetaModel>> {
              private MetaGetF60Method(MetaTestingMetaModelClass owner) {
                super("getF60",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaModel.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF60();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF60();
              }
            }

            public final class MetaGetF61Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> {
              private MetaGetF61Method(MetaTestingMetaModelClass owner) {
                super("getF61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaModel.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF61();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF61();
              }
            }

            public final class MetaGetF62Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, reactor.core.publisher.Mono<java.lang.String>> {
              private MetaGetF62Method(MetaTestingMetaModelClass owner) {
                super("getF62",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF62();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF62();
              }
            }

            public final class MetaGetF63Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, reactor.core.publisher.Flux<java.lang.String>> {
              private MetaGetF63Method(MetaTestingMetaModelClass owner) {
                super("getF63",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF63();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF63();
              }
            }

            public final class MetaGetF64Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.util.stream.Stream<java.lang.String>> {
              private MetaGetF64Method(MetaTestingMetaModelClass owner) {
                super("getF64",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF64();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF64();
              }
            }

            public final class MetaGetF65Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, reactor.core.publisher.Mono<java.lang.String[]>> {
              private MetaGetF65Method(MetaTestingMetaModelClass owner) {
                super("getF65",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF65();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF65();
              }
            }

            public final class MetaGetF66Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.String> {
              private MetaGetF66Method(MetaTestingMetaModelClass owner) {
                super("getF66",metaType(java.lang.String.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF66();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF66();
              }
            }

            public final class MetaGetF67Method extends InstanceMetaMethod<MetaTestingMetaModelClass, io.art.meta.test.TestingMetaModel, java.lang.String> {
              private MetaGetF67Method(MetaTestingMetaModelClass owner) {
                super("getF67",metaType(java.lang.String.class),owner);
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance, Object[] arguments)
                  throws Throwable {
                return instance.getF67();
              }

              @Override
              public Object invoke(io.art.meta.test.TestingMetaModel instance) throws Throwable {
                return instance.getF67();
              }
            }

            public static final class MetaTestingMetaModelBuilderClass extends MetaClass<io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
              private static final io.art.core.property.LazyProperty<MetaTestingMetaModelBuilderClass> self = MetaClass.self(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class);

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Integer> f1Field = registerField(new MetaField<>("f1",metaType(int.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Short> f2Field = registerField(new MetaField<>("f2",metaType(short.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Double> f3Field = registerField(new MetaField<>("f3",metaType(double.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Float> f4Field = registerField(new MetaField<>("f4",metaType(float.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Long> f5Field = registerField(new MetaField<>("f5",metaType(long.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Boolean> f6Field = registerField(new MetaField<>("f6",metaType(boolean.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Character> f7Field = registerField(new MetaField<>("f7",metaType(char.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, Byte> f8Field = registerField(new MetaField<>("f8",metaType(byte.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Integer> f9Field = registerField(new MetaField<>("f9",metaType(java.lang.Integer.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Short> f10Field = registerField(new MetaField<>("f10",metaType(java.lang.Short.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Double> f11Field = registerField(new MetaField<>("f11",metaType(java.lang.Double.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Float> f12Field = registerField(new MetaField<>("f12",metaType(java.lang.Float.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Long> f13Field = registerField(new MetaField<>("f13",metaType(java.lang.Long.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Boolean> f14Field = registerField(new MetaField<>("f14",metaType(java.lang.Boolean.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Character> f15Field = registerField(new MetaField<>("f15",metaType(java.lang.Character.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.String> f16Field = registerField(new MetaField<>("f16",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, int[]> f17Field = registerField(new MetaField<>("f17",metaArray(int[].class, int[]::new, metaType(int.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, short[]> f18Field = registerField(new MetaField<>("f18",metaArray(short[].class, short[]::new, metaType(short.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, double[]> f19Field = registerField(new MetaField<>("f19",metaArray(double[].class, double[]::new, metaType(double.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, float[]> f20Field = registerField(new MetaField<>("f20",metaArray(float[].class, float[]::new, metaType(float.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, long[]> f21Field = registerField(new MetaField<>("f21",metaArray(long[].class, long[]::new, metaType(long.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, boolean[]> f22Field = registerField(new MetaField<>("f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, char[]> f23Field = registerField(new MetaField<>("f23",metaArray(char[].class, char[]::new, metaType(char.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, byte[]> f24Field = registerField(new MetaField<>("f24",metaArray(byte[].class, byte[]::new, metaType(byte.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Integer[]> f25Field = registerField(new MetaField<>("f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Short[]> f26Field = registerField(new MetaField<>("f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Double[]> f27Field = registerField(new MetaField<>("f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Float[]> f28Field = registerField(new MetaField<>("f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Long[]> f29Field = registerField(new MetaField<>("f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Boolean[]> f30Field = registerField(new MetaField<>("f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.Character[]> f31Field = registerField(new MetaField<>("f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.String[]> f32Field = registerField(new MetaField<>("f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.lang.String>> f33Field = registerField(new MetaField<>("f33",metaType(java.util.List.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Set<java.lang.String>> f34Field = registerField(new MetaField<>("f34",metaType(java.util.Set.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Collection<java.lang.String>> f35Field = registerField(new MetaField<>("f35",metaType(java.util.Collection.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, io.art.core.collection.ImmutableArray<java.lang.String>> f36Field = registerField(new MetaField<>("f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, io.art.core.collection.ImmutableSet<java.lang.String>> f37Field = registerField(new MetaField<>("f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.function.Supplier<java.lang.String>> f38Field = registerField(new MetaField<>("f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, io.art.core.property.LazyProperty<java.lang.String>> f39Field = registerField(new MetaField<>("f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<int[]>> f40Field = registerField(new MetaField<>("f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<byte[]>> f41Field = registerField(new MetaField<>("f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.lang.String[]>> f42Field = registerField(new MetaField<>("f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<byte[]>[]> f43Field = registerField(new MetaField<>("f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<int[]>[]> f44Field = registerField(new MetaField<>("f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.lang.String[]>[]> f45Field = registerField(new MetaField<>("f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.util.List<java.lang.String>>> f46Field = registerField(new MetaField<>("f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.util.List<java.lang.String>[]>> f47Field = registerField(new MetaField<>("f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.Integer, java.lang.String>> f48Field = registerField(new MetaField<>("f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, java.lang.String[]>> f49Field = registerField(new MetaField<>("f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field = registerField(new MetaField<>("f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field = registerField(new MetaField<>("f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field = registerField(new MetaField<>("f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel> f53Field = registerField(new MetaField<>("f53",metaType(io.art.meta.test.TestingMetaModel.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.time.LocalDateTime> f54Field = registerField(new MetaField<>("f54",metaType(java.time.LocalDateTime.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.time.ZonedDateTime> f55Field = registerField(new MetaField<>("f55",metaType(java.time.ZonedDateTime.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.time.Duration> f56Field = registerField(new MetaField<>("f56",metaType(java.time.Duration.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.ModelEnum> f57Field = registerField(new MetaField<>("f57",metaEnum(io.art.meta.test.TestingMetaModel.ModelEnum.class, io.art.meta.test.TestingMetaModel.ModelEnum::valueOf),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field = registerField(new MetaField<>("f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class)))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.List<io.art.meta.test.TestingMetaModel>> f59Field = registerField(new MetaField<>("f59",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaModel.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Set<io.art.meta.test.TestingMetaModel>> f60Field = registerField(new MetaField<>("f60",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaModel.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Field = registerField(new MetaField<>("f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaModel.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, reactor.core.publisher.Mono<java.lang.String>> f62Field = registerField(new MetaField<>("f62",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, reactor.core.publisher.Flux<java.lang.String>> f63Field = registerField(new MetaField<>("f63",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.util.stream.Stream<java.lang.String>> f64Field = registerField(new MetaField<>("f64",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class)),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, reactor.core.publisher.Mono<java.lang.String[]>> f65Field = registerField(new MetaField<>("f65",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.String> f66Field = registerField(new MetaField<>("f66",metaType(java.lang.String.class),false,this));

              private final MetaField<MetaTestingMetaModelBuilderClass, java.lang.String> f67Field = registerField(new MetaField<>("f67",metaType(java.lang.String.class),false,this));

              private final MetaF1Method f1Method = registerMethod(new MetaF1Method(this));

              private final MetaF2Method f2Method = registerMethod(new MetaF2Method(this));

              private final MetaF3Method f3Method = registerMethod(new MetaF3Method(this));

              private final MetaF4Method f4Method = registerMethod(new MetaF4Method(this));

              private final MetaF5Method f5Method = registerMethod(new MetaF5Method(this));

              private final MetaF6Method f6Method = registerMethod(new MetaF6Method(this));

              private final MetaF7Method f7Method = registerMethod(new MetaF7Method(this));

              private final MetaF8Method f8Method = registerMethod(new MetaF8Method(this));

              private final MetaF9Method f9Method = registerMethod(new MetaF9Method(this));

              private final MetaF10Method f10Method = registerMethod(new MetaF10Method(this));

              private final MetaF11Method f11Method = registerMethod(new MetaF11Method(this));

              private final MetaF12Method f12Method = registerMethod(new MetaF12Method(this));

              private final MetaF13Method f13Method = registerMethod(new MetaF13Method(this));

              private final MetaF14Method f14Method = registerMethod(new MetaF14Method(this));

              private final MetaF15Method f15Method = registerMethod(new MetaF15Method(this));

              private final MetaF16Method f16Method = registerMethod(new MetaF16Method(this));

              private final MetaF17Method f17Method = registerMethod(new MetaF17Method(this));

              private final MetaF18Method f18Method = registerMethod(new MetaF18Method(this));

              private final MetaF19Method f19Method = registerMethod(new MetaF19Method(this));

              private final MetaF20Method f20Method = registerMethod(new MetaF20Method(this));

              private final MetaF21Method f21Method = registerMethod(new MetaF21Method(this));

              private final MetaF22Method f22Method = registerMethod(new MetaF22Method(this));

              private final MetaF23Method f23Method = registerMethod(new MetaF23Method(this));

              private final MetaF24Method f24Method = registerMethod(new MetaF24Method(this));

              private final MetaF25Method f25Method = registerMethod(new MetaF25Method(this));

              private final MetaF26Method f26Method = registerMethod(new MetaF26Method(this));

              private final MetaF27Method f27Method = registerMethod(new MetaF27Method(this));

              private final MetaF28Method f28Method = registerMethod(new MetaF28Method(this));

              private final MetaF29Method f29Method = registerMethod(new MetaF29Method(this));

              private final MetaF30Method f30Method = registerMethod(new MetaF30Method(this));

              private final MetaF31Method f31Method = registerMethod(new MetaF31Method(this));

              private final MetaF32Method f32Method = registerMethod(new MetaF32Method(this));

              private final MetaF33Method f33Method = registerMethod(new MetaF33Method(this));

              private final MetaF34Method f34Method = registerMethod(new MetaF34Method(this));

              private final MetaF35Method f35Method = registerMethod(new MetaF35Method(this));

              private final MetaF36Method f36Method = registerMethod(new MetaF36Method(this));

              private final MetaF37Method f37Method = registerMethod(new MetaF37Method(this));

              private final MetaF38Method f38Method = registerMethod(new MetaF38Method(this));

              private final MetaF39Method f39Method = registerMethod(new MetaF39Method(this));

              private final MetaF40Method f40Method = registerMethod(new MetaF40Method(this));

              private final MetaF41Method f41Method = registerMethod(new MetaF41Method(this));

              private final MetaF42Method f42Method = registerMethod(new MetaF42Method(this));

              private final MetaF43Method f43Method = registerMethod(new MetaF43Method(this));

              private final MetaF44Method f44Method = registerMethod(new MetaF44Method(this));

              private final MetaF45Method f45Method = registerMethod(new MetaF45Method(this));

              private final MetaF46Method f46Method = registerMethod(new MetaF46Method(this));

              private final MetaF47Method f47Method = registerMethod(new MetaF47Method(this));

              private final MetaF48Method f48Method = registerMethod(new MetaF48Method(this));

              private final MetaF49Method f49Method = registerMethod(new MetaF49Method(this));

              private final MetaF50Method f50Method = registerMethod(new MetaF50Method(this));

              private final MetaF51Method f51Method = registerMethod(new MetaF51Method(this));

              private final MetaF52Method f52Method = registerMethod(new MetaF52Method(this));

              private final MetaF53Method f53Method = registerMethod(new MetaF53Method(this));

              private final MetaF54Method f54Method = registerMethod(new MetaF54Method(this));

              private final MetaF55Method f55Method = registerMethod(new MetaF55Method(this));

              private final MetaF56Method f56Method = registerMethod(new MetaF56Method(this));

              private final MetaF57Method f57Method = registerMethod(new MetaF57Method(this));

              private final MetaF58Method f58Method = registerMethod(new MetaF58Method(this));

              private final MetaF59Method f59Method = registerMethod(new MetaF59Method(this));

              private final MetaF60Method f60Method = registerMethod(new MetaF60Method(this));

              private final MetaF61Method f61Method = registerMethod(new MetaF61Method(this));

              private final MetaF62Method f62Method = registerMethod(new MetaF62Method(this));

              private final MetaF63Method f63Method = registerMethod(new MetaF63Method(this));

              private final MetaF64Method f64Method = registerMethod(new MetaF64Method(this));

              private final MetaF65Method f65Method = registerMethod(new MetaF65Method(this));

              private final MetaF66Method f66Method = registerMethod(new MetaF66Method(this));

              private final MetaF67Method f67Method = registerMethod(new MetaF67Method(this));

              private final MetaBuildMethod buildMethod = registerMethod(new MetaBuildMethod(this));

              private MetaTestingMetaModelBuilderClass() {
                super(metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class));
              }

              public static MetaTestingMetaModelBuilderClass testingMetaModelBuilder() {
                return self.get();
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Integer> f1Field() {
                return f1Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Short> f2Field() {
                return f2Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Double> f3Field() {
                return f3Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Float> f4Field() {
                return f4Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Long> f5Field() {
                return f5Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Boolean> f6Field() {
                return f6Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Character> f7Field() {
                return f7Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, Byte> f8Field() {
                return f8Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Integer> f9Field() {
                return f9Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Short> f10Field() {
                return f10Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Double> f11Field() {
                return f11Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Float> f12Field() {
                return f12Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Long> f13Field() {
                return f13Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Boolean> f14Field() {
                return f14Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Character> f15Field() {
                return f15Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.String> f16Field() {
                return f16Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, int[]> f17Field() {
                return f17Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, short[]> f18Field() {
                return f18Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, double[]> f19Field() {
                return f19Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, float[]> f20Field() {
                return f20Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, long[]> f21Field() {
                return f21Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, boolean[]> f22Field() {
                return f22Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, char[]> f23Field() {
                return f23Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, byte[]> f24Field() {
                return f24Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Integer[]> f25Field() {
                return f25Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Short[]> f26Field() {
                return f26Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Double[]> f27Field() {
                return f27Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Float[]> f28Field() {
                return f28Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Long[]> f29Field() {
                return f29Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Boolean[]> f30Field() {
                return f30Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.Character[]> f31Field() {
                return f31Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.String[]> f32Field() {
                return f32Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.lang.String>> f33Field(
                  ) {
                return f33Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Set<java.lang.String>> f34Field(
                  ) {
                return f34Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Collection<java.lang.String>> f35Field(
                  ) {
                return f35Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, io.art.core.collection.ImmutableArray<java.lang.String>> f36Field(
                  ) {
                return f36Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, io.art.core.collection.ImmutableSet<java.lang.String>> f37Field(
                  ) {
                return f37Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.function.Supplier<java.lang.String>> f38Field(
                  ) {
                return f38Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, io.art.core.property.LazyProperty<java.lang.String>> f39Field(
                  ) {
                return f39Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<int[]>> f40Field() {
                return f40Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<byte[]>> f41Field(
                  ) {
                return f41Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.lang.String[]>> f42Field(
                  ) {
                return f42Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<byte[]>[]> f43Field(
                  ) {
                return f43Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<int[]>[]> f44Field(
                  ) {
                return f44Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.lang.String[]>[]> f45Field(
                  ) {
                return f45Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.util.List<java.lang.String>>> f46Field(
                  ) {
                return f46Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<java.util.List<java.lang.String>[]>> f47Field(
                  ) {
                return f47Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.Integer, java.lang.String>> f48Field(
                  ) {
                return f48Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, java.lang.String[]>> f49Field(
                  ) {
                return f49Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Field(
                  ) {
                return f50Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Field(
                  ) {
                return f51Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Field(
                  ) {
                return f52Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel> f53Field(
                  ) {
                return f53Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.time.LocalDateTime> f54Field(
                  ) {
                return f54Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.time.ZonedDateTime> f55Field(
                  ) {
                return f55Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.time.Duration> f56Field() {
                return f56Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.ModelEnum> f57Field(
                  ) {
                return f57Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Field(
                  ) {
                return f58Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.List<io.art.meta.test.TestingMetaModel>> f59Field(
                  ) {
                return f59Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Set<io.art.meta.test.TestingMetaModel>> f60Field(
                  ) {
                return f60Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Field(
                  ) {
                return f61Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, reactor.core.publisher.Mono<java.lang.String>> f62Field(
                  ) {
                return f62Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, reactor.core.publisher.Flux<java.lang.String>> f63Field(
                  ) {
                return f63Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.util.stream.Stream<java.lang.String>> f64Field(
                  ) {
                return f64Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, reactor.core.publisher.Mono<java.lang.String[]>> f65Field(
                  ) {
                return f65Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.String> f66Field() {
                return f66Field;
              }

              public MetaField<MetaTestingMetaModelBuilderClass, java.lang.String> f67Field() {
                return f67Field;
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

              public MetaF62Method f62Method() {
                return f62Method;
              }

              public MetaF63Method f63Method() {
                return f63Method;
              }

              public MetaF64Method f64Method() {
                return f64Method;
              }

              public MetaF65Method f65Method() {
                return f65Method;
              }

              public MetaF66Method f66Method() {
                return f66Method;
              }

              public MetaF67Method f67Method() {
                return f67Method;
              }

              public MetaBuildMethod buildMethod() {
                return buildMethod;
              }

              public final class MetaF1Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Integer> f1Parameter = register(new MetaParameter<>(0, "f1",metaType(int.class)));

                private MetaF1Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f1",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f1((int)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f1((int)(argument));
                }

                public MetaParameter<java.lang.Integer> f1Parameter() {
                  return f1Parameter;
                }
              }

              public final class MetaF2Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Short> f2Parameter = register(new MetaParameter<>(0, "f2",metaType(short.class)));

                private MetaF2Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f2",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f2((short)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f2((short)(argument));
                }

                public MetaParameter<java.lang.Short> f2Parameter() {
                  return f2Parameter;
                }
              }

              public final class MetaF3Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Double> f3Parameter = register(new MetaParameter<>(0, "f3",metaType(double.class)));

                private MetaF3Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f3",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f3((double)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f3((double)(argument));
                }

                public MetaParameter<java.lang.Double> f3Parameter() {
                  return f3Parameter;
                }
              }

              public final class MetaF4Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Float> f4Parameter = register(new MetaParameter<>(0, "f4",metaType(float.class)));

                private MetaF4Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f4",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f4((float)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f4((float)(argument));
                }

                public MetaParameter<java.lang.Float> f4Parameter() {
                  return f4Parameter;
                }
              }

              public final class MetaF5Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Long> f5Parameter = register(new MetaParameter<>(0, "f5",metaType(long.class)));

                private MetaF5Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f5",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f5((long)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f5((long)(argument));
                }

                public MetaParameter<java.lang.Long> f5Parameter() {
                  return f5Parameter;
                }
              }

              public final class MetaF6Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Boolean> f6Parameter = register(new MetaParameter<>(0, "f6",metaType(boolean.class)));

                private MetaF6Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f6",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f6((boolean)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f6((boolean)(argument));
                }

                public MetaParameter<java.lang.Boolean> f6Parameter() {
                  return f6Parameter;
                }
              }

              public final class MetaF7Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Character> f7Parameter = register(new MetaParameter<>(0, "f7",metaType(char.class)));

                private MetaF7Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f7",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f7((char)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f7((char)(argument));
                }

                public MetaParameter<java.lang.Character> f7Parameter() {
                  return f7Parameter;
                }
              }

              public final class MetaF8Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<Byte> f8Parameter = register(new MetaParameter<>(0, "f8",metaType(byte.class)));

                private MetaF8Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f8",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f8((byte)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f8((byte)(argument));
                }

                public MetaParameter<Byte> f8Parameter() {
                  return f8Parameter;
                }
              }

              public final class MetaF9Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Integer> f9Parameter = register(new MetaParameter<>(0, "f9",metaType(java.lang.Integer.class)));

                private MetaF9Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f9",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f9((java.lang.Integer)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f9((java.lang.Integer)(argument));
                }

                public MetaParameter<java.lang.Integer> f9Parameter() {
                  return f9Parameter;
                }
              }

              public final class MetaF10Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Short> f10Parameter = register(new MetaParameter<>(0, "f10",metaType(java.lang.Short.class)));

                private MetaF10Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f10",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f10((java.lang.Short)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f10((java.lang.Short)(argument));
                }

                public MetaParameter<java.lang.Short> f10Parameter() {
                  return f10Parameter;
                }
              }

              public final class MetaF11Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Double> f11Parameter = register(new MetaParameter<>(0, "f11",metaType(java.lang.Double.class)));

                private MetaF11Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f11",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f11((java.lang.Double)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f11((java.lang.Double)(argument));
                }

                public MetaParameter<java.lang.Double> f11Parameter() {
                  return f11Parameter;
                }
              }

              public final class MetaF12Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Float> f12Parameter = register(new MetaParameter<>(0, "f12",metaType(java.lang.Float.class)));

                private MetaF12Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f12",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f12((java.lang.Float)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f12((java.lang.Float)(argument));
                }

                public MetaParameter<java.lang.Float> f12Parameter() {
                  return f12Parameter;
                }
              }

              public final class MetaF13Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Long> f13Parameter = register(new MetaParameter<>(0, "f13",metaType(java.lang.Long.class)));

                private MetaF13Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f13",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f13((java.lang.Long)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f13((java.lang.Long)(argument));
                }

                public MetaParameter<java.lang.Long> f13Parameter() {
                  return f13Parameter;
                }
              }

              public final class MetaF14Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Boolean> f14Parameter = register(new MetaParameter<>(0, "f14",metaType(java.lang.Boolean.class)));

                private MetaF14Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f14",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f14((java.lang.Boolean)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f14((java.lang.Boolean)(argument));
                }

                public MetaParameter<java.lang.Boolean> f14Parameter() {
                  return f14Parameter;
                }
              }

              public final class MetaF15Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Character> f15Parameter = register(new MetaParameter<>(0, "f15",metaType(java.lang.Character.class)));

                private MetaF15Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f15",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f15((java.lang.Character)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f15((java.lang.Character)(argument));
                }

                public MetaParameter<java.lang.Character> f15Parameter() {
                  return f15Parameter;
                }
              }

              public final class MetaF16Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.String> f16Parameter = register(new MetaParameter<>(0, "f16",metaType(java.lang.String.class)));

                private MetaF16Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f16",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f16((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f16((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> f16Parameter() {
                  return f16Parameter;
                }
              }

              public final class MetaF17Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<int[]> f17Parameter = register(new MetaParameter<>(0, "f17",metaArray(int[].class, int[]::new, metaType(int.class))));

                private MetaF17Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f17",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f17((int[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f17((int[])(argument));
                }

                public MetaParameter<int[]> f17Parameter() {
                  return f17Parameter;
                }
              }

              public final class MetaF18Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<short[]> f18Parameter = register(new MetaParameter<>(0, "f18",metaArray(short[].class, short[]::new, metaType(short.class))));

                private MetaF18Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f18",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f18((short[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f18((short[])(argument));
                }

                public MetaParameter<short[]> f18Parameter() {
                  return f18Parameter;
                }
              }

              public final class MetaF19Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<double[]> f19Parameter = register(new MetaParameter<>(0, "f19",metaArray(double[].class, double[]::new, metaType(double.class))));

                private MetaF19Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f19",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f19((double[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f19((double[])(argument));
                }

                public MetaParameter<double[]> f19Parameter() {
                  return f19Parameter;
                }
              }

              public final class MetaF20Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<float[]> f20Parameter = register(new MetaParameter<>(0, "f20",metaArray(float[].class, float[]::new, metaType(float.class))));

                private MetaF20Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f20",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f20((float[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f20((float[])(argument));
                }

                public MetaParameter<float[]> f20Parameter() {
                  return f20Parameter;
                }
              }

              public final class MetaF21Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<long[]> f21Parameter = register(new MetaParameter<>(0, "f21",metaArray(long[].class, long[]::new, metaType(long.class))));

                private MetaF21Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f21",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f21((long[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f21((long[])(argument));
                }

                public MetaParameter<long[]> f21Parameter() {
                  return f21Parameter;
                }
              }

              public final class MetaF22Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<boolean[]> f22Parameter = register(new MetaParameter<>(0, "f22",metaArray(boolean[].class, boolean[]::new, metaType(boolean.class))));

                private MetaF22Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f22",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f22((boolean[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f22((boolean[])(argument));
                }

                public MetaParameter<boolean[]> f22Parameter() {
                  return f22Parameter;
                }
              }

              public final class MetaF23Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<char[]> f23Parameter = register(new MetaParameter<>(0, "f23",metaArray(char[].class, char[]::new, metaType(char.class))));

                private MetaF23Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f23",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f23((char[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f23((char[])(argument));
                }

                public MetaParameter<char[]> f23Parameter() {
                  return f23Parameter;
                }
              }

              public final class MetaF24Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<byte[]> f24Parameter = register(new MetaParameter<>(0, "f24",metaArray(byte[].class, byte[]::new, metaType(byte.class))));

                private MetaF24Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f24",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f24((byte[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f24((byte[])(argument));
                }

                public MetaParameter<byte[]> f24Parameter() {
                  return f24Parameter;
                }
              }

              public final class MetaF25Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Integer[]> f25Parameter = register(new MetaParameter<>(0, "f25",metaArray(java.lang.Integer[].class, java.lang.Integer[]::new, metaType(java.lang.Integer.class))));

                private MetaF25Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f25",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f25((java.lang.Integer[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f25((java.lang.Integer[])(argument));
                }

                public MetaParameter<java.lang.Integer[]> f25Parameter() {
                  return f25Parameter;
                }
              }

              public final class MetaF26Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Short[]> f26Parameter = register(new MetaParameter<>(0, "f26",metaArray(java.lang.Short[].class, java.lang.Short[]::new, metaType(java.lang.Short.class))));

                private MetaF26Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f26",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f26((java.lang.Short[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f26((java.lang.Short[])(argument));
                }

                public MetaParameter<java.lang.Short[]> f26Parameter() {
                  return f26Parameter;
                }
              }

              public final class MetaF27Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Double[]> f27Parameter = register(new MetaParameter<>(0, "f27",metaArray(java.lang.Double[].class, java.lang.Double[]::new, metaType(java.lang.Double.class))));

                private MetaF27Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f27",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f27((java.lang.Double[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f27((java.lang.Double[])(argument));
                }

                public MetaParameter<java.lang.Double[]> f27Parameter() {
                  return f27Parameter;
                }
              }

              public final class MetaF28Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Float[]> f28Parameter = register(new MetaParameter<>(0, "f28",metaArray(java.lang.Float[].class, java.lang.Float[]::new, metaType(java.lang.Float.class))));

                private MetaF28Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f28",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f28((java.lang.Float[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f28((java.lang.Float[])(argument));
                }

                public MetaParameter<java.lang.Float[]> f28Parameter() {
                  return f28Parameter;
                }
              }

              public final class MetaF29Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Long[]> f29Parameter = register(new MetaParameter<>(0, "f29",metaArray(java.lang.Long[].class, java.lang.Long[]::new, metaType(java.lang.Long.class))));

                private MetaF29Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f29",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f29((java.lang.Long[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f29((java.lang.Long[])(argument));
                }

                public MetaParameter<java.lang.Long[]> f29Parameter() {
                  return f29Parameter;
                }
              }

              public final class MetaF30Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Boolean[]> f30Parameter = register(new MetaParameter<>(0, "f30",metaArray(java.lang.Boolean[].class, java.lang.Boolean[]::new, metaType(java.lang.Boolean.class))));

                private MetaF30Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f30",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f30((java.lang.Boolean[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f30((java.lang.Boolean[])(argument));
                }

                public MetaParameter<java.lang.Boolean[]> f30Parameter() {
                  return f30Parameter;
                }
              }

              public final class MetaF31Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.Character[]> f31Parameter = register(new MetaParameter<>(0, "f31",metaArray(java.lang.Character[].class, java.lang.Character[]::new, metaType(java.lang.Character.class))));

                private MetaF31Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f31",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f31((java.lang.Character[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f31((java.lang.Character[])(argument));
                }

                public MetaParameter<java.lang.Character[]> f31Parameter() {
                  return f31Parameter;
                }
              }

              public final class MetaF32Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.String[]> f32Parameter = register(new MetaParameter<>(0, "f32",metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))));

                private MetaF32Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f32",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f32((java.lang.String[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f32((java.lang.String[])(argument));
                }

                public MetaParameter<java.lang.String[]> f32Parameter() {
                  return f32Parameter;
                }
              }

              public final class MetaF33Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<java.lang.String>> f33Parameter = register(new MetaParameter<>(0, "f33",metaType(java.util.List.class,metaType(java.lang.String.class))));

                private MetaF33Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f33",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f33((java.util.List<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f33((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.lang.String>> f33Parameter() {
                  return f33Parameter;
                }
              }

              public final class MetaF34Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Set<java.lang.String>> f34Parameter = register(new MetaParameter<>(0, "f34",metaType(java.util.Set.class,metaType(java.lang.String.class))));

                private MetaF34Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f34",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f34((java.util.Set<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f34((java.util.Set)(argument));
                }

                public MetaParameter<java.util.Set<java.lang.String>> f34Parameter() {
                  return f34Parameter;
                }
              }

              public final class MetaF35Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Collection<java.lang.String>> f35Parameter = register(new MetaParameter<>(0, "f35",metaType(java.util.Collection.class,metaType(java.lang.String.class))));

                private MetaF35Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f35",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f35((java.util.Collection<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f35((java.util.Collection)(argument));
                }

                public MetaParameter<java.util.Collection<java.lang.String>> f35Parameter() {
                  return f35Parameter;
                }
              }

              public final class MetaF36Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f36Parameter = register(new MetaParameter<>(0, "f36",metaType(io.art.core.collection.ImmutableArray.class,metaType(java.lang.String.class))));

                private MetaF36Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f36",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f36((io.art.core.collection.ImmutableArray<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f36((io.art.core.collection.ImmutableArray)(argument));
                }

                public MetaParameter<io.art.core.collection.ImmutableArray<java.lang.String>> f36Parameter(
                    ) {
                  return f36Parameter;
                }
              }

              public final class MetaF37Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f37Parameter = register(new MetaParameter<>(0, "f37",metaType(io.art.core.collection.ImmutableSet.class,metaType(java.lang.String.class))));

                private MetaF37Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f37",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f37((io.art.core.collection.ImmutableSet<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f37((io.art.core.collection.ImmutableSet)(argument));
                }

                public MetaParameter<io.art.core.collection.ImmutableSet<java.lang.String>> f37Parameter(
                    ) {
                  return f37Parameter;
                }
              }

              public final class MetaF38Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter = register(new MetaParameter<>(0, "f38",metaType(java.util.function.Supplier.class,metaType(java.lang.String.class))));

                private MetaF38Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f38",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f38((java.util.function.Supplier<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f38((java.util.function.Supplier)(argument));
                }

                public MetaParameter<java.util.function.Supplier<java.lang.String>> f38Parameter() {
                  return f38Parameter;
                }
              }

              public final class MetaF39Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f39Parameter = register(new MetaParameter<>(0, "f39",metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))));

                private MetaF39Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f39",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f39((io.art.core.property.LazyProperty<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f39((io.art.core.property.LazyProperty)(argument));
                }

                public MetaParameter<io.art.core.property.LazyProperty<java.lang.String>> f39Parameter(
                    ) {
                  return f39Parameter;
                }
              }

              public final class MetaF40Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<int[]>> f40Parameter = register(new MetaParameter<>(0, "f40",metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class)))));

                private MetaF40Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f40",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f40((java.util.List<int[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f40((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<int[]>> f40Parameter() {
                  return f40Parameter;
                }
              }

              public final class MetaF41Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<byte[]>> f41Parameter = register(new MetaParameter<>(0, "f41",metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class)))));

                private MetaF41Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f41",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f41((java.util.List<byte[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f41((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<byte[]>> f41Parameter() {
                  return f41Parameter;
                }
              }

              public final class MetaF42Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<java.lang.String[]>> f42Parameter = register(new MetaParameter<>(0, "f42",metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                private MetaF42Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f42",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f42((java.util.List<java.lang.String[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f42((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.lang.String[]>> f42Parameter() {
                  return f42Parameter;
                }
              }

              public final class MetaF43Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<byte[]>[]> f43Parameter = register(new MetaParameter<>(0, "f43",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(byte[].class, byte[]::new, metaType(byte.class))))));

                private MetaF43Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f43",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f43((java.util.List<byte[]>[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f43((java.util.List[])(argument));
                }

                public MetaParameter<java.util.List<byte[]>[]> f43Parameter() {
                  return f43Parameter;
                }
              }

              public final class MetaF44Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<int[]>[]> f44Parameter = register(new MetaParameter<>(0, "f44",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(int[].class, int[]::new, metaType(int.class))))));

                private MetaF44Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f44",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f44((java.util.List<int[]>[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f44((java.util.List[])(argument));
                }

                public MetaParameter<java.util.List<int[]>[]> f44Parameter() {
                  return f44Parameter;
                }
              }

              public final class MetaF45Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<java.lang.String[]>[]> f45Parameter = register(new MetaParameter<>(0, "f45",metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class))))));

                private MetaF45Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f45",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f45((java.util.List<java.lang.String[]>[])(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f45((java.util.List[])(argument));
                }

                public MetaParameter<java.util.List<java.lang.String[]>[]> f45Parameter() {
                  return f45Parameter;
                }
              }

              public final class MetaF46Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<java.util.List<java.lang.String>>> f46Parameter = register(new MetaParameter<>(0, "f46",metaType(java.util.List.class,metaType(java.util.List.class,metaType(java.lang.String.class)))));

                private MetaF46Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f46",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f46((java.util.List<java.util.List<java.lang.String>>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f46((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.util.List<java.lang.String>>> f46Parameter(
                    ) {
                  return f46Parameter;
                }
              }

              public final class MetaF47Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f47Parameter = register(new MetaParameter<>(0, "f47",metaType(java.util.List.class,metaArray(java.util.List[].class, java.util.List[]::new, metaType(java.util.List.class,metaType(java.lang.String.class))))));

                private MetaF47Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f47",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f47((java.util.List<java.util.List<java.lang.String>[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f47((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<java.util.List<java.lang.String>[]>> f47Parameter(
                    ) {
                  return f47Parameter;
                }
              }

              public final class MetaF48Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Map<java.lang.Integer, java.lang.String>> f48Parameter = register(new MetaParameter<>(0, "f48",metaType(java.util.Map.class,metaType(java.lang.Integer.class),metaType(java.lang.String.class))));

                private MetaF48Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f48",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f48((java.util.Map<java.lang.Integer, java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f48((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.Integer, java.lang.String>> f48Parameter(
                    ) {
                  return f48Parameter;
                }
              }

              public final class MetaF49Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f49Parameter = register(new MetaParameter<>(0, "f49",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                private MetaF49Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f49",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f49((java.util.Map<java.lang.String, java.lang.String[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f49((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, java.lang.String[]>> f49Parameter(
                    ) {
                  return f49Parameter;
                }
              }

              public final class MetaF50Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Parameter = register(new MetaParameter<>(0, "f50",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.util.List.class,metaType(java.lang.String.class)))));

                private MetaF50Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f50",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f50((java.util.Map<java.lang.String, java.util.List<java.lang.String>>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f50((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> f50Parameter(
                    ) {
                  return f50Parameter;
                }
              }

              public final class MetaF51Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Parameter = register(new MetaParameter<>(0, "f51",metaType(java.util.Map.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                private MetaF51Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f51",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f51((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f51((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f51Parameter(
                    ) {
                  return f51Parameter;
                }
              }

              public final class MetaF52Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Parameter = register(new MetaParameter<>(0, "f52",metaType(io.art.core.collection.ImmutableMap.class,metaType(java.lang.String.class),metaArray(java.util.Map[].class, java.util.Map[]::new, metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(java.lang.String.class))))));

                private MetaF52Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f52",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f52((io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f52((io.art.core.collection.ImmutableMap)(argument));
                }

                public MetaParameter<io.art.core.collection.ImmutableMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>[]>> f52Parameter(
                    ) {
                  return f52Parameter;
                }
              }

              public final class MetaF53Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<io.art.meta.test.TestingMetaModel> f53Parameter = register(new MetaParameter<>(0, "f53",metaType(io.art.meta.test.TestingMetaModel.class)));

                private MetaF53Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f53",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f53((io.art.meta.test.TestingMetaModel)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f53((io.art.meta.test.TestingMetaModel)(argument));
                }

                public MetaParameter<io.art.meta.test.TestingMetaModel> f53Parameter() {
                  return f53Parameter;
                }
              }

              public final class MetaF54Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.time.LocalDateTime> f54Parameter = register(new MetaParameter<>(0, "f54",metaType(java.time.LocalDateTime.class)));

                private MetaF54Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f54",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f54((java.time.LocalDateTime)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f54((java.time.LocalDateTime)(argument));
                }

                public MetaParameter<java.time.LocalDateTime> f54Parameter() {
                  return f54Parameter;
                }
              }

              public final class MetaF55Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.time.ZonedDateTime> f55Parameter = register(new MetaParameter<>(0, "f55",metaType(java.time.ZonedDateTime.class)));

                private MetaF55Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f55",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f55((java.time.ZonedDateTime)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f55((java.time.ZonedDateTime)(argument));
                }

                public MetaParameter<java.time.ZonedDateTime> f55Parameter() {
                  return f55Parameter;
                }
              }

              public final class MetaF56Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.time.Duration> f56Parameter = register(new MetaParameter<>(0, "f56",metaType(java.time.Duration.class)));

                private MetaF56Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f56",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f56((java.time.Duration)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f56((java.time.Duration)(argument));
                }

                public MetaParameter<java.time.Duration> f56Parameter() {
                  return f56Parameter;
                }
              }

              public final class MetaF57Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<io.art.meta.test.TestingMetaModel.ModelEnum> f57Parameter = register(new MetaParameter<>(0, "f57",metaEnum(io.art.meta.test.TestingMetaModel.ModelEnum.class, io.art.meta.test.TestingMetaModel.ModelEnum::valueOf)));

                private MetaF57Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f57",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f57((io.art.meta.test.TestingMetaModel.ModelEnum)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f57((io.art.meta.test.TestingMetaModel.ModelEnum)(argument));
                }

                public MetaParameter<io.art.meta.test.TestingMetaModel.ModelEnum> f57Parameter() {
                  return f57Parameter;
                }
              }

              public final class MetaF58Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter = register(new MetaParameter<>(0, "f58",metaType(java.util.Optional.class,metaType(java.util.List.class,metaType(io.art.core.property.LazyProperty.class,metaType(java.lang.String.class))))));

                private MetaF58Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f58",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f58((java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f58((java.util.Optional)(argument));
                }

                public MetaParameter<java.util.Optional<java.util.List<io.art.core.property.LazyProperty<java.lang.String>>>> f58Parameter(
                    ) {
                  return f58Parameter;
                }
              }

              public final class MetaF59Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.List<io.art.meta.test.TestingMetaModel>> f59Parameter = register(new MetaParameter<>(0, "f59",metaType(java.util.List.class,metaType(io.art.meta.test.TestingMetaModel.class))));

                private MetaF59Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f59",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f59((java.util.List<io.art.meta.test.TestingMetaModel>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f59((java.util.List)(argument));
                }

                public MetaParameter<java.util.List<io.art.meta.test.TestingMetaModel>> f59Parameter(
                    ) {
                  return f59Parameter;
                }
              }

              public final class MetaF60Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Set<io.art.meta.test.TestingMetaModel>> f60Parameter = register(new MetaParameter<>(0, "f60",metaType(java.util.Set.class,metaType(io.art.meta.test.TestingMetaModel.class))));

                private MetaF60Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f60",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f60((java.util.Set<io.art.meta.test.TestingMetaModel>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f60((java.util.Set)(argument));
                }

                public MetaParameter<java.util.Set<io.art.meta.test.TestingMetaModel>> f60Parameter(
                    ) {
                  return f60Parameter;
                }
              }

              public final class MetaF61Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Parameter = register(new MetaParameter<>(0, "f61",metaType(java.util.Map.class,metaType(java.lang.String.class),metaType(io.art.meta.test.TestingMetaModel.class))));

                private MetaF61Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f61",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f61((java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f61((java.util.Map)(argument));
                }

                public MetaParameter<java.util.Map<java.lang.String, io.art.meta.test.TestingMetaModel>> f61Parameter(
                    ) {
                  return f61Parameter;
                }
              }

              public final class MetaF62Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f62Parameter = register(new MetaParameter<>(0, "f62",metaType(reactor.core.publisher.Mono.class,metaType(java.lang.String.class))));

                private MetaF62Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f62",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f62((reactor.core.publisher.Mono<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f62((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String>> f62Parameter() {
                  return f62Parameter;
                }
              }

              public final class MetaF63Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f63Parameter = register(new MetaParameter<>(0, "f63",metaType(reactor.core.publisher.Flux.class,metaType(java.lang.String.class))));

                private MetaF63Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f63",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f63((reactor.core.publisher.Flux<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f63((reactor.core.publisher.Flux)(argument));
                }

                public MetaParameter<reactor.core.publisher.Flux<java.lang.String>> f63Parameter() {
                  return f63Parameter;
                }
              }

              public final class MetaF64Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.util.stream.Stream<java.lang.String>> f64Parameter = register(new MetaParameter<>(0, "f64",metaType(java.util.stream.Stream.class,metaType(java.lang.String.class))));

                private MetaF64Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f64",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f64((java.util.stream.Stream<java.lang.String>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f64((java.util.stream.Stream)(argument));
                }

                public MetaParameter<java.util.stream.Stream<java.lang.String>> f64Parameter() {
                  return f64Parameter;
                }
              }

              public final class MetaF65Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f65Parameter = register(new MetaParameter<>(0, "f65",metaType(reactor.core.publisher.Mono.class,metaArray(java.lang.String[].class, java.lang.String[]::new, metaType(java.lang.String.class)))));

                private MetaF65Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f65",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f65((reactor.core.publisher.Mono<java.lang.String[]>)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f65((reactor.core.publisher.Mono)(argument));
                }

                public MetaParameter<reactor.core.publisher.Mono<java.lang.String[]>> f65Parameter(
                    ) {
                  return f65Parameter;
                }
              }

              public final class MetaF66Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.String> f66Parameter = register(new MetaParameter<>(0, "f66",metaType(java.lang.String.class)));

                private MetaF66Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f66",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f66((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f66((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> f66Parameter() {
                  return f66Parameter;
                }
              }

              public final class MetaF67Method extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder> {
                private final MetaParameter<java.lang.String> f67Parameter = register(new MetaParameter<>(0, "f67",metaType(java.lang.String.class)));

                private MetaF67Method(MetaTestingMetaModelBuilderClass owner) {
                  super("f67",metaType(io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.f67((java.lang.String)(arguments[0]));
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object argument) throws Throwable {
                  return instance.f67((java.lang.String)(argument));
                }

                public MetaParameter<java.lang.String> f67Parameter() {
                  return f67Parameter;
                }
              }

              public final class MetaBuildMethod extends InstanceMetaMethod<MetaTestingMetaModelBuilderClass, io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder, io.art.meta.test.TestingMetaModel> {
                private MetaBuildMethod(MetaTestingMetaModelBuilderClass owner) {
                  super("build",metaType(io.art.meta.test.TestingMetaModel.class),owner);
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance,
                    Object[] arguments) throws Throwable {
                  return instance.build();
                }

                @Override
                public Object invoke(
                    io.art.meta.test.TestingMetaModel.TestingMetaModelBuilder instance) throws
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
