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
	mavenCentral()
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
		target("src/**/*.java")
		toggleOffOn()
		palantirJavaFormat()
		importOrder("java", "javax", "org", "com", "at", )
		removeUnusedImports()
		trimTrailingWhitespace()
	}
}
