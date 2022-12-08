FROM adoptopenjdk/openjdk8

# create app folder
RUN mkdir /app

# copy files
COPY run.sh /app
COPY build/libs/MuchBetter.API.jar /app

# makr run script executable
RUN chmod 755 /app/run.sh

# install redis
RUN apt update
RUN apt install -y redis-server

WORKDIR /app
# execute run script
cmd ./run.sh

# Expose redis & service ports
EXPOSE 6379
EXPOSE 5050