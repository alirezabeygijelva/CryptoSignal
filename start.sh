#!/bin/bash

# 1.
echo "Building the project with Maven..."
mvn clean package -DskipTests=true

if [ $? -ne 0 ]; then
  echo "Maven build failed!"
  exit 1
fi

echo "Maven build succeeded."

# 2.
echo "Starting V2Ray in the background..."
nohup /usr/local/bin/v2ray/v2ray run -c /etc/v2ray/config.json > v2ray.log 2>&1 &

if [ $? -ne 0 ]; then
  echo "Failed to start V2Ray!"
  exit 1
fi

echo "V2Ray started successfully."

# 3.
echo "Starting the Java application..."
java ${JAVA_OPTS} -jar target/fms.jar

if [ $? -ne 0 ]; then
  echo "Failed to start Java application!"
  exit 1
fi

echo "Java application started successfully."
