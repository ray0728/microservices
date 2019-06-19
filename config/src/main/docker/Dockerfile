FROM ray/web-jar:1.8.0
RUN mkdir -p /usr/local/server
ADD @project.build.finalName@.jar /usr/local/server
ADD run.sh run.sh
RUN chmod a+x run.sh
CMD ./run.sh
