import org.gradle.kotlin.dsl.withType
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
	id("org.springframework.boot") version "3.4.6"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

group = "ru.vassuv.testmediasofttask"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {
	/** starters */
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter:7.23.0")
	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-webapp:7.23.0")
	implementation("org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-rest:7.23.0")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")


	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.liquibase:liquibase-core")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("com.github.ben-manes.caffeine:caffeine")
	implementation("ru.vassuv:exception-handler-starter:0.0.4-SNAPSHOT")
	implementation("software.amazon.awssdk:s3:2.25.21")
	implementation("software.amazon.awssdk:auth:2.25.21")

	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("com.h2database:h2")

	/** tests */
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("com.h2database:h2")
	testImplementation("io.mockk:mockk:1.14.2")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("org.testcontainers:postgresql:1.19.1")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootJar> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


detekt {
	config.setFrom("config/detekt/detekt.yml")
	buildUponDefaultConfig = true

	source.setFrom("src/main/kotlin", "src/test/kotlin")

	autoCorrect = true // для автоматического исправления простых проблем
}