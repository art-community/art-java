# Building image
FROM ubuntu:18.10

RUN echo "nameserver 8.8.8.8" | tee /etc/resolv.conf > /dev/null

RUN DISTRIB_CODENAME=$(cat /etc/*release* | grep DISTRIB_CODENAME | cut -f2 -d'=') \
    && echo "deb http://archive.ubuntu.com/ubuntu ${DISTRIB_CODENAME} main universe\n" > /etc/apt/sources.list \
    && echo "deb http://archive.ubuntu.com/ubuntu ${DISTRIB_CODENAME}-updates main universe\n" >> /etc/apt/sources.list \
    && echo "deb http://security.ubuntu.com/ubuntu ${DISTRIB_CODENAME}-security main universe\n" >> /etc/apt/sources.list

RUN apt-get update -qqy
RUN apt-get -qqy --no-install-recommends install software-properties-common
RUN add-apt-repository -y ppa:git-core/ppa
RUN apt-get update -qqy

RUN apt-get -qqy --no-install-recommends install \
    iproute2 \
    openssh-client ssh-askpass\
    ca-certificates \
    openjdk-8-jdk \
    tar zip unzip \
    wget curl \
    git \
    iptables \
    build-essential \
    less nano tree

RUN rm -rf /var/lib/apt/lists/*

RUN sed -i 's/securerandom\.source=file:\/dev\/random/securerandom\.source=file:\/dev\/urandom/' ./usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/java.security

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

COPY build/libs /agent
COPY src/main/resources /agent

WORKDIR /agent

# Running
EXPOSE 9002
EXPOSE 10001

ENTRYPOINT [ "java", "-Xdebug", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=10001", "-jar", "agent-1.1.0-SNAPSHOT.jar", "-server -XX:GCTimeRatio=2 -Xms1g -Xmx1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=90 -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=30 -XX:+AggressiveOpts -XX:+UseTLAB -XX:CompileThreshold=100 -XX:ThreadStackSize=4096 -XX:+UseFastAccessorMethods -XX:MaxTenuringThreshold=5 -XX:ReservedCodeCacheSize=256m"]