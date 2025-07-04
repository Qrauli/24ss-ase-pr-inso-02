plugins {
	java
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.diffplug.spotless") version "6.25.0"
}

group = "at.ase.respond"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	maven {
		url = uri("https://reset.inso.tuwien.ac.at/repo/api/v4/projects/${System.getenv("CI_PROJECT_ID")}/packages/maven")
		name = "GitLab"
		credentials(HttpHeaderCredentials::class) {
			name = if(System.getenv("CI") != null) "Job-Token" else "Private-Token"
			value = System.getenv("CI_JOB_TOKEN")
		}
		authentication {
			create("header", HttpHeaderAuthentication::class)
		}
	}
}

dependencies {
	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Rabbit
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	testImplementation("org.springframework.amqp:spring-rabbit-test")

	// Respond Common Lib
	implementation("at.ase.respond:common-lib:0.0.1-SNAPSHOT-76")
}

buildscript {
	repositories {
		mavenLocal()
		mavenCentral()
	}
}

tasks.bootJar {
	archiveFileName.set("${project.name}.jar")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

spotless {
	java {
		importOrder("java", "javax", "org", "com", "at")
		removeUnusedImports()
		trimTrailingWhitespace()
	}
}
