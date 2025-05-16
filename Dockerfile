FROM eclipse-temurin:21
LABEL authors="krezo"

EXPOSE 5432:5432
RUN mkdir /opt/app
COPY ./build/libs/trpp_database.jar /opt/app/app.jar
COPY ./entrypoint.sh /opt/app/entrypoint.sh

RUN chmod +x /opt/app/entrypoint.sh
CMD ["/opt/app/entrypoint.sh"]
