FROM eclipse-temurin:21-jre

# 设置工作目录
RUN mkdir -p /vertex-app
WORKDIR /vertex-app

# 复制Jar包到镜像中
COPY vertex-application/target/*.jar app.jar

## 设置 TZ 时区
ENV TZ=Asia/Shanghai
## 设置 JAVA_OPTS 环境变量
ENV JAVA_OPTS="-Xms512m -Xmx512m"
## 应用参数
ENV ARGS="--spring.profiles.active=prod"

# 暴露端口
EXPOSE 10240

## 启动后端项目
CMD java ${JAVA_OPTS} -jar app.jar $ARGS