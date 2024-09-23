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
* **[Consuming](#consuming)**
  * **[Logging](#logging)**

## Building

The microservice might be built and run successfully under **Ubuntu Server (Ubuntu 22.04.5 LTS x86-64)**. Install the necessary dependencies (`openjdk-17-jdk-headless`, ~~`gradle`~~, `make`, `sqlite3`, `docker.io`):

```
$ sudo apt-get update && \
  sudo apt-get install openjdk-17-jdk-headless make sqlite3 docker.io -y
...
```

**Note:** A system-wide Gradle installation is not needed to build the microservice, since it is intended to use Gradle Wrapper for that, which is already resided in the current repository. Hence, commands given in the following two paragraphs can be simply ignored &mdash; they are kept here just for reference.

> Since Gradle package is somehow outdated in the stock Ubuntu package repository, it is preferred to be installed through the SDKMAN! toolkit. For that, first it needs to install SDKMAN! and to `source` its initialization script:

```
$ curl -s https://get.sdkman.io | bash
...
$ . /home/<username>/.sdkman/bin/sdkman-init.sh
```

> Then install latest stable version of Gradle via SDKMAN!:

```
$ sdk install gradle
...
```

---

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
$ java -jar build/libs/customers-api-lite-0.1.1.jar; echo $?
...
```

## Consuming

The microservice exposes **six endpoints** to web clients. They are all intended to deal with customer entities and/or contact entities that belong to customer profiles. The following table displays their syntax:

No. | Endpoint name                                      | Request method and REST URI                            | Request body
--- | -------------------------------------------------- | ------------------------------------------------------ | ------------
1   | Create customer                                    | `PUT /customers`                                       | `{}`
2   | Create contact                                     | `PUT /customers/{customer_id}/contact`                 | `{}`
3   | List customers                                     | `GET /customers`                                       | N/A
4   | Retrieve customer                                  | `GET /customers/{customer_id}`                         | N/A
5   | List contacts for a given customer                 | `GET /customers/{customer_id}/contacts`                | N/A
6   | List contacts of a given type for a given customer | `GET /customers/{customer_id}/contacts/{contact_type}` | N/A

The following command-line snippets display the exact usage for these endpoints (the **cURL** utility is used as an example to access them):

1. **Create customer**

```
$ curl -vXPUT http://localhost:8765/customers
...
> PUT /customers HTTP/1.1
...
< HTTP/1.1 201 Created
...
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 3
...
[/]$
```

2. **Create contact**

```
$ curl -vXPUT http://localhost:8765/customers/{customer_id}/contact
...
> PUT /customers/customer_id/contact HTTP/1.1
...
< HTTP/1.1 201 Created
...
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 20
...
/customer_id/contact$
```

3. **List customers**

```
$ curl -v http://localhost:8765/customers
...
> GET /customers HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 1
...
/$
```

4. **Retrieve customer**

```
$ curl -v http://localhost:8765/customers/{customer_id}
...
> GET /customers/customer_id HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 12
...
/customer_id$
```

5. **List contacts for a given customer**

```
$ curl -v http://localhost:8765/customers/{customer_id}/contacts
...
> GET /customers/customer_id/contacts HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 21
...
/customer_id/contacts$
```

6. **List contacts of a given type for a given customer**

```
$ curl -v http://localhost:8765/customers/{customer_id}/contacts/{contact_type}
...
> GET /customers/customer_id/contacts/contact_type HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 34
...
/customer_id/contacts/contact_type$
```

### Logging

The microservice has the ability to log messages to a logfile and to the Unix syslog facility. When running under Ubuntu Server (not in a Docker container), logs can be seen and analyzed in an ordinary fashion, by `tail`ing the `log/customers-api-lite.log` logfile:

```
$ tail -f log/customers-api-lite.log
...
[2024-09-12][23:20:44][INFO ]  Undertow started on port 8765 (http) with context path '/'
[2024-09-12][23:20:44][INFO ]  Started CustomersApiLiteApp in 5.345 seconds (process running for 6.609)
[2024-09-12][23:20:44][DEBUG]  [Customers API Lite]
[2024-09-12][23:20:44][INFO ]  Server started on port 8765
[2024-09-12][23:25:07][INFO ]  Commencing graceful shutdown. Waiting for active requests to complete
[2024-09-12][23:25:07][INFO ]  Graceful shutdown complete
[2024-09-12][23:25:07][INFO ]  stopping server: Undertow - 2.3.13.Final
[2024-09-12][23:25:07][INFO ]  Server stopped
```

Messages registered by the Unix system logger can be seen and analyzed using the `journalctl` utility:

```
$ journalctl -f
...
Sep 12 23:20:44 <hostname> java[<pid>]: [Customers API Lite]
Sep 12 23:20:44 <hostname> java[<pid>]: Server started on port 8765
Sep 12 23:25:07 <hostname> java[<pid>]: Server stopped
```

---

:dvd:
