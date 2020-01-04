#!/bin/bash

echo "Starting MySQL and phpMyAdmin servers..."
docker-compose up --build -d  db phpmyadmin 
echo "Servers started"

cd ../API-Management
mvn clean package
cp target/swagger-spring-1.0.0.jar ../Topology/images/api-management/app.jar

cd ../API-Calendar
mvn clean package
cp target/swagger-spring-1.0.0.jar ../Topology/images/api-calendar/app.jar

cd ../Topology/
docker-compose up --build -d api-management api-calendar
