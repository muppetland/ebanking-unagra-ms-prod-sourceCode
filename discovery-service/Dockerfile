FROM openjdk:17
VOLUME /temp
COPY target/unagra-discovery-server-docker.jar unagra-discovery-server-docker.jar
#ADD target/unagra-discovery-server-docker.jar unagra-discovery-server-docker.jar
ENTRYPOINT ["java", "-jar","unagra-discovery-server-docker.jar"]
EXPOSE 8761