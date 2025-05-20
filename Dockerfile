FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV USER_PROFILE=local
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${USER_PROFILE}","/app.jar"]