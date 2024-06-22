FROM gradle:jdk17-alpine
ARG PRODUCTION

ENV PRODUCTION ${PRODUCTION}

WORKDIR /app
COPY ./build/libs/service-registry-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java","-jar","service-registry-0.0.1-SNAPSHOT.jar"]
