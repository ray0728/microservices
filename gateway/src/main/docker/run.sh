#!/bin/sh
echo "********************************************************"
echo "Waiting for the zipkin server to start on port $ZIPKIN_PORT"
echo "********************************************************"
while ! `nc -z zipkin $ZIPKIN_PORT`; do sleep 3; done
echo "*******  Zipkin Server has started"

echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z configserver $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z discoveryserver $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the account server to start  on port $ACCOUNT_PORT"
echo "********************************************************"
while ! `nc -z accountserver $ACCOUNT_PORT`; do sleep 10; done
echo "******* ACCOUNT has started"

echo "********************************************************"
echo "Waiting for the resource server to start  on port $RESOURCE_PORT"
echo "********************************************************"
while ! `nc -z resourceserver $RESOURCE_PORT`; do sleep 10; done
echo "******* RESOURCE has started"


echo "********************************************************"
echo "Starting the Gateway Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom                \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI            \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
     -Dspring.kafka.bootstrap-servers=$KAFKA_URI            \
     -Dspring.zipkin.base-url=$ZIPKIN_URI                   \
     -Dspring.profiles.active=$PROFILE                      \
-jar /usr/local/server/@project.build.finalName@.jar