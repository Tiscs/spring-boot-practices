import com.google.protobuf.gradle.*

plugins {
    id("sbp-build-kotlin-library-base")
    id("com.google.protobuf")
}

val protobufJavaVersion: String by project
val grpcStubVersion: String by project
val grpcProtobufVersion: String by project
val protocVersion: String by project
val protocGenGrpcJavaVersion: String by project
val protocGenGrpcKotlinVersion: String by project

dependencies {
    implementation("com.google.protobuf:protobuf-java:${protobufJavaVersion}")
    implementation("io.grpc:grpc-stub:${grpcStubVersion}")
    implementation("io.grpc:grpc-protobuf:${grpcProtobufVersion}")
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${protocVersion}" }
    plugins {
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
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
