import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.jmongard.git-semver-plugin") version "0.4.2"
    id("com.google.cloud.tools.jib") version "3.2.0"
    id("com.adarshr.test-logger") version "3.2.0"
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "net.liccioni"
version = semver.version
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.1"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

testlogger {
    theme = MOCHA
}

jib.to {
    image = "liccioni/gateway"
    tags = setOf("latest", project.version.toString())
}

task("myTask"){
    println("Project version: ${project.version}")
    println("Semver: ${semver.semVersion}")
    println("Semver Info: ${semver.infoVersion}")
}
