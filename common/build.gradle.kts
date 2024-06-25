plugins {
    java
    id("java-library")
    id("maven-publish")
    id("io.spring.dependency-management") version "1.1.4"
    id("org.barfuin.gradle.jacocolog") version "3.1.0"
    id("jacoco")
}

group = "at.ase.respond"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    // Logging
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.springframework.boot:spring-boot-starter-logging:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter:3.3.0")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.0")
    testImplementation("ch.qos.logback:logback-classic:1.5.6")
    testImplementation("org.junit-pioneer:junit-pioneer:1.4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("common-lib") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://reset.inso.tuwien.ac.at/repo/api/v4/projects/${System.getenv("CI_PROJECT_ID")}/packages/maven")
            name = "GitLab"
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }
        }
        mavenLocal()
    }
}

jacoco {
    toolVersion = "0.8.12" // Use the latest version of JaCoCo
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
    jvmArgs("--add-opens", "java.base/java.util=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).apply {
            exclude(
                "at/ase/respond/common/*.class",
                "at/ase/respond/common/exception/**",
                "at/ase/respond/common/dto/**",
                "at/ase/respond/common/event/**"
            )
        }
    }))
}
