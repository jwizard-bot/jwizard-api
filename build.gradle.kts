/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

var jvmVersion = JavaVersion.VERSION_17

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "2.0.0-Beta3"
	kotlin("plugin.noarg") version "1.9.22"
}

group = "pl.jwizard.api"
version = "1.0.0"

java.sourceCompatibility = jvmVersion
java.targetCompatibility = jvmVersion

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

noArg {
	annotation("pl.jwizard.api.config.annotation.NoArgConstructor")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.cloud:spring-cloud-vault-config:4.1.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.slf4j:slf4j-api:2.0.11")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("org.apache.commons:commons-lang3:3.14.0")
	implementation("org.modelmapper:modelmapper:3.2.0")
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
	compileOnly("io.mongock:mongock-springboot-v3:5.4.2")
	compileOnly("io.mongock:mongodb-springdata-v4-driver:5.4.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.jetbrains.kotlin:kotlin-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootJar> {
	archiveFileName = "jwizard-api.jar"
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = jvmVersion.toString()
	}
}
