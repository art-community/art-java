# Versions:
# Gradle: 5.6.2
# Nodejs: 10.
# Yarn: 1.19.1
# JDK: openjdk 11_0_2

# Building image
FROM ubuntu:18.10

# Enable DNS
RUN echo "nameserver 8.8.8.8" | tee /etc/resolv.conf > /dev/null

# Setup APT sources
RUN DISTRIB_CODENAME=$(cat /etc/*release* | grep DISTRIB_CODENAME | cut -f2 -d'=') \
    && echo "deb http://archive.ubuntu.com/ubuntu ${DISTRIB_CODENAME} main universe\n" > /etc/apt/sources.list \
    && echo "deb http://archive.ubuntu.com/ubuntu ${DISTRIB_CODENAME}-updates main universe\n" >> /etc/apt/sources.list \
    && echo "deb http://security.ubuntu.com/ubuntu ${DISTRIB_CODENAME}-security main universe\n" >> /etc/apt/sources.list

# APT preparation
RUN apt-get update -qqy
RUN apt-get -qqy --no-install-recommends install software-properties-common

# Add APT Git repository
RUN add-apt-repository -y ppa:git-core/ppa
RUN apt-get update -qqy

# Insall APT tools
RUN apt-get -qqy --no-install-recommends install apt-utils
RUN apt-get -qqy --no-install-recommends install aptitude

# Install common libraries
RUN apt-get -qqy --no-install-recommends install \
    curl \
    fontconfig \
    bzr \
    iproute2 \
    tar zip unzip \
    wget curl \
    dirmngr \
    iptables \
    build-essential \
    less nano tree \
    gnupg-agent

# Install SSH
RUN apt-get -qqy --no-install-recommends install \
    openssh-client ssh-askpass

# Install certificates
RUN apt-get -qqy --no-install-recommends install \
    ca-certificates

# Install JDK
RUN apt-get -qqy --no-install-recommends install \
        openjdk-11-jdk

# Install Git
RUN apt-get -qqy --no-install-recommends install \
        git \
        git-lfs

# Install Snap
RUN apt-get -qqy --no-install-recommends install \
    snapd squashfuse fuse
ENV PATH /snap/bin:$PATH

# Install Gradle
ENV GRADLE_HOME /opt/gradle
RUN set -o errexit -o nounset
RUN groupadd --system --gid 1000 gradle
RUN useradd --system --gid gradle --uid 1000 --shell /bin/bash --create-home gradle
RUN mkdir /home/gradle/.gradle
RUN chown --recursive gradle:gradle /home/gradle
RUN ln -s /home/gradle/.gradle /root/.gradle
ENV GRADLE_VERSION 5.6.2
ARG GRADLE_DOWNLOAD_SHA256=32fce6628848f799b0ad3205ae8db67d0d828c10ffe62b748a7c0d9f4a5d9ee0
RUN set -o errexit -o nounset
RUN wget --no-verbose --output-document=gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
RUN unzip gradle.zip
RUN rm gradle.zip
RUN mv "gradle-${GRADLE_VERSION}" "${GRADLE_HOME}/"
RUN ln --symbolic "${GRADLE_HOME}/bin/gradle" /usr/bin/gradle

# Install Maven
ARG MAVEN_VERSION=3.6.2
ARG USER_HOME_DIR="/root"
ARG SHA=d941423d115cd021514bfd06c453658b1b3e39e6240969caf4315ab7119a77299713f14b620fb2571a264f8dff2473d8af3cb47b05acf0036fc2553199a5c1ee
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries
RUN mkdir -p /usr/share/maven /usr/share/maven/ref
RUN curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz
RUN echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c -
RUN tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1
RUN rm -f /tmp/apache-maven.tar.gz
RUN ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

# Install NPM
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN apt-get -qqy --no-install-recommends install nodejs
RUN curl -L https://www.npmjs.com/install.sh | sh

# Install YARN
ENV YARN_VERSION 1.19.1
RUN set -ex \
  && for key in \
    6A010C5166006599AA17F08146C2130DFD2497F5 \
  ; do \
    gpg --batch --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys "$key" || \
    gpg --batch --keyserver hkp://ipv4.pool.sks-keyservers.net --recv-keys "$key" || \
    gpg --batch --keyserver hkp://pgp.mit.edu:80 --recv-keys "$key" ; \
  done
RUN curl -fsSLO --compressed "https://yarnpkg.com/downloads/$YARN_VERSION/yarn-v$YARN_VERSION.tar.gz"
RUN curl -fsSLO --compressed "https://yarnpkg.com/downloads/$YARN_VERSION/yarn-v$YARN_VERSION.tar.gz.asc"
RUN gpg --batch --verify yarn-v$YARN_VERSION.tar.gz.asc yarn-v$YARN_VERSION.tar.gz
RUN mkdir -p /opt
RUN tar -xzf yarn-v$YARN_VERSION.tar.gz -C /opt/
RUN ln -s /opt/yarn-v$YARN_VERSION/bin/yarn /usr/local/bin/yarn
RUN ln -s /opt/yarn-v$YARN_VERSION/bin/yarnpkg /usr/local/bin/yarnpkg
RUN rm yarn-v$YARN_VERSION.tar.gz.asc yarn-v$YARN_VERSION.tar.gz

# Set securerandom for Java
RUN sed -i 's/securerandom\.source=file:\/dev\/random/securerandom\.source=file:\/dev\/urandom/' ./usr/lib/jvm/java-11-openjdk-amd64/conf/security/java.security

# Copy Agent files
COPY build/libs /agent
COPY src/main/resources /agent
WORKDIR /agent

# Running
ENTRYPOINT ["java", "-jar", "agent-1.1.0-SNAPSHOT.jar", "-server -XX:GCTimeRatio=2 -Xms1g -Xmx1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MinHeapFreeRatio=40 -XX:MaxHeapFreeRatio=90 -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=30 -XX:+AggressiveOpts -XX:+UseTLAB -XX:CompileThreshold=100 -XX:ThreadStackSize=4096 -XX:+UseFastAccessorMethods -XX:MaxTenuringThreshold=5 -XX:ReservedCodeCacheSize=256m"]