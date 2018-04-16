# grpc-java-demo

本项目是一个使用`gRPC`的Java实例项目。

下面将一步步介绍本项目的构建过程，以详解对`gRPC`的使用方法。

## 第一步：定义服务

新建模块`grpc-java-proto`用于服务定义。

在模块的pom.xml中添加需要的依赖。

```xml
<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-netty</artifactId>
  <version>1.11.0</version>
</dependency>
<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-protobuf</artifactId>
  <version>1.11.0</version>
</dependency>
<dependency>
  <groupId>io.grpc</groupId>
  <artifactId>grpc-stub</artifactId>
  <version>1.11.0</version>
</dependency>
```

添加插件[protobuf-maven-plugin](https://www.xolstice.org/protobuf-maven-plugin/)用于代码生成。

```xml
<build>
  <extensions>
    <extension>
      <groupId>kr.motd.maven</groupId>
      <artifactId>os-maven-plugin</artifactId>
      <version>1.5.0.Final</version>
    </extension>
  </extensions>
  <plugins>
    <plugin>
      <groupId>org.xolstice.maven.plugins</groupId>
      <artifactId>protobuf-maven-plugin</artifactId>
      <version>0.5.1</version>
      <configuration>
        <protocArtifact>com.google.protobuf:protoc:3.5.1-1:exe:${os.detected.classifier}</protocArtifact>
        <pluginId>grpc-java</pluginId>
        <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.11.0:exe:${os.detected.classifier}</pluginArtifact>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>compile</goal>
            <goal>compile-custom</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

`gRPC`协议使用的数据传输格式是[protocol buffers](https://developers.google.com/protocol-buffers/docs/overview)。

在`src/main`下面建立`proto`目录，`protobuf-maven-plugin`默认会扫描该目录以生成java文件。

新建`helloworld.proto`。[proto3](https://developers.google.com/protocol-buffers/docs/proto3)的具体用法请查看链接文档。

**关于proto语法的一些说明：**

* 可选的`package`说明符：避免多个消息类型的名称冲突，在指定了`package`之后，引用时需要带上`package`。

	```
	package foo.bar;
	message Open { ... }
	```
	```
	message Foo {
	  ...
	  foo.bar.Open open = 1;
	  ...
	}
	```
在Java中，除非在`.proto`文件中明确提供了`optional java_package`，否则该`package`将用作Java包。

**一些常用的option**

* **`java_package`**：要用于生成的Java类的包。如果在`.proto`文件不指定`java_package`，默认使用`.proto`文件中的`package`。

	```
	option java_package = "com.example.foo";
	```

* **`java_multiple_files`**：导致在包级别定义顶级消息，枚举和服务，而不是以.proto文件命名的类中。

	```
	option java_multiple_files = true;
	```

* **`java_outer_classname`**：要生成的最外层Java类的类名。如果在`.proto`文件不指定`java_outer_classname`，类名将通过将`.proto`文件名转换为驼峰风格来构造(`foo_bar.proto`对应于 `FooBar.java`)。

	```
	option java_outer_classname = "Ponycopter";
	```

* **`optimize_for`**：可以设置为`SPEED`,`CODE_SIZE`或`LITE_RUNTIME`，它影响代码生成的方式，默认是`SPEED`。

	```
	option optimize_for = SPEED;
	```

* **`deprecated `**：如果设置为`true`，则表示该字段已弃用且不应被新代码使用，在Java中会变成`@Deprecated`注解。

	```
	int32 old_field = 6 [deprecated=true];
	```

## 第二步、写服务器

新建`grpc-java-server`模块，并提供实现类`GretterImpl`实现`GreeterGrpc.Greeter`接口。另外提供一个gRPC服务器，监听来自客户端的请求并返回服务的响应。

## 第二步、写客户端

新建`grpc-java-client`模块，提供`gRPC`客户端来调用服务。