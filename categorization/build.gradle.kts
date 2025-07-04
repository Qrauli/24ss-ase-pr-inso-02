plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.barfuin.gradle.jacocolog") version "3.1.0"
	id("jacoco")
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
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	testImplementation("org.springframework.security:spring-security-test")

	// OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Mongo
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	// Respond Common Lib
	implementation("at.ase.respond:common-lib:0.1.0")
}

tasks.bootJar {
	archiveFileName.set("${project.name}.jar")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jacoco {
	toolVersion = "0.8.12" // Use the latest version of JaCoCo
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report

	classDirectories.setFrom(files(classDirectories.files.map {
		fileTree(it).apply {
			exclude(
				"at/ase/respond/categorization/persistence/**/*.class",
				"at/ase/respond/categorization/presentation/dto/**/*.class",
				"at/ase/respond/categorization/presentation/mapper/*.class",
				"at/ase/respond/categorization/util/*.class"
			)
		}
	}))
}
