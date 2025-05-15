FROM eclipse-temurin:21
LABEL authors="krezo"

ARG JAR_FILE

RUN mkdir /opt/app
COPY ./target/${JAR_FILE} /opt/app/app.jar
COPY ./entrypoint.sh /opt/app/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh
CMD ["/apps/entrypoint.sh"]