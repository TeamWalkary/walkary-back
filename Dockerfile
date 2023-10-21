#build
FROM gradle:7.4-jdk17-alpine as builder
WORKDIR /build

COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY . /build
RUN gradle build -x test --parallel


# runtime
FROM openjdk:17.0-slim
WORKDIR /app

COPY --from=builder /build/build/libs/walkary-server-0.0.1-SNAPSHOT.jar /app/app.jar
ARG MYSQL_HOST
ARG MYSQL_NAME
ARG MYSQL_PASSWORD

ENV MYSQL_HOST=${MYSQL_HOST}
ENV MYSQL_NAME=${MYSQL_NAME}
ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}

USER nobody

ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "-Djava.security.egd=file:/dev/./urandom",              \
    "-Dsun.net.inetaddr.ttl=0",                             \
    "app.jar"                                               \
]