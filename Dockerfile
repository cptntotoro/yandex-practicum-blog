# Этап сборки
FROM gradle:8.6-jdk21 AS build

WORKDIR /app
COPY build.gradle ./
COPY src ./src

# Кэширование зависимостей
RUN gradle dependencies --no-daemon

# Сборка
RUN gradle bootJar -x test --no-daemon

# Этап запуска
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Копируем только JAR из этапа сборки
COPY --from=build /app/build/libs/app.jar ./app.jar

# Оптимизация для Spring Boot
ENV SPRING_PROFILES_ACTIVE=prod \
    SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]