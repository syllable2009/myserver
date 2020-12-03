Docker是容器技术的一个代表，容器的技术从本质上讲是程序打包、隔离的技术，不是一个很新的技术，核心的技术在内核中已经存在很久了。
解决环境配置隔离的难题，
方案一：虚拟机：资源占用多，冗余步骤多，启动慢
方案二：Linux 容器，Linux 容器不是模拟一个完整的操作系统，而是对进程进行隔离，容器是进程级别的，相比虚拟机有很多优势。
启动快，资源占用少，体积小
Docker 属于 Linux 容器的一种封装，提供简单易用的容器使用接口。它是目前最流行的 Linux 容器解决方案和代表。
docker和虚拟机是有非常多的相似点。它们都是将应用放在相对独立的环境里，调度主机的内核来运行。
在宿主机上有一个Docker engine，然后再运行各种各样的应用程序。因为它没有虚拟机层，它会比虚拟机轻量很多，包括程序运行速度也非常的快。
使用Docker，扩展应用程序的过程就是启动新的可执行文件，而不是运行繁重的VM主机.
Docker 将应用程序与该程序的依赖，打包在一个文件里面。运行这个文件，就会生成一个虚拟容器。程序在这个虚拟容器里运行，就好像在真实的物理机上运行一样。有了 Docker，就不用担心环境问题。

#List Docker CLI commands
docker
docker container --help

# Display Docker version and info
docker -v  返回安装的docker的版本信息，执行这个命令不需要docker守护进程已经启动
docker --version|version 返回安装的docker的版本信息，分客户端和服务器,执行这个命令不需要docker守护进程已经启动
docker info 查看docker环境的信息

# Execute Docker image
docker run hello-world


image(镜像)   ->   容器实例
docker image ls    docker container ls



# 镜像操作
# 列出本机的所有 image 文件。
只有下载后，镜像才会保存在本地（docker环境所在的主机），通过如下命令可以查看本地已经存在的镜像。
dokcer images = docker image ls 命令可带参数，是个字符串，可以带通配，只显示过滤的镜像。
image 是二进制文件。实际开发中，一个 image 文件往往通过继承另一个 image 文件，加上一些个性化设置而生成。举例来说，你可以在 Ubuntu 的 image 基础上，往里面加入 Apache 服务器，形成你的 image。
本地的镜像是从远程镜像中心（Registry）下载到本地的，默认的镜像中心是docker公司负责运营的docker hub中心。镜像是保存在仓库（Repository）中，仓库存在镜像中心（Registry）中。
docker hub中有两种类型的镜像：用户仓库（user repository）和顶层仓库（top-level  repository）。用户仓库的镜像是有docker用户创建的。顶层仓库是有docker公司内部管理的。(也可以搭建私服的镜像中心，供企业内部使用。)
每个镜像都有一个唯一的ID号。 一个仓库（如上面的ubuntu）中有多个镜像，通过tag标识来区分不同镜像，一个仓库中的镜像通常是同一种类型的镜像，只是不同版本的区别。换句话说，镜像时由仓库名和tag标识来共同决定的。
镜像名称的标准格式是：用户仓库的命名由用户名和仓库名两部分组成（中间用/分隔）；而顶层仓库只有仓库名，如ubuntu仓库。用户名/仓库名:Tag名，对于顶层的则没有用户名，如果省略tag名，默认为latest.
# 在docker hub中心查找仓库
docker search mysql
# 下载镜像
docker pull  [用户名/]仓库名[:TAG]  命令只下载镜像，不创建容器。如果只指定仓库名，默认下载的是 latest标识的镜像，如果还指定了TAG值，则会下载指定的镜像
docker run [用户名/]仓库名[:TAG]  创建容器，如果指定的镜像在本机不存在，则会先去下载镜像。只指定仓库名，不指定tag时，默认下载的是 latest标识的镜像。
# 删除 image 文件
$ docker image rm = docker rmi [镜像名/ID]
# 镜像的复制
docker tag [OPTIONS] orignIMAGE[:TAG] [REGISTRYHOST/][USERNAME/]newNAME[:TAG]
把一个已有的镜像 加入（也就是复制）到别的仓库中，这样相当于改个名，但实际是拷贝一份。

# 容器操作
# List Docker containers (running, all, all in quiet mode)
docker ps -aq=docker container ls -aq  查看dokcer主机上已经创建的容器:-a表示列出所有容器（包括停止运行的容器），否则只会列出运行中的容器。 -q表示只返回容器ID信息，其它容器信息（如状态、对应的镜像等）不显示。
docker [container] run hello-world 
docker [container] run 创建容器。如果发现本地没有指定的 image 文件，就会从仓库自动抓取
上面停止运行，容器自动终止，有些容器不会自动终止，因为提供的是服务，比如，安装运行 Ubuntu 的 image，就可以在命令行体验 Ubuntu 系统
docker run -it ubuntu /bin/bash 其中-i -t 表示创建一个提供交互式shell的容器。
docker run --name mydaemon -d ubuntu /bin/sh -c "while true;do echo hello world;sleep 1;done" 创建守护式容器
注意:每个容器都有一个唯一的ID，作为容器的标识。每个容器也有个唯一的名称，在用docker run命令创建时可以通过 --name 名称 来指定，如果不指定，系统会自动产生一个名称。
docker run [--name  mycontainer]  -i -t ubuntu /bash
docker [container] kill [containID] 对于那些不会自动终止的容器，必须使用docker [container] kill 命令手动终止。
image 文件生成的容器实例，本身也是一个文件，称为容器文件，也就是说，一旦容器生成，就会同时存在两个文件： image 文件和容器文件。而且关闭容器并不会删除容器文件，只是容器停止运行而已。
docker []container] rm [containerID] 终止运行的容器文件，依然会占据硬盘空间，可以使用docker []container] rm命令删除。

#创建镜像
1.commit创建
docker commit old /newName[:TAG]
2.根据Dockerfile文件生成二进制的 image 文件,Dockerfile文件中，是一系列的指令组成。每条指令，包括指令名（必须大写）和指令所需的参数
cd root
首先，在项目的根目录下，新建一个文本文件.dockerignore，写入下面的内容。
.git
node_modules
npm-debug.log
上面代码表示，这三个路径要排除，不要打包进入 image 文件。如果你没有路径要排除，这个文件可以不新建。
然后，在项目的根目录下，新建一个文本文件 Dockerfile，写入下面的内容。
# version：0.0.1 文件的第一行可以是一条注释，在dockfile文件中，以#开头的表示注释。
FROM ubuntu  FROM指令，用于指定基础镜像。是所有dockerfile的第一条命令。因为所有新的镜像都会基于该基础镜像基础上变化来的。
MAINTAINER XXX "xxx@qq.com" MAINTAINER指令，是标识镜像的作者和联系方式（这里是电子邮件），以方便镜像使用者和作者联系。
RUN echo hello1 > test1.txt
RUN echo hello2 > /test2.txt
RUN  apt-get update  用于在容器中执行参数指定的命令
RUN  apt-get install -y nginx
RUN  echo 'hello, i am a web image'  > /usr/share/nginx/html/index.html
EXPOSE 80  将容器80端口暴露出来， 允许外部连接这个端口。
EXPOSE 81
CMD ["/bin/bash"] CMD指令指定容器启动时要执行的命令,这样就省去了在docker run中写命令了,dockerfile中可以有多条cmd命令，但只是最后一条有效,一般写成 字符串数组的方式,CMD  ["echo","hello world"]
                  我们仍然可以在docker run命令中带上容器启动时执行的命令，这会覆盖dockerfile中的CMD指令指定的命令

通过docker build命令运行dockerfile文件，最后生成需要的镜像 docker build -t="jene/myweb:0.0.1" .
上面代码中，-t参数用来指定 image 文件的名字，后面还可以用冒号指定标签。如果不指定，默认的标签就是latest。最后的那个点表示 Dockerfile 文件所在的路径，上例是当前路径，所以是一个点。

运行应用程序，使用-p将机器的端口4000映射到容器的已发布端口80：
$ docker container run -p 8000:80 -it koa-demo /bin/bash
或者
$ docker container run -p 8000:80 -it koa-demo:0.0.1 /bin/bash
note:
-p参数：容器的 3000 端口映射到本机的 8000 端口。docker容器在启动的时候，如果不指定端口映射参数，在容器外部是无法通过网络来访问容器内的网络应用和服务的。
-P 将容器内部开放的网络端口随机映射到宿主机的一个端口上，-p HOSTPORT:CONTAINERPORT 指定要映射的端口，一个指定端口上只可以绑定一个容器，docker run -it -d -p 127.0.0.1::4000 docker.io/centos:latest /bin/bash
-it参数：容器的 Shell 映射到当前的 Shell，然后你在本机窗口输入的命令，就会传入容器。
koa-demo:0.0.1：image 文件的名字（如果有标签，还需要提供标签，默认是 latest 标签）。
/bin/bash：容器启动以后，内部第一个执行的命令。这里是启动 Bash，保证用户可以使用 Shell。

docker container start命令，它用来启动已经生成、已经停止运行的容器文件,docker container run命令是新建容器，每运行一次，就会新建一个容器
docker container kill命令终止容器运行，相当于向容器里面的主进程发出 SIGKILL 信号。而docker container stop命令也是用来终止容器运行，相当于向容器里面的主进程发出 SIGTERM 信号，然后过一段时间再发出 SIGKILL 信号。
这两个信号的差别是，应用程序收到 SIGTERM 信号以后，可以自行进行收尾清理工作，但也可以不理会这个信号。如果收到 SIGKILL 信号，就会强行立即终止，那些正在进行中的操作会全部丢失

（3）docker container logs
docker container logs命令用来查看 docker 容器的输出，即容器里面 Shell 的标准输出。如果docker run命令运行容器的时候，没有使用-it参数，就要用这个命令查看输出。
$ docker container logs [containerID]
（4）docker container exec = docker attach
docker container exec命令用于进入一个正在运行的 docker 容器。如果docker run命令运行容器的时候，没有使用-it参数，就要用这个命令进入容器。一旦进入了容器，就可以在容器的 Shell 执行命令了。
docker exec -it 80e2791b519d /bin/bash
$ docker container exec -it [containerID] /bin/bash
（5）docker container cp
docker container cp命令用于从正在运行的 Docker 容器里面，将文件拷贝到本机。下面是拷贝到当前目录的写法。
$ docker container cp [containID]:[/path/to/file] .

#jib构建你的镜像
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>0.9.6</version>
    <configuration>
        <from>
            <!--base image-->
            <image>openjdk:alpine</image>
        </from>
        <to>
            <!--<image>registry.cn-hangzhou.aliyuncs.com/m65536/jibtest</image>-->
            <!--目标镜像registry地址，为了方便测试，你需要换成自己的地址，如果你的网络不好，可以选用国内加速器，比如阿里云的-->
            <image>registry.hub.docker.com/moxingwang/jibtest</image>
        </to>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>build</goal>
            </goals>
        </execution>
    </executions>
</plugin>

构建镜像：mvn compile jib:build


docker tag <image> username/repository:tag  # Tag <image> for upload to registry
docker push username/repository:tag 

