echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$CONFIGSERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
     -jar /usr/local/server/@project.build.finalName@.jar
