# runtime
FROM eclipse-temurin:17-jre AS runtime
RUN addgroup --system app && \
    adduser --system --home /opt/app --ingroup app app
WORKDIR /opt/app
COPY --chown=app:app target/*.war /opt/app/app.war
USER app:app
ENTRYPOINT [ "java", "-jar", "/opt/app/app.war" ]