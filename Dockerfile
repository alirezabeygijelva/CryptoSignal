# Build Stage
FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app
COPY . .

# Download and install dockerize.
# Needed so the web container will wait for MySQL to start.
ENV DOCKERIZE_VERSION v0.19.0
RUN curl -sfL https://github.com/powerman/dockerize/releases/download/"$DOCKERIZE_VERSION"/dockerize-`uname -s`-`uname -m` | install /dev/stdin /usr/local/bin/dockerize

EXPOSE 80
EXPOSE 8080
EXPOSE 3306

# Pre-Install Maven Dependency and Plugins
# Comment the following two commands if you don't want dependencies and plugins to be installed when building the image.
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "dependency:resolve-plugins"]

# Wait for the db to startup(via dockerize), then
# Build and run steve, requires a db to be available on port 3306
CMD dockerize -wait tcp://mysql-db:3306 -timeout 60s && \
	mvn clean package -DskipTests=true && \
	java -jar target/fms.jar