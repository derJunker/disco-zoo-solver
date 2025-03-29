plugins {
    id("java")
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    war
}

group = "junker.disco.solver"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_23
}

tasks.processResources {
    dependsOn("copyFrontend")
}

tasks.register<Copy>("copyFrontend") {
    dependsOn("npmBuild")
    from("./frontend/dist") {
        include("**/*")
    }
    into("/build/resources/main/static")
}

tasks.register("npmBuild") {
    exec {
        errorOutput = System.err
        workingDir = file("./frontend")
        commandLine ("cmd", "/c", "npm", "run", "build")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")


    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.slf4j:slf4j-api:2.0.17")

    compileOnly ("ch.qos.logback:logback-classic:1.5.18")
    compileOnly ("ch.qos.logback:logback-core:1.5.18")

    implementation("org.apache.logging.log4j:log4j-api:2.24.3")
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.24.3")

    runtimeOnly("com.h2database:h2:2.3.232")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootWar {
    archiveFileName="disco-solver#api.war"
}