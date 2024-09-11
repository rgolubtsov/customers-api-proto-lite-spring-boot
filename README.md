# Customers API Lite microservice prototype :small_blue_diamond: :coffee:

**A Spring Boot-based application, designed and intended to be run as a microservice,
<br />implementing a special Customers API prototype with a smart yet simplified data scheme**

*Preamble is to be written later on* &mdash; **TBD** :cd:

Since the microservice architecture for building independent backend modules of a composite system are very prevalent nowadays, this seems to be natural for creating a microservice, which is containerized and run as a daemon, serving a continuous flow of HTTP requests.

This microservice is intended to be built locally and to be run like a conventional daemon in the VM environment, as well as a containerized service, managed by Docker.

One may consider this project to be suitable for a wide variety of applied areas and may use this prototype as: (1) a template for building similar microservices, (2) for evolving it to make something more universal, or (3) to simply explore it and take out some snippets and techniques from it for *educational purposes*, etc.

---

## Table of Contents

* **[Building](#building)**
* **[Running](#running)**

## Building

The microservice might be built and run successfully under **Ubuntu Server (Ubuntu 22.04.4 LTS x86-64)**. Install the necessary dependencies (`openjdk-17-jdk-headless`, `gradle`, `make`, `sqlite3`, `docker.io`):

```
$ sudo apt-get update && \
  sudo apt-get install openjdk-17-jdk-headless make sqlite3 docker.io -y
...
```

Since Gradle package is somehow outdated in the stock Ubuntu package repository, it is preferred to be installed through the SDKMAN! toolkit. For that, first it needs to install SDKMAN! and to `source` its initialization script:

```
$ curl -s https://get.sdkman.io | bash
...
$ . /home/<username>/.sdkman/bin/sdkman-init.sh
```

Then install latest stable version of Gradle via SDKMAN!:

```
$ sdk install gradle
...
```

**Build** the microservice using **Gradle Wrapper**:

```
$ ./gradlew clean
...
$ ./gradlew compileJava
...
$ ./gradlew build
...
```

Or **build** the microservice using **GNU Make** (optional, but for convenience &mdash; it covers the same **Gradle Wrapper** build workflow under the hood):

```
$ make clean
...
$ make      # <== Compilation phase.
...
$ make all  # <== Assembling JAR bundles of the microservice.
...
```

## Running

**Run** the microservice using **Gradle Wrapper** (generally for development and debugging purposes):

```
$ ./gradlew -q bootRun; echo $?
...
```

**Run** the microservice using its all-in-one JAR bundle, built previously by the `build` or `all` targets:

```
$ java -jar build/libs/customers-api-lite-0.0.1.jar; echo $?
...
```

---

:dvd:
