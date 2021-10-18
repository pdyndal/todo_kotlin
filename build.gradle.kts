import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("nu.studer.jooq") version "6.0"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("kapt") version "1.5.21"
}

group = "com.pady"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq:2.5.5")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("org.flywaydb:flyway-core:8.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.h2database:h2:1.4.200")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.5")
    testImplementation("io.rest-assured:kotlin-extensions:4.4.0")
    testImplementation("com.atlassian.oai:swagger-request-validator-restassured:2.20.0")
    jooqGenerator("org.jooq:jooq-meta-extensions:3.15.2")

    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Wrapper> {
    gradleVersion = "7.1.1"
}

sourceSets {
    main {
        java {
            srcDir("src/generated-jooq/kotlin")
        }
    }
}

//https://github.com/etiennestuder/gradle-jooq-plugin
jooq {
    version.set("3.15.1")
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                generator.apply {
                    name =
                        "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name =
                            "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties.add(
                            org.jooq.meta.jaxb.Property().withKey("scripts")
                                .withValue("src/main/resources/db/migration")
                        )
                        properties.add(
                            org.jooq.meta.jaxb.Property().withKey("sort")
                                .withValue("flyway")
                        )
                        properties.add(
                            org.jooq.meta.jaxb.Property().withKey("unqualifiedSchema")
                                .withValue("none")
                        )
                        properties.add(
                            org.jooq.meta.jaxb.Property().withKey("defaultNameCase")
                                .withValue("as_is")
                        )
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isPojos = false
                        isImmutablePojos = false
                        isFluentSetters = false
                        isGeneratedAnnotation = false
                    }
                    target.apply {
                        packageName =
                            "com.pady.todo.jooq"
                        directory =
                            "${project.buildDir}/generated-jooq"
                    }
                    strategy.name =
                        "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.getByName("generateJooq") {
    doLast {
        delete("${project.projectDir}/src/generated-jooq/kotlin")
        copy {
            from("${project.buildDir}/generated-jooq")
            into("${project.projectDir}/src/generated-jooq/kotlin")
        }
        delete("${project.buildDir}/generated-jooq")
    }
}