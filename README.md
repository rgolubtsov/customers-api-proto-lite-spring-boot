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
$ java -jar build/libs/customers-api-lite-0.3.0.jar; echo $?
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
[2025-01-19][03:00:41][INFO ]  Undertow started on port 8765 (http) with context path '/'
[2025-01-19][03:00:41][INFO ]  Started CustomersApiLiteApp in 7.709 seconds (process running for 9.915)
[2025-01-19][03:00:41][DEBUG]  [Customers API Lite]
[2025-01-19][03:00:41][DEBUG]  [org.sqlite.JDBC]
[2025-01-19][03:00:41][DEBUG]  [jdbc:sqlite:data/db/customers-api-lite.db]
[2025-01-19][03:00:41][INFO ]  Server started on port 8765
[2025-01-19][03:06:02][INFO ]  Initializing Spring DispatcherServlet 'dispatcherServlet'
[2025-01-19][03:06:02][INFO ]  Initializing Servlet 'dispatcherServlet'
[2025-01-19][03:06:02][INFO ]  Completed initialization in 4 ms
[2025-01-19][03:06:02][DEBUG]  customer_id=12
[2025-01-19][03:06:02][INFO ]  HikariPool-1 - Starting...
[2025-01-19][03:06:03][INFO ]  HikariPool-1 - Added connection org.sqlite.jdbc4.JDBC4Connection@44f35bb0
[2025-01-19][03:06:03][INFO ]  HikariPool-1 - Start completed.
[2025-01-19][03:06:03][DEBUG]  [12|Saturday Sunday]
[2025-01-19][03:09:32][DEBUG]  customer_id=12
[2025-01-19][03:09:32][DEBUG]  [Saturday.Sunday@example.com]
[2025-01-19][03:09:33][DEBUG]  [email|Saturday.Sunday@example.com]
[2025-01-19][03:09:37][DEBUG]  customer_id=12 | contact_type=email
[2025-01-19][03:09:37][DEBUG]  [Saturday.Sunday@example.com]
[2025-01-19][03:10:06][INFO ]  Commencing graceful shutdown. Waiting for active requests to complete
[2025-01-19][03:10:06][INFO ]  Graceful shutdown complete
[2025-01-19][03:10:06][INFO ]  stopping server: Undertow - 2.3.18.Final
[2025-01-19][03:10:06][INFO ]  Destroying Spring FrameworkServlet 'dispatcherServlet'
[2025-01-19][03:10:06][INFO ]  HikariPool-1 - Shutdown initiated...
[2025-01-19][03:10:06][INFO ]  HikariPool-1 - Shutdown completed.
[2025-01-19][03:10:06][INFO ]  Server stopped
```

Messages registered by the Unix system logger can be seen and analyzed using the `journalctl` utility:

```
$ journalctl -f
...
Jan 19 03:00:41 <hostname> java[<pid>]: [Customers API Lite]
Jan 19 03:00:41 <hostname> java[<pid>]: [org.sqlite.JDBC]
Jan 19 03:00:41 <hostname> java[<pid>]: [jdbc:sqlite:data/db/customers-api-lite.db]
Jan 19 03:00:41 <hostname> java[<pid>]: Server started on port 8765
Jan 19 03:06:02 <hostname> java[<pid>]: customer_id=12
Jan 19 03:06:03 <hostname> java[<pid>]: [12|Saturday Sunday]
Jan 19 03:09:32 <hostname> java[<pid>]: customer_id=12
Jan 19 03:09:32 <hostname> java[<pid>]: [Saturday.Sunday@example.com]
Jan 19 03:09:33 <hostname> java[<pid>]: [email|Saturday.Sunday@example.com]
Jan 19 03:09:37 <hostname> java[<pid>]: customer_id=12 | contact_type=email
Jan 19 03:09:37 <hostname> java[<pid>]: [Saturday.Sunday@example.com]
Jan 19 03:10:06 <hostname> java[<pid>]: Server stopped
```

### Error handling

When the URI path or request body passed in an incoming request contains inappropriate input, the microservice will respond with the **HTTP 400 Bad Request** status code, including a specific response body in JSON representation which may describe a possible cause of underlying client error, like the following:

```
$ curl 'http://localhost:8765/v1/customers/4/contacts=qwerty4838&=-i-.;--089asdf../nj524987'
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
$
$ curl http://localhost:8765/v1QWERTY/customers/4..,,7/contacts/email
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
$
$ curl -XPUT http://localhost:8765/v1/customers \
       -H 'content-type: application/--089asdf../nj524987' \
       -d '{"name":"Saturday Sunday"}'
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
```

Or even simpler:

```
$ curl http://localhost:8765/v1/customers/
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
```
