echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom                         \
     -Dserver.port=$CONFIGSERVER_PORT                                \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI        \
     -Dspring.cloud.config.server.git.uri=$GIT_URI                   \
     -Dspring.cloud.config.server.git.search-paths=$GIT_SEARCH_PATH  \
     -Dspring.cloud.config.server.git.username=$GIT_USERNAME         \
     -Dspring.cloud.config.server.git.password=$GIT_PASSWD           \
     -Dlocal.save.dir=$SAVE_DIR                                      \
     -jar /usr/local/server/@project.build.finalName@.jar
