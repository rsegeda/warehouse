# Build Stage for Spring boot application image
FROM azul/zulu-openjdk-alpine:11 as build

WORKDIR /app

COPY gradle gradle
COPY gradlew .
RUN chmod +x gradlew
COPY build.gradle .
COPY src src
RUN ./gradlew build installBootDist -x test

# Production Stage for Spring boot application image
FROM azul/zulu-openjdk-alpine:11-jre as production

# Copy the dependency application file from build stage artifact
COPY --from=build /app/build/install/app-boot /app

# Run the Spring boot application
ENTRYPOINT exec ./app/bin/app
