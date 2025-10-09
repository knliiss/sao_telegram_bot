FROM ubuntu:latest
LABEL authors="knalis"

ENTRYPOINT ["top", "-b"]