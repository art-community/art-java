plugins {
    kotlin("jvm") version "1.4.20"
}

dependencies {
    implementation(project(":model"))
    implementation(project(":value"))
    implementation(project(":json"))
    implementation(project(":protobuf"))
    implementation(project(":message-pack"))
    implementation(project(":xml"))
    implementation(project(":core"))
    implementation(project(":logging"))
    implementation(project(":scheduler"))
    implementation(project(":server"))
    implementation(project(":communicator"))
    implementation(project(":configurator"))
}
