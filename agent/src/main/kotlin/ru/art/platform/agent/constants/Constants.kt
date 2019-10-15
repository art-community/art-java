package ru.art.platform.agent.constants

object CommonConstants {

}


object JvmConstants {
    const val DEFAULT_JVM_OPTIONS = "-server -XX:GCTimeRatio=2 -Xms1g -Xmx1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=90 -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=30 -XX:+AggressiveOpts -XX:+UseTLAB -XX:CompileThreshold=100 -XX:ThreadStackSize=4096 -XX:+UseFastAccessorMethods -XX:MaxTenuringThreshold=5 -XX:ReservedCodeCacheSize=256m"
}