FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash
ARG JAR_FILE=target/profiles.jar
COPY ${JAR_FILE} app.jar
COPY src/docker/wait-for-it.sh wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh
#ENTRYPOINT ["java", "-jar", "/app.jar"]
ENTRYPOINT ["./wait-for-mysql.sh", "mysql:3306", "--", "java", "-jar", "/app.jar"]