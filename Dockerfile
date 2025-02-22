# Этап сборки
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

# Этап запуска
FROM tomcat:10.1-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR с явным именем
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Добавляем healthcheck для Tomcat
HEALTHCHECK --interval=30s --timeout=5s \
  CMD curl -f http://localhost:8080/posts/actuator/health || exit 1

EXPOSE 8080
CMD ["catalina.sh", "run"]