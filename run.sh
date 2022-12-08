#!/bin/sh

# allow external access to redis (may be removable - was required for testing)
sed -i 's/127.0.0.1/0.0.0.0/g' /etc/redis/redis.conf
# start redis server
service redis-server start
cd /app
# start server
java -jar MuchBetter.API.jar