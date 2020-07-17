1、项目pom文件依赖，父级项目spring-boot-starter-parent
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.1.7.RELEASE</version>
         <relativePath/> <!-- lookup parent from repository -->
</parent>

spring-boot-starter-parent-2.1.7.RELEASE.pom这个继承自spring-boot-dependencies，
保存了基本的依赖信息，另外我们也可以看到项目的编码格式，JDK 的版本等信息，数据过滤信息，
版本的定义以及dependencyManagement节点。


2、通过使用scope=import，配置Spring Boot
<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.1.7.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  
这样写之后，依赖的版本号问题虽然解决了，但是关于打包的插件、编译的 JDK 版本、文件的编码格式等等这些配置，
在没有 parent 的时候，这些统统要自己去配置  
