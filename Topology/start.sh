#!/bin/bash

docker-compose down

echo "Starting MySQL and phpMyAdmin servers..."
docker-compose up --build -d  db phpmyadmin proxy mock 
echo "Servers started"

cd ../API-Management
mvn clean package 
cp target/swagger-spring-1.0.0.jar ../Topology/images/api-management/app.jar
cp src/main/resources/docker-application.properties ../Topology/images/api-management/docker-application.properties

cd ../API-Calendar
mvn clean package 
cp src/main/resources/docker-application.properties ../Topology/images/api-calendar/docker-application.properties
cp target/swagger-spring-1.0.0.jar ../Topology/images/api-calendar/app.jar

cd ../Topology/
docker-compose up --build -d api-management api-calendar
