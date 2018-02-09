FROM openjdk:8-jdk-alpine

VOLUME /tmp

ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /${project.build.finalName}.jar