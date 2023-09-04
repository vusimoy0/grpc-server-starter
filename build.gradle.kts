import com.google.protobuf.gradle.id

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

//gRPC
val protobuf_version =  "1.56.1"
val grpc_kt_version = "1.3.0"
val protoc_version = "3.23.4"
val proto_util_version = "3.22.3"
val grpc_version = "1.55.1"
val tomcat_annotations_version = "6.0.53"

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
    id("idea")
    id("com.google.protobuf") version "0.9.4"
}

group = "dev.vusi"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.cio.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-cio-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //grpc deps
    implementation("io.grpc:grpc-protobuf:$protobuf_version")
    implementation("io.grpc:protoc-gen-grpc-kotlin:$grpc_kt_version")
    implementation("com.google.protobuf:protobuf-java-util:$proto_util_version")
    implementation("io.grpc:grpc-kotlin-stub:$grpc_kt_version")
    implementation("io.grpc:grpc-stub:$grpc_version")
    compileOnly("org.apache.tomcat:annotations-api:$tomcat_annotations_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

idea {
    module {
        sourceDirs.plusAssign(file("src/main/proto"))
    }
}

protobuf {

    protoc {
        artifact = "com.google.protobuf:protoc:$protoc_version"
    }
    plugins {

        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpc_version"
        }

        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpc_kt_version:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

