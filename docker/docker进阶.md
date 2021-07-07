#镜像
docker rmi -f $(docker images -aq)
#容器
docker run  -it -d --name ubuntu01 -p 80:8080 -e ES_JAVA_OPTS="-Xms64m -Xmx512m" ubuntu /bin/bash
# 常见的坑，docker容器使用后台运行，就必须要有要一个前台进程，docker发现没有应用，就会自动停止
docker top 容器id 查看容器运行信息
docker inspect 镜像id  命令查看容器信息包含镜像信息
exit #容器直接退出
ctrl +P +Q #容器不停止退出
docker exec -it 容器id bashshell  进入当前正在运行的容器
docker attach 容器id 进入当前正在运行的容器的运行进程
docker rm -f $(docker ps -aq) = docker ps -a -q|xargs docker rm  #删除所有的容器
docker cp 容器id:容器内路径  主机目的路径


# 数据卷技术：容器和数据分离，数据共享
三种挂载方式：
-v 容器内路径			#匿名挂载,-v只写了容器内的路径，没有写容器外的路径
-v 卷名：容器内路径:ro		#具名挂载
-v /宿主机路径：容器内路径 #指定路径挂载:rw docker volume ls 是查看不到的
docker volume ls 查看所有的volume的情况,可以查看具名挂载和匿名挂载
docker volume inspect volumeName 可以查看具体的挂载信息
所有的docker容器内的卷，没有指定目录的情况下都是在/var/lib/docker/volumes/xxxx/_data下,如果指定了目录，docker volume ls 是查看不到的
docker run -it -v 主机目录:容器内目录  -p 主机端口:容器内端口
docker run -it -v /Users/jiaxiaopeng/home:/home centos:centos7 /bin/bash
docker run -d -p 3306:3306 -v /home/mysql/conf:/etc/mysql/conf.d -v /home/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --name mysql mysql:5.7

# Dockerfile
①. Dockerfile是用来构建Docker镜像的构建文件，是由一系列命令和参数构成的脚本。
②. 构建三步骤(编写Dockerfile文件 | docker build | docker run)
一般而言，Dockerfile可以分为三部分:基础镜像信息 镜像操作指令 启动时执行指令
FROM alpine
docker build -f XXX/DockerFile -t USER/REPOSITORY:TAG
docker tag 镜像id USER/REPOSITORY:TAG
docker push 镜像id
每条保留字指令都必须为大写字母且后面要跟随至少一个参数
指令按照从上到下，顺序执行
#表示注释
每条指令都会创建一个新的镜像层，并对镜像进行提交
②. 从应用软件的角度来看，Dockerfile、Docker镜像与Docker容器分别代表软件的三个不同阶段 掌握
Dockerfile是软件的原材料
Docker镜像是软件的交付品
Docker容器则可以认为是软件的运行态
保留字指令：
FROM				# 基础镜像，一切从这里开始构建
MAINTAINER			# 镜像是谁写的， 姓名+邮箱
RUN					# 镜像构建的时候需要运行的命令
ADD					# ADD和COPY的功能是一样的,ADD多了自动下载远程文件和解压的功能
COPY				# 类似ADD，拷贝文件和目录到镜像中
WORKDIR				# 镜像的工作目录，指定在创建容器后，终端默认登陆的进来工作目录，一个落脚点
VOLUME				# 挂载的目录，容器数据卷，用于数据保存和持久化工作
EXPOSE				# 当前容器对外暴露出的端口
CMD					# 指定这个容器启动的时候要运行的命令，Dockerfile 中可以有多个 CMD 指令，但只有最后一个生效，CMD 会被 docker run 之后的参数替换
ENTRYPOINT			# 指定一个容器启动时要运行的命令,使用docker run 之后的参数会进行一个叠加的操作
ONBUILD				# 当构建一个被继承 DockerFile 这个时候就会运行ONBUILD的指令，触发指令。
ENV					# 构建的时候设置环境变量！

FROM centos
MAINTAINER jxp<xxx@qq.com>
ENTRYPOINT ["ls","-a"]
COPY README /usr/local/README #复制文件
ADD jdk-8u231-linux-x64.tar.gz /usr/local/ #复制解压
ADD apache-tomcat-9.0.35.tar.gz /usr/local/ #复制解压
RUN yum -y install vim
ENV MYPATH /usr/local #设置环境变量
WORKDIR $MYPATH #设置工作目录
ENV JAVA_HOME /usr/local/jdk1.8.0_231 #设置环境变量
ENV CATALINA_HOME /usr/local/apache-tomcat-9.0.35 #设置环境变量
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib #设置环境变量 分隔符是：
EXPOSE 8080 #设置暴露的端口
CMD /usr/local/apache-tomcat-9.0.35/bin/startup.sh && tail -F /usr/local/apache-tomcat-9.0.35/logs/catalina.out # 设置默认命令

RUN指令有两种形式,一种是shell,另外一个是exec形式
RUN echo "123456"   在shell形式中,您可以使用\（反斜杠）将一条指令继续到下一行
RUN ["/bin/sh","-c","echo $prams"] -c可以自动替换变量

CMD [ "ping","baidu.com" ]
ENTRYPOINT [ "ping","-c" ]
官方都是建议使用[ ]方式(CMD ["/bin/sh","-c",“ping ${url}”]),变化的写CMD,固定不变的写ENTRYPO INT(未来他是容器启动的唯一入口)

docker network ls 列出这些docker内置的网络模式
Docker使用的是Linux的桥接，宿主机是一个Docker容器的网桥 docker0
Docker中所有网络接口都是虚拟的，虚拟的转发效率高（内网传递文件）
只要容器删除，对应的网桥一对就没了！

docker run -d -P --name tomcat03 --link tomcat02 tomcat

# docker-compose
Compose 是用于定义和运行多容器 Docker 应用程序的工具。通过 Compose，您可以使用 YML 文件来配置应用程序需要的所有服务。然后，使用一个命令，就可以从 YML 文件配置中创建并启动所有服务。
如果你还不了解 YML 文件配置，可以先阅读 YAML 入门教程。
Compose 使用的三个步骤：
使用 Dockerfile 定义应用程序的环境。
使用 docker-compose.yml 定义构成应用程序的服务，这样它们可以在隔离环境中一起运行。
最后，执行 docker-compose up 命令来启动并运行整个应用程序。
docker-compose version 查看安装的版本


