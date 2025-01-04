FROM openjdk:17-oracle
LABEL maintainer="nik"
COPY target/project01-prod-user-redis.jar /ProdUserRedis.jar
ENTRYPOINT ["java", "-jar", "/ProdUserRedis.jar"]

