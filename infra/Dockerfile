# base image
FROM adoptopenjdk/openjdk11:alpine-jre

# set the working directory
WORKDIR /app

# copy the pom.xml and the source code to the container
COPY ../target/www-be-0.0.1-SNAPSHOT.jar app.jar

# expose container port 8080
EXPOSE 8080

# set the startup command to execute the jar
CMD ["java", "-jar", "-Dspring.profiles.active=alpha", "app.jar"]
