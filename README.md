# Customers API Lite microservice prototype :small_blue_diamond: :coffee:

**A Spring Boot-based application, designed and intended to be run as a microservice,
<br />implementing a special Customers API prototype
<br />with a smart yet simplified data scheme**

:cd:

Since the microservice architecture for building independent backend modules of a composite system are very prevalent nowadays, this seems to be natural for creating a microservice, which is containerized and run as a daemon, serving a continuous flow of HTTP requests.

This microservice is intended to be built locally and to be run like a conventional daemon in the VM environment, as well as a containerized service, managed by Docker.

One may consider this project to be suitable for a wide variety of applied areas and may use this prototype as: (1) a template for building similar microservices, (2) for evolving it to make something more universal, or (3) to simply explore it and take out some snippets and techniques from it for *educational purposes*, etc.

---

## Table of Contents

* **[Building](#building)**
* **[Running](#running)**

## Building

The microservice might be built and run successfully under **Ubuntu Server (Ubuntu 22.04.4 LTS x86-64)**. Install the necessary dependencies (`openjdk-17-jre-headless`, `gradle`, `make`, `docker.io`):

```
$ sudo apt-get update && \
  sudo apt-get install openjdk-17-jre-headless make docker.io -y
...
```

Since Gradle package is somehow outdated in the stock Ubuntu package repository, it is preferred to be installed through the SDKMAN! toolkit. For that, first it needs to install SDKMAN! and to `source` its initialization script:

```
$ curl -s https://get.sdkman.io | bash
...
$ . /home/radic/.sdkman/bin/sdkman-init.sh
```

Then install latest stable version of Gradle via SDKMAN!:

```
$ sdk install gradle
...
```

:cd:

## Running

**Run** the microservice using **Gradle Wrapper** (generally for development and debugging purposes):

:dvd:
