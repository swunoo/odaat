#!/bin/bash

# Running the backend
cd ./spring-server/odaat || { echo "Not Found."; exit 1; }
mvn spring-boot:run &
SERVER_PID=$!
sleep 10

# Running the frontend
cd ../../webapp || { echo "Not Found."; exit 1; }
npm run dev &
WEBAPP_PID=$!
sleep 5

# Function to stop backend and frontend
stop_apps() {
  echo "Stopping the applications..."
  kill $SERVER_PID
  kill $WEBAPP_PID
  echo "Applications stopped."
}

# Stop applications when the program exits
trap stop_apps SIGINT SIGTERM