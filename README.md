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
  * **[Error handling](#error-handling)**

## Building

The microservice might be built and run successfully under **Ubuntu Server (Ubuntu 24.04.1 LTS x86-64)**. Install the necessary dependencies (`openjdk-17-jdk-headless`, ~~`gradle`~~, `make`, `sqlite3`, `docker.io`):

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
$ java -jar build/libs/customers-api-lite-0.2.5.jar; echo $?
...
```

## Consuming

The microservice exposes **six REST API endpoints** to web clients. They are all intended to deal with customer entities and/or contact entities that belong to customer profiles. The following table displays their syntax:

No. | Endpoint name                                      | Request method and REST URI                                   | Request body
--: | -------------------------------------------------- | ------------------------------------------------------------- | ----------------------------------------------------------------
1   | Create customer                                    | **PUT** `/v1/customers`                                       | `{"name":"{customer_name}"}`
2   | Create contact                                     | **PUT** `/v1/customers/contacts`                              | `{"customer_id":"{customer_id}","contact":"{customer_contact}"}`
3   | List customers                                     | **GET** `/v1/customers`                                       | &ndash;
4   | Retrieve customer                                  | **GET** `/v1/customers/{customer_id}`                         | &ndash;
5   | List contacts for a given customer                 | **GET** `/v1/customers/{customer_id}/contacts`                | &ndash;
6   | List contacts of a given type for a given customer | **GET** `/v1/customers/{customer_id}/contacts/{contact_type}` | &ndash;

* The `{customer_name}` placeholder is a string &mdash; it usually means the full name given to a newly created customer.
* The `{customer_id}` placeholder is a decimal positive integer number, greater than `0`.
* The `{customer_contact}` placeholder is a string &mdash; it denotes a newly created customer contact (phone or email).
* The `{contact_type}` placeholder is a string and can take one of two possible values, case-insensitive: `phone` or `email`.

The following command-line snippets display the exact usage for these endpoints (the **cURL** utility is used as an example to access them):

1. **Create customer**

```
$ curl -vXPUT http://localhost:8765/v1/customers \
       -H 'content-type: application/json' \
       -d '{"name":"Jamison Palmer"}'
...
> PUT /v1/customers HTTP/1.1
...
> content-type: application/json
> Content-Length: 25
...
< HTTP/1.1 201 Created
...
< Location: /v1/customers/4
...
< Content-Type: application/json
...
{"id":4,"name":"Jamison Palmer"}
```

2. **Create contact**

```
$ curl -vXPUT http://localhost:8765/v1/customers/contacts \
       -H 'content-type: application/json' \
       -d '{"customer_id":"4","contact":"+12197654320"}'
...
> PUT /v1/customers/contacts HTTP/1.1
...
> content-type: application/json
> Content-Length: 44
...
< HTTP/1.1 201 Created
...
< Location: /v1/customers/4/contacts/phone
...
< Content-Type: application/json
...
{"contact":"+12197654320"}
```

Or create **email** contact:

```
$ curl -vXPUT http://localhost:8765/v1/customers/contacts \
       -H 'content-type: application/json' \
       -d '{"customer_id":"4","contact":"jamison.palmer@example.com"}'
...
> PUT /v1/customers/contacts HTTP/1.1
...
> content-type: application/json
> Content-Length: 58
...
< HTTP/1.1 201 Created
...
< Location: /v1/customers/4/contacts/email
...
< Content-Type: application/json
...
{"contact":"jamison.palmer@example.com"}
```

3. **List customers**

```
$ curl -v http://localhost:8765/v1/customers
...
> GET /v1/customers HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
[{"id":1,"name":"Jammy Jellyfish"},{"id":2,"name":"Noble Numbat"},{"id":3,"name":"Noah Henley"},{"id":4,"name":"Jamison Palmer"},{"id":5,"name":"Madeline Michelle"},{"id":6,"name":"Al Lester"},{"id":7,"name":"Sarah Kitteringham"},{"id":8,"name":"Just Name"}]
```

4. **Retrieve customer**

```
$ curl -v http://localhost:8765/v1/customers/4
...
> GET /v1/customers/4 HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
{"id":4,"name":"Jamison Palmer"}
```

5. **List contacts for a given customer**

```
$ curl -v http://localhost:8765/v1/customers/4/contacts
...
> GET /v1/customers/4/contacts HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
[{"contact":"+12197654320"},{"contact":"jamison.palmer@example.com"}]
```

6. **List contacts of a given type for a given customer**

```
$ curl -v http://localhost:8765/v1/customers/4/contacts/phone
...
> GET /v1/customers/4/contacts/phone HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
[{"contact":"+12197654320"}]
```

Or list **email** contacts:

```
$ curl -v http://localhost:8765/v1/customers/4/contacts/email
...
> GET /v1/customers/4/contacts/email HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
[{"contact":"jamison.palmer@example.com"}]
```

### Logging

The microservice has the ability to log messages to a logfile and to the Unix syslog facility. When running under Ubuntu Server (not in a Docker container), logs can be seen and analyzed in an ordinary fashion, by `tail`ing the `log/customers-api-lite.log` logfile:

```
$ tail -f log/customers-api-lite.log
...
[2024-10-31][14:30:43][INFO ]  Undertow started on port 8765 (http) with context path '/'
[2024-10-31][14:30:43][INFO ]  Started CustomersApiLiteApp in 5.859 seconds (process running for 7.184)
[2024-10-31][14:30:43][DEBUG]  [Customers API Lite]
[2024-10-31][14:30:43][DEBUG]  [org.sqlite.JDBC]
[2024-10-31][14:30:43][DEBUG]  [jdbc:sqlite:data/db/customers-api-lite.db]
[2024-10-31][14:30:43][INFO ]  Server started on port 8765
[2024-10-31][14:35:13][INFO ]  Commencing graceful shutdown. Waiting for active requests to complete
[2024-10-31][14:35:13][INFO ]  Graceful shutdown complete
[2024-10-31][14:35:13][INFO ]  stopping server: Undertow - 2.3.17.Final
[2024-10-31][14:35:13][INFO ]  Server stopped
```

Messages registered by the Unix system logger can be seen and analyzed using the `journalctl` utility:

```
$ journalctl -f
...
Oct 31 14:30:43 <hostname> java[<pid>]: [Customers API Lite]
Oct 31 14:30:43 <hostname> java[<pid>]: [org.sqlite.JDBC]
Oct 31 14:30:43 <hostname> java[<pid>]: [jdbc:sqlite:data/db/customers-api-lite.db]
Oct 31 14:30:43 <hostname> java[<pid>]: Server started on port 8765
Oct 31 14:35:13 <hostname> java[<pid>]: Server stopped
```

### Error handling

When the URI path or request body passed in an incoming request contains inappropriate input, the microservice will respond with the **HTTP 400 Bad Request** status code, including a specific response body in JSON representation which describes a possible cause of underlying client error, like the following:

**TBD** :cd:

---

**TBD** :dvd:
