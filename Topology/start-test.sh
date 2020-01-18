#!/bin/bash

cd test

docker-compose down

echo "Starting MySQL and phpMyAdmin servers..."
docker-compose up --build -d  db 
echo "Servers started"

echo "Waiting 10 secs..."
sleep 10

cd ../../API-Management
mvn test

cd ../../API-Calendar
mvn test package


cd ../Topology/test/
docker-compose down
