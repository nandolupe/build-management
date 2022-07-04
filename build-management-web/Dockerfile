FROM openjdk:19
ARG PROFILE

ENV PROFILE $PROFILE
ENV MAIL_USERNAME $MAIL_USERNAME
ENV MAIL_PASSWORD $MAIL_PASSWORD

RUN mkdir /app

COPY target/*.jar /app/app.jar

WORKDIR /app

CMD java \
    -Dspring.profiles.active=$PROFILE \
    -Dspring.mail.username=$MAIL_USERNAME \
    -Dspring.mail.password=$MAIL_PASSWORD \
    -DJava.awt.headless=true \
    -jar \
    -Xms516m \
    -Xmx516m \
    /app/app.jar

EXPOSE 8080
