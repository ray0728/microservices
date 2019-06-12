FROM tanxin/ubuntu-jar:1.8.0
RUN mkdir -p /usr/local/configserver
ADD @project.build.finalName@.jar /usr/local/configserver
ADD run.sh run.sh
RUN chmod a+x run.sh
CMD ./run.sh
