# grpc android

1. 添加protobuf的gradle插件

        classpath "com.google.protobuf:protobuf-gradle-plugin:0.8.3"//protobuf插件

2. 添加protobuf依赖

         //protobuf依赖
        compile group: 'io.grpc', name: 'grpc-okhttp', version: '1.9.0'
        compile group: 'io.grpc', name: 'grpc-protobuf-lite', version: '1.9.0'
        compile group: 'io.grpc', name: 'grpc-stub', version: '1.9.0'
        compile group: 'javax.annotation', name: 'javax.annotation-api', version: '1.3.1'

3. 启用protobuf插件

        apply plugin: 'com.google.protobuf'//启用protobuf插件

4. 添加protobuf task

        //protobuf
        protobuf {
            protoc {
                artifact = 'com.google.protobuf:protoc:3.5.1'
            }
            plugins {
                javalite {
                    artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0"
                }
                grpc {
                    artifact = 'io.grpc:protoc-gen-grpc-java:1.9.0'
                }
            }
            generateProtoTasks {
                all().each { task ->
                    task.plugins {
                        javalite {}
                        grpc {
                            // Options added to --grpc_out
                            option 'lite'
                        }
                    }
                }
            }
        }

5. 将proto文件拷贝到src/main/proto

6. build生成java文件

7. 使用示例看yz.grpc.android.MainActivity