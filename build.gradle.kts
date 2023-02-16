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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    testImplementation("com.jayway.jsonpath:json-path:2.4.0")
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
