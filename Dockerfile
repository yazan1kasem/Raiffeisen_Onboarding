FROM ubuntu:latest
LABEL authors="sinoa"

ENTRYPOINT ["top", "-b"]