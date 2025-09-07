FROM ubuntu:latest
LABEL authors="sihme"

ENTRYPOINT ["top", "-b"]