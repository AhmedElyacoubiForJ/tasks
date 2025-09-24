#!/bin/bash
export $(grep -v '^#' .env | xargs)
SPRING_PROFILES_ACTIVE=local-prod ./mvnw spring-boot:run
