FROM openjdk:11 AS build
WORKDIR /var/src

# Download gradle
COPY gradlew .
COPY gradle/ gradle/
RUN ./gradlew --no-daemon --version

# Build
COPY . .
RUN ./gradlew --no-daemon shadowJar

FROM openjdk:11-jre
WORKDIR /var/app

COPY --from=build /var/src/build/libs/karmaleon-all.jar .

CMD java -jar karmaleon-all.jar
