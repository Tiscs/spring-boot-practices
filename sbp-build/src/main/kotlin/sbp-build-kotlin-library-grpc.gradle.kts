import com.google.protobuf.gradle.*

plugins {
    id("sbp-build-kotlin-library-base")
    id("com.google.protobuf")
}

val protocVersion: String by project
val protocGenValidateVersion: String by project
val protocGenGrpcJavaVersion: String by project
val protocGenGrpcKotlinVersion: String by project
val kotlinxCoroutinesVersion: String by project
val protobufJavaVersion: String by project
val protobufKotlinVersion: String by project
val protobufValidateVersion: String by project
val grpcCoreVersion: String by project
val grpcStubVersion: String by project
val grpcProtobufVersion: String by project
val grpcKotlinStubVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${kotlinxCoroutinesVersion}")
    implementation("com.google.protobuf:protobuf-java:${protobufJavaVersion}")
    implementation("com.google.protobuf:protobuf-kotlin:${protobufKotlinVersion}")
    implementation("build.buf.protoc-gen-validate:pgv-java-stub:${protobufValidateVersion}")
    implementation("io.grpc:grpc-core:${grpcCoreVersion}")
    implementation("io.grpc:grpc-stub:${grpcStubVersion}")
    implementation("io.grpc:grpc-protobuf:${grpcProtobufVersion}")
    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinStubVersion}")
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${protocVersion}" }
    plugins {
        id("pgv") {
            artifact = "build.buf.protoc-gen-validate:protoc-gen-validate:${protocGenValidateVersion}"
        }
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${protocGenGrpcJavaVersion}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${protocGenGrpcKotlinVersion}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("pgv") { option("lang=java") }
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
