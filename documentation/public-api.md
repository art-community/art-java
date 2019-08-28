# Public API Specification

## Configuration API

### ConfigProvider
```java
Config commonConfig()
Config commonConfig(ConfigType configType)
Config config(String configId)
Config config(String configId, ConfigType configType)
```
### AgileConfigurationsActivator
```java
useAgileConfigurations()
useAgileConfigurations(String moduleId)
useRemoteAgileConfigurations()
useRemoteAgileConfigurations(String mainModuleId) 
```
### RemoteConfigProvider
```java
useRemoteConfigurations()
useRemoteConfigurations(ContextInitialConfiguration contextInitialConfiguration)
Config remoteConfig(String sectionId)
Config remoteConfig()
```
### GrpcConfigProvider
```java
Config grpcServerConfig()
Config grpcCommunicationConfig()
```
### GrpcConfigProvider
```java
Config grpcServerConfig()
Config grpcCommunicationConfig()
```
### HttpConfigProvider
```java
Config httpServerConfig()
Config httpCommunicationConfig()
```
### KafkaConfigProvider
```java
Config kafkaBrokerConfig()
Config kafkaConsumerConfig()
Config kafkaProducerConfig()
```
### LoggingConfigProvider
```java
Config loggingConfig()
```
### MetricsConfigProvider
```java
Config metricsConfig()
```
### MetricsConfigProvider
```java
Config networkManagerConfig()
```
### RocksDbConfigProvider
```java
Config rocksDbConfig()
```
### RsocketConfigProvider
```java
Config rsocketConfig()
```
### ServiceConfigProvider
```java
CircuitBreakerServiceConfig getCircuitBreakerServiceConfig(String sectionId)
RateLimiterServiceConfig getRateLimiterServiceConfig(String sectionId)
BulkheadServiceConfig getBulkheadServiceConfig(String sectionId)
RetryServiceConfig getRetryServiceConfig(String sectionId)
DeactivationConfig getServiceDeactivationConfig(String serviceId)
Config serviceConfig(String serviceId)
Config serviceConfig()
```
### SqlConfigProvider
```java
Config sqlDbConfig()
```
### TarantoolConfigProvider
```java
Config tarantoolConfig()
```
### ConfigExtensions
```java
String configString(String sectionId, String path)
Integer configInt(String sectionId, String path)
Long configLong(String sectionId, String path)
Double configDouble(String sectionId, String path)
Boolean configBoolean(String sectionId, String path)
List<String> configStringList(String sectionId, String path)
List<Integer> configIntList(String sectionId, String path)
List<Long> configLongList(String sectionId, String path)
List<Double> configDoubleList(String sectionId, String path)
List<Boolean> configBooleanList(String sectionId, String path)
String configString(String path)
Integer configInt(String path)
Long configLong(String path)
Double configDouble(String path)
Boolean configBoolean(String path)
List<String> configStringList(String path)
List<Integer> configIntList(String path)
List<Long> configLongList(String path)
List<Double> configDoubleList(String path)
List<Boolean> configBooleanList(String path)
String configString(String sectionId, String path, String defaultValue)
Integer configInt(String sectionId, String path, int defaultValue)
Double configDouble(String sectionId, String path, double defaultValue)
Long configLong(String sectionId, String path, long defaultValue)
Boolean configBoolean(String sectionId, String path, boolean defaultValue)
List<String> configStringList(String sectionId, String path, List<String> defaultValue)
List<Integer> configIntList(String sectionId, String path, List<Integer> defaultValue)
List<Double> configDoubleList(String sectionId, String path, List<Double> defaultValue)
List<Long> configLongList(String sectionId, String path, List<Long> defaultValue)
List<Boolean> configBooleanList(String sectionId, String path, List<Boolean> defaultValue)
Config configInner(String sectionId, String path)
Config configInner(String path)
Map<String, T> configMap(String path, Function<Config, T> configMapper)
Map<String, T> configMap(String path, BiFunction<String, Config, T> configMapper)
Map<String, Config> configMap(String path)
<T> Map<String, T> configMap(String sectionId, String path, Function<Config, T> configMapper)
Map<String, Config> configMap(String sectionId, String path)
<T> Map<String, T> configMap(String path, Function<Config, T> configMapper, Map<String, T> defaultValues)
<T> Map<String, T> configMap(String path, BiFunction<String, Config, T> configMapper, Map<String, T> defaultValues)
<T> Map<String, T> configMap(String sectionId, String path, Function<Config, T> configMapper, Map<String, T> defaultValues)
boolean hasPath(String sectionId, String path)
boolean hasPath(String path)
Properties configProperties(String sectionId, String key)
```

## Core API and Java extensions
### Caster
```java
<T> T cast(Object obj)
```
### CheckerForEmptiness
```java
boolean isNotEmpty(something)
boolean isEmpty(something)
<T> T ifEmpty(checkingValue, alternative)
```
### Constants:
* ArrayConstants
* BufferConstants
* BuilderValidatorErrors
* CharConstants
* ContextConstants
* DateConstants
* ExceptionMessages
* InterceptionStrategy
* LoggingMessages
* MimeTypeConstants
* MimeTypeExceptionFormat
* NetworkConstants
* SizesConstants
* StreamConstants
* StringConstants
* SystemConstants
* SystemNamePatterns
* SystemProperties
* ThreadConstants
### Context
```java
contextConfiguration()
withDefaultContext()
withDefaultContext(ContextInitialConfiguration configuration)
getModule(String id, Supplier<M> ifNotLoadedCreator)
getModuleState(String id, Supplier<M> ifNotLoadedCreator)
getModule(String id, Module ifNotLoaded)
getModuleState(String id, Module ifNotLoaded)
loadModule(Module module)
loadModule(Module module, ModuleConfigurator configurator)
loadModule(Module module, ModuleConfiguration customConfiguration)
overrideModule(String id, ModuleConfiguration customConfiguration)
reloadModule(String id)
refreshModule(String id)
refreshAndReloadModule(String id)
refreshAndReloadModules()
reloadModules()
refreshModules()
getModuleNames()
hasModule(String id)
initialized()
initDefaultContext(ContextInitialConfiguration configuartion)
defaultContext()
ready()
constructInsideDefaultContext(Supplier<C> constructor)
constructInsideDefaultContext(ContextInitialConfiguration configuartion, Supplier<C> constructor)
outsideDefaultContext(Consumer<Context> action)
context()
recreateContext(ContextInitialConfiguration contextInitialConfiguration)
initContext(ContextInitialConfiguration contextInitialConfiguration)
```
### WslPathConverter
```java
convertToWslPath(String windowsPath)
```
### SystemDeterminant
```java
isWindows()
isMac()
isUnix()
isSolaris()
```
### Extensions
* ArraysExtensions
* CollectionExtensions
* DateExtensions
* DateTimeExtensions
* EqualsCheckingExtensions
* ExceptionExtensions
* FileExtensions
* InputOutputStreamExtensions
* InputStreamExtensions
* NullCheckingExtensions
* OptionalExtensions
* StringExtensions
* ThreadExtensions
### CollectionsFactory
```java
mapOf(K k, V v)
mapOf()
mapOf(Map<K, V> map)
mapOf(int size)
concurrentHashMap()
concurrentHashMap(Map<K, V> map)
fixedArrayOf(Collection<T> elements)
fixedArrayOf(T... elements)
arrayOf(int size)
fixedArrayOf(Stream<T> stream)
dynamicArrayOf(T... elements)
dynamicArrayOf(Collection<T> elements)
linkedListOf(T... elements)
linkedListOf(Collection<T> elements)
dequeOf(Collection<T> elements)
dequeOf(T... elements)
queueOf(T... elements)
priorityQueueOf(Comparator<T> comparator, T... elements)
priorityQueueOf(Comparator<T> comparator, Collection<T> elements)
stackOf(T... elements)
setOf(T... elements)
setOf(Collection<T> elements)
treeOf(Comparator<T> comparator, T... elements)
treeOf(T... elements)
treeOf(Collection<T> elements, Comparator<T> comparator)
pairOf(K k, V v)
```
### CauseHandler
```java
CauseHandler handleCause(Throwable outer)
```
### JarExtensions
```java
insideJar()
extractCurrentJar()
extractCurrentJar(String directory)
extractJar(String jarPath)
extractJar(String jarPath, String directory)
extractCurrentJarEntry(String entryRegex)
extractCurrentJarEntry(String entryRegex, String directory)
extractJarEntry(String jarPath, String entryRegex)
extractJarEntry(String jarPath, String entryRegex, String directory)
```
### IpAddressProvider
```java
String getIpAddress()
```
### PortSelector
```java
int findAvailableTcpPort()
int findAvailableTcpPort(int minPort)
int findAvailableTcpPort(int minPort, int maxPort)
Set<Integer> findAvailableTcpPorts(int numRequested)
Set<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort)
int findAvailableUdpPort()
int findAvailableUdpPort(int minPort)
int findAvailableUdpPort(int minPort, int maxPort)
Set<Integer> findAvailableUdpPorts(int numRequested)
Set<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort)
```
### Replacer
```java
replaceWith(T current, T from, T to)
```
### ExceptionWrapper
```java
wrapException(Runnable action)
wrapException(Callable<T> action)
ignoreException(ExceptionRunnable action)
ignoreException(ExceptionRunnable action, Consumer<Throwable> onException)
ignoreException(ExceptionCallable<T> action, Function<Throwable, T> onException)
wrapException(Runnable action, ExceptionFactory<?> exceptionFactory)
wrapException(Callable<T> action, ExceptionFactory<?> exceptionFactory)
```

## Value API

### Value
```java

```