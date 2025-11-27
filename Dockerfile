FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el jar compilado
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
