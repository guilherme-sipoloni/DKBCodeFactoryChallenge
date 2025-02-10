FROM alpine/java:21

WORKDIR /app

COPY build/libs/dkbCodeFactoryChallenge-0.0.1-SNAPSHOT.jar ./urlshortener.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "urlshortener.jar"]