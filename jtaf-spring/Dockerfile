FROM openjdk:8-jdk-alpine

VOLUME /tmp

ADD target/jtaf-spring-1.0.0-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar