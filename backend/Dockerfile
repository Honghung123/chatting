FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app
COPY mvnw mvnw.cmd *.xml ./
COPY .mvn .mvn 
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean install -DskipTests
RUN rm target/*.original

FROM eclipse-temurin:17-jre-jammy AS final
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/chatting.jar
ENTRYPOINT ["java", "-jar" , "/app/chatting.jar"]