# -- DEV DOCKERFILE --
# -- DO NOT USE IN PRODUCTION! --

FROM eclipse-temurin:21.0.3_9-jdk-jammy
ENV JAVA_HOME=/opt/java/openjdk
COPY --from=eclipse-temurin:21 $JAVA_HOME $JAVA_HOME
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN apt-get update && \
    apt-get install -y git vim unzip && \
    mkdir -p /ringo-app && \
    wget https://services.gradle.org/distributions/gradle-8.7-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.7-bin.zip -d /opt && \
    rm /tmp/gradle-8.7-bin.zip


ENV GRADLE_HOME=/opt/gradle-8.7
ENV PATH="${GRADLE_HOME}/bin:${PATH}"

#node & npm & yarn 설치 그래들로 대체
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash && \
    bash -c "source ~/.bashrc"
RUN /bin/bash -c "source ~/.nvm/nvm.sh && nvm install 18.19.1 && npm install --global yarn"
RUN #/bin/sh -c npm install --global yarn

RUN mkdir -p /ringo-app

WORKDIR /ringo-app

EXPOSE 9090

CMD ["tail", "-f", "/dev/null"]
