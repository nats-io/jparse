plugins {
    `java-library`
    id("me.champeau.jmh") version "0.6.7"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.jsoniter:jsoniter:0.9.23")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    testImplementation("com.jayway.jsonpath:json-path:2.4.0")
    testImplementation("org.noggit:noggit:0.8")
    testImplementation("io.nats:jnats:2.16.8")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}



tasks.named<Test>("test") {
    useJUnitPlatform()
}


jmh {
    warmupIterations.set(1)
    iterations.set(2)
    fork.set(1)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
