FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Cloud Run expects the app to listen on the PORT environment variable
ENV PORT=8080
EXPOSE $PORT

# Add user for security (Cloud Run best practice)
RUN addgroup -g 1000 appgroup && adduser -D -s /bin/sh -u 1000 -G appgroup appuser
RUN chown appuser:appgroup /app
USER appuser

# Use environment variable for port and add JVM options for Cloud Run
ENTRYPOINT ["sh", "-c", "java -Xmx512m -Dserver.port=${PORT} -jar app.jar"]