FROM java:8-jre-alpine

RUN mkdir /var/app

ARG JAR_FILE

COPY ${JAR_FILE} /var/app

WORKDIR /var/app

EXPOSE 8080

CMD java -jar -Djava.security.egd=file:/dev/./urandom *.jar