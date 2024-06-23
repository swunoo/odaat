#!/bin/bash

# Unit tests for the backend
cd ./spring-server/odaat || { echo "Not Found."; exit 1; }
mvn test || { echo "Tests failed."; exit 1; }

# Unit tests for the frontend
cd ../../webapp || { echo "Not Found."; exit 1; }
npm test || { echo "Tests failed."; exit 1; }

# Running the backend
cd ../spring-server/odaat || { echo "Not Found."; exit 1; }
mvn spring-boot:run &
SERVER_PID=$!
sleep 10

# Running the frontend
cd ../../webapp || { echo "Not Found."; exit 1; }
npm run dev &
WEBAPP_PID=$!
sleep 5

# Function to handle integration errors
handle_error() {
  echo "Integration tests failed."
  stop_apps
  exit 1
}

# Function to stop backend and frontend
stop_apps() {
  echo "Stopping the applications..."
  kill $SERVER_PID
  kill $WEBAPP_PID
  echo "Applications stopped."
}

# Stop applications when the program exits
trap stop_apps SIGINT SIGTERM

# Integration tests
npx playwright test --workers=1 || handle_error

# Print success and exit
echo "Tests succeeded."
stop_apps
exit 0