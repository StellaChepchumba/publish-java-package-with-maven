##base Image
#FROM openjdk:17-jdk-alpine
#COPY msg-core/target/messaging-core-0.0.1.jar messaging-core-latest-0.0.1.jar
#ENTRYPOINT ["java","-jar","/messaging-core-0.0.1.jar"]
#EXPOSE 8082

# the first stage of our build will use a maven 3.6.1 parent image
FROM maven:3.6.1-jdk-11 AS MAVEN_BUILD

#set a working dir
WORKDIR /msg-core

# copy the pom and src code to the container
COPY ./ ./

# package our application code
RUN mvn -B package -DskipTests --file pom.xml

# the second stage of our build will use open jdk 8 on alpine 3.9
FROM openjdk:17-jdk-alpine

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD  /msg-core/msg-core/target/messaging-core-0.0.1-SNAPSHOT.jar /messaging-core-latest-0.0.1.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/messaging-core-latest-0.0.1.jar"]