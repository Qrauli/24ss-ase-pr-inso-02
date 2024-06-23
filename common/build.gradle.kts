plugins {
    java
    id("java-library")
    id("maven-publish")
}

group = "at.ase.respond"
version = "0.0.1-SNAPSHOT-76"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
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
