/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "2.0.0-Beta3"
	kotlin("plugin.noarg") version "1.9.22"
}

group = "pl.jwizard.api"
version = "1.0.0"

var jvmVersion = JavaVersion.VERSION_17
var jarSnapshotSHA = System.getenv("JAR_SNAPSHOT_SHA") ?: version

java.sourceCompatibility = jvmVersion
java.targetCompatibility = jvmVersion

repositories {
	mavenCentral()
}

noArg {
	annotation("pl.jwizard.api.config.annotation.NoArgConstructor")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.cloud:spring-cloud-vault-config:4.1.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.slf4j:slf4j-api:2.0.11")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("org.apache.commons:commons-lang3:3.14.0")
	implementation("org.modelmapper:modelmapper:3.2.0")
	implementation("com.mysql:mysql-connector-j:8.4.0")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.ehcache:ehcache:3.10.8")
	implementation("javax.cache:cache-api:1.1.1")
	implementation("org.apache.commons:commons-collections4:4.4")
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("com.sun.xml.bind:jaxb-impl:2.3.1")

	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.jetbrains.kotlin:kotlin-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<Delete>("clean") {
	doLast {
		val binDir = file("$projectDir/.bin")
		if (binDir.exists()) {
			binDir.deleteRecursively()
		}
	}
}

tasks.withType<BootJar> {
	destinationDirectory = file("$projectDir/.bin")
	archiveFileName = "jwizard-api-$jarSnapshotSHA.jar"
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = jvmVersion.toString()
	}
}
