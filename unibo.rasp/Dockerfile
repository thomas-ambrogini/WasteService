## ----------------------------------------------------------
## Dockerfile for unibo.basicrobot22
## ----------------------------------------------------------

FROM openjdk:12.0.2
EXPOSE 8056
## ADD extracts the tar
ADD ./build/distributions/unibo.rasp-1.4.tar /
WORKDIR /unibo.rasp-1.4/bin
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.sh ./
COPY So* ./
## COPY ./*.py ./


CMD ["bash", "unibo.rasp"]

## gradlew distTar
##  docker build -t basicrobot22:2.0 .
## OCCORRE APRIRE UDP PER PERMETTERE COAP
## docker run -it --rm --name basicrobot22 -p8020:8020/tcp -p8020:8020/udp --privileged basicrobot22:2.0  /bin/bash
## MODIFICA DEL FILE DI CONFIGURAZIONE
# docker cp basicrobotConfig.json a343be9333cb:/unibo.basicrobot22-2.0/bin/basicrobotConfig.json
# docker cp stepTimeConfig.json a9c36dac5bdd:/unibo.basicrobot-2.0/bin/basicrobotConfig.json
# docker tag basicrobot22:2.0 natbodocker/basicrobot22:2.0

#type docker_password.txt | docker login --username natbodocker --password-stdin
#docker login --username natbodocker --password xyz
#docker push natbodocker/basicrobot22:2.0
