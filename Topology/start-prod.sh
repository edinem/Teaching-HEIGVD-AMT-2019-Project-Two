#!/bin/bash

cd prod

docker-compose down

echo "Starting MySQL and phpMyAdmin servers..."
docker-compose up --build -d  db phpmyadmin proxy mock 
echo "Servers started"

cd ../../API-Management
mvn clean install -DskipTests
cp target/swagger-spring-1.0.0.jar ../Topology/prod/images/api-management/app.jar
cp src/main/resources/docker-application.properties ../Topology/prod/images/api-management/docker-application.properties

cd ../API-Calendar
mvn clean install -DskipTests
cp src/main/resources/docker-application.properties ../Topology/prod/images/api-calendar/docker-application.properties
cp target/swagger-spring-1.0.0.jar ../Topology/prod/images/api-calendar/app.jar

cd ../Topology/prod/
docker-compose up --build -d api-management api-calendar
