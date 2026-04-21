# 1. Fase di Build (usa JDK 21 e Maven)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copia solo il pom.xml per scaricare le dipendenze (ottimizza la cache)
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia il resto del codice e compila
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Fase di Run (usa JRE 21 leggero)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Prende il file .jar generato dalla fase precedente
COPY --from=build /app/target/*.jar app.jar
EXPOSE 10000
# Avvia l'app sulla porta richiesta da Render
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]