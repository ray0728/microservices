FROM ray/web-jar:1.8.0
RUN mkdir -p /usr/local/server
ADD sources.list /etc/apt/
RUN apt update && apt install -y netcat-openbsd
ADD @project.build.finalName@.jar /usr/local/server
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh