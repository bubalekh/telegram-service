# build
FROM maven:3-eclipse-temurin-11 AS builder
WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn -B -e -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY . .
RUN mvn -B -e -o -T 1C verify --fail-never
RUN mvn -B -e -o -T 1C clean package spring-boot:repackage

# runtime
FROM eclipse-temurin:11-jre AS runtime
RUN addgroup --system app && \
    adduser --system --home /opt/app --ingroup app app
WORKDIR /opt/app
COPY --from=builder --chown=app:app /usr/src/app/target/*.war /opt/app/app.war
USER app:app
ENTRYPOINT [ "java", "-jar", "/opt/app/app.war" ]