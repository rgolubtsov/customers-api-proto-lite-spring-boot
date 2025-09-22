# Customers API Lite microservice prototype :small_blue_diamond: :coffee:

**A Spring Boot-based application, designed and intended to be run as a microservice,
<br />implementing a special Customers API prototype with a smart yet simplified data scheme**

This repo is dedicated to develop a microservice that implements a prototype of REST API service for ordinary Customers operations like adding/retrieving a Customer to/from the database, also doing the same ops with Contacts (phone or email) which belong to a Customer account.

The data scheme chosen is very simplified and consisted of only three SQL database tables, but that's quite sufficient because the service operates on only two entities: a **Customer** and a **Contact** (phone or email). And a set of these operations is limited to the following ones:

* Create a new customer (put customer data to the database).
* Create a new contact for a given customer (put a contact regarding a given customer to the database).
* Retrieve from the database and list all customer profiles.
* Retrieve profile details for a given customer from the database.
* Retrieve from the database and list all contacts associated with a given customer.
* Retrieve from the database and list all contacts of a given type associated with a given customer.

As it is clearly seen, there are no *mutating*, usually expected operations like *update* or *delete* an entity and that's made intentionally.

The microservice incorporates the **[SQLite](https://sqlite.org "A small, fast, self-contained, high-reliability, full-featured, SQL database engine")** database as its persistent store. It is located in the `data/db/` directory as an XZ-compressed database file with minimal initial data &mdash; actually having two Customers and by six Contacts for each Customer. The database file is automatically decompressed during build process of the microservice and ready to use as is even when containerized with Docker.

Generally speaking, this project might be explored as a PoC (proof of concept) on how to amalgamate Java REST API service backed by SQLite database, running standalone as a conventional daemon in host or VM environment, or in a containerized form as usually widely adopted nowadays.

Surely, one may consider this project to be suitable for a wide variety of applied areas and may use this prototype as: (1) a template for building similar microservices, (2) for evolving it to make something more universal, or (3) to simply explore it and take out some snippets and techniques from it for *educational purposes*, etc.

---

## Table of Contents

* **[Building](#building)**
  * **[Creating a Docker image](#creating-a-docker-image)**
* **[Running](#running)**
  * **[Running a Docker image](#running-a-docker-image)**
  * **[Exploring a Docker image payload](#exploring-a-docker-image-payload)**
* **[Consuming](#consuming)**
  * **[Logging](#logging)**
  * **[Error handling](#error-handling)**

## Building

The microservice might be built and run successfully under **Ubuntu Server (Ubuntu 24.04.3 LTS x86-64)** and **Arch Linux** (both proven). &mdash; First install the necessary dependencies (`openjdk-21-jdk-headless`, `make`, `docker.io`):

* In Ubuntu Server:

```
$ sudo apt-get update && \
  sudo apt-get install openjdk-21-jdk-headless make docker.io -y
...
```

* In Arch Linux:

```
$ sudo pacman -Syu jdk21-openjdk make docker
...
```

---

**Build** the microservice using **Gradle Wrapper**:

```
$ ./gradlew -q clean
$
$ ./gradlew -q compileJava
$
$ ./gradlew -q build && \
if [ -f data/db/customers-api-lite.db.xz ]; then \
    unxz data/db/customers-api-lite.db.xz; \
fi
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

### Creating a Docker image

**Build** a Docker image for the microservice:

```
$ # Pull the JRE image first, if not already there:
$ sudo docker pull azul/zulu-openjdk-alpine:17-jre-headless-latest
...
$ # Then build the microservice image:
$ sudo docker build -tcustomersapi/api-lite .
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
$ java -jar build/libs/customers-api-lite-0.3.5.jar; echo $?
...
```

To run the microservice as a *true* daemon, i.e. in the background, redirecting all the console output to `/dev/null`, the following form of invocation of its executable can be used:

```
$ java -jar build/libs/customers-api-lite-0.3.5.jar > /dev/null 2>&1 &
[1] <pid>
```

**Note:** This will suppress all the console output only; logging to a logfile and to the Unix syslog will remain unchanged.

The daemonized microservice then can be stopped at any time by issuing the following command:

```
$ kill -SIGTERM <pid>
$
[1]+  Exit 143                java -jar build/libs/customers-api-lite-0.3.5.jar > /dev/null 2>&1
```

### Running a Docker image

**Run** a Docker image of the microservice, deleting all stopped containers prior to that (if any):

```
$ sudo docker rm `sudo docker ps -aq`; \
  export PORT=8765 && sudo docker run -dp${PORT}:${PORT} --name api-lite customersapi/api-lite; echo $?
...
```

### Exploring a Docker image payload

The following is not necessary but might be considered somewhat interesting &mdash; to look up into the running container, and check out that the microservice's JAR layers, logfile, and accompanied SQLite database are at their expected places and in effect:

```
$ sudo docker ps -a
CONTAINER ID   IMAGE                   COMMAND                    CREATED              STATUS              PORTS                                       NAMES
<container_id> customersapi/api-lite   "java org.springfram..."   About a minute ago   Up About a minute   0.0.0.0:8765->8765/tcp, :::8765->8765/tcp   api-lite
$
$ sudo docker exec -it api-lite sh; echo $?
/var/tmp $
/var/tmp $ java --version
openjdk 17.0.13 2024-10-15 LTS
OpenJDK Runtime Environment Zulu17.54+21-CA (build 17.0.13+11-LTS)
OpenJDK 64-Bit Server VM Zulu17.54+21-CA (build 17.0.13+11-LTS, mixed mode, sharing)
/var/tmp $
/var/tmp $ ls -al
total 32
drwxrwxrwt    1 root     root          4096 Jan 19 20:00 .
drwxr-xr-x    1 root     root          4096 Jan  8 11:04 ..
drwxr-xr-x    1 nobody   nobody        4096 Jan 19 19:50 BOOT-INF
drwxr-xr-x    3 nobody   nobody        4096 Jan 19 19:50 META-INF
drwxr-xr-x    1 daemon   daemon        4096 Jan 19 19:50 data
drwxr-xr-x    2 daemon   daemon        4096 Jan 19 20:00 log
drwxr-xr-x    3 nobody   nobody        4096 Jan 19 18:10 org
/var/tmp $
/var/tmp $ ls -al BOOT-INF/ BOOT-INF/classes/com/customers/proto/liteapi/ data/db/ log/
BOOT-INF/:
total 24
drwxr-xr-x    1 nobody   nobody        4096 Jan 19 19:50 .
drwxrwxrwt    1 root     root          4096 Jan 19 20:00 ..
drwxr-xr-x    3 nobody   nobody        4096 Jan 19 19:50 classes
-rw-r--r--    1 nobody   nobody        1907 Jan 19  2025 classpath.idx
-rw-r--r--    1 nobody   nobody         212 Jan 19  2025 layers.idx
drwxr-xr-x    2 nobody   nobody        4096 Jan 19 18:10 lib

BOOT-INF/classes/com/customers/proto/liteapi/:
total 56
drwxr-xr-x    2 nobody   nobody        4096 Jan 19 19:50 .
drwxr-xr-x    3 nobody   nobody        4096 Jan 19 19:50 ..
-rw-r--r--    1 nobody   nobody        4458 Jan 19 19:50 CustomersApiLiteApp.class
-rw-r--r--    1 nobody   nobody        9931 Jan 19 19:50 CustomersApiLiteController.class
-rw-r--r--    1 nobody   nobody         668 Jan 19 19:50 CustomersApiLiteEntityContact.class
-rw-r--r--    1 nobody   nobody         869 Jan 19 19:50 CustomersApiLiteEntityCustomer.class
-rw-r--r--    1 nobody   nobody         654 Jan 19 19:50 CustomersApiLiteEntityError.class
-rw-r--r--    1 nobody   nobody        3067 Jan 19 19:50 CustomersApiLiteExceptionHandler.class
-rw-r--r--    1 nobody   nobody        4867 Jan 19 19:50 CustomersApiLiteHelper.class
-rw-r--r--    1 nobody   nobody        1692 Jan 19 19:50 CustomersApiLiteModel.class

data/db/:
total 32
drwxr-xr-x    1 daemon   daemon        4096 Jan 19 19:50 .
drwxr-xr-x    1 daemon   daemon        4096 Jan 19 19:50 ..
-rw-rw-r--    1 daemon   daemon       24576 Jan 19 17:50 customers-api-lite.db

log/:
total 12
drwxr-xr-x    2 daemon   daemon        4096 Jan 19 20:00 .
drwxrwxrwt    1 root     root          4096 Jan 19 20:00 ..
-rw-r--r--    1 daemon   daemon        1183 Jan 19 20:00 customers-api-lite.log
/var/tmp $
/var/tmp $ netstat -plunt
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp        0      0 :::8765                 :::*                    LISTEN      1/java
/var/tmp $
/var/tmp $ ps ax
PID   USER     TIME  COMMAND
    1 daemon    0:20 java org.springframework.boot.loader.launch.JarLauncher
   28 daemon    0:00 sh
   50 daemon    0:00 ps ax
/var/tmp $
/var/tmp $ exit # Or simply <Ctrl-D>.
0
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

The following command-line snippets display the exact usage for these endpoints (the **cURL** utility is used as an example to access them)^:

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

> ^ The given names in customer accounts and in email contacts (in samples above) are for demonstrational purposes only. They have nothing common WRT any actual, ever really encountered names elsewhere.

### Logging

The microservice has the ability to log messages to a logfile and to the Unix syslog facility. To enable debug logging, the `logger.debug.enabled` setting in the microservice main config file `src/main/resources/application.properties` should be set to `true` *before building the microservice*. When running under Ubuntu Server (not in a Docker container), logs can be seen and analyzed in an ordinary fashion, by `tail`ing the `log/customers-api-lite.log` logfile:

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

Inside the running container logs might be queried also by `tail`ing the `log/customers-api-lite.log` logfile:

```
/var/tmp $ tail -f log/customers-api-lite.log
...
[2025-01-19][20:00:26][INFO ]  Undertow started on port 8765 (http) with context path '/'
[2025-01-19][20:00:26][INFO ]  Started CustomersApiLiteApp in 8.342 seconds (process running for 11.369)
[2025-01-19][20:00:26][DEBUG]  [Customers API Lite]
[2025-01-19][20:00:26][DEBUG]  [org.sqlite.JDBC]
[2025-01-19][20:00:26][DEBUG]  [jdbc:sqlite:data/db/customers-api-lite.db]
[2025-01-19][20:00:26][INFO ]  Server started on port 8765
[2025-01-19][21:12:10][INFO ]  Initializing Spring DispatcherServlet 'dispatcherServlet'
[2025-01-19][21:12:10][INFO ]  Initializing Servlet 'dispatcherServlet'
[2025-01-19][21:12:10][INFO ]  Completed initialization in 6 ms
[2025-01-19][21:12:10][DEBUG]  [Saturday Sunday]
[2025-01-19][21:12:11][INFO ]  HikariPool-1 - Starting...
[2025-01-19][21:12:11][INFO ]  HikariPool-1 - Added connection org.sqlite.jdbc4.JDBC4Connection@e3daeac
[2025-01-19][21:12:11][INFO ]  HikariPool-1 - Start completed.
[2025-01-19][21:12:11][DEBUG]  [3|Saturday Sunday]
[2025-01-19][21:15:02][DEBUG]  customer_id=3
[2025-01-19][21:15:02][DEBUG]  [Saturday.Sunday@example.com]
[2025-01-19][21:15:02][DEBUG]  [email|Saturday.Sunday@example.com]
[2025-01-19][21:17:10][DEBUG]  customer_id=3 | contact_type=email
[2025-01-19][21:17:10][DEBUG]  [Saturday.Sunday@example.com]
...
```

And of course, Docker itself gives the possibility to read log messages by using the corresponding command for that:

```
$ sudo docker logs -f api-lite
...
[2025-01-19][20:00:26][INFO ]  Undertow started on port 8765 (http) with context path '/'
[2025-01-19][20:00:26][INFO ]  Started CustomersApiLiteApp in 8.342 seconds (process running for 11.369)
[2025-01-19][20:00:26][DEBUG]  [Customers API Lite]
[2025-01-19][20:00:26][DEBUG]  [org.sqlite.JDBC]
[2025-01-19][20:00:26][DEBUG]  [jdbc:sqlite:data/db/customers-api-lite.db]
[2025-01-19][20:00:26][INFO ]  Server started on port 8765
[2025-01-19][21:12:10][INFO ]  Initializing Spring DispatcherServlet 'dispatcherServlet'
[2025-01-19][21:12:10][INFO ]  Initializing Servlet 'dispatcherServlet'
[2025-01-19][21:12:10][INFO ]  Completed initialization in 6 ms
[2025-01-19][21:12:10][DEBUG]  [Saturday Sunday]
[2025-01-19][21:12:11][INFO ]  HikariPool-1 - Starting...
[2025-01-19][21:12:11][INFO ]  HikariPool-1 - Added connection org.sqlite.jdbc4.JDBC4Connection@e3daeac
[2025-01-19][21:12:11][INFO ]  HikariPool-1 - Start completed.
[2025-01-19][21:12:11][DEBUG]  [3|Saturday Sunday]
[2025-01-19][21:15:02][DEBUG]  customer_id=3
[2025-01-19][21:15:02][DEBUG]  [Saturday.Sunday@example.com]
[2025-01-19][21:15:02][DEBUG]  [email|Saturday.Sunday@example.com]
[2025-01-19][21:17:10][DEBUG]  customer_id=3 | contact_type=email
[2025-01-19][21:17:10][DEBUG]  [Saturday.Sunday@example.com]
[2025-01-19][21:20:20][INFO ]  Commencing graceful shutdown. Waiting for active requests to complete
[2025-01-19][21:20:20][INFO ]  Graceful shutdown complete
[2025-01-19][21:20:20][INFO ]  stopping server: Undertow - 2.3.18.Final
[2025-01-19][21:20:20][INFO ]  Destroying Spring FrameworkServlet 'dispatcherServlet'
[2025-01-19][21:20:20][INFO ]  HikariPool-1 - Shutdown initiated...
[2025-01-19][21:20:20][INFO ]  HikariPool-1 - Shutdown completed.
[2025-01-19][21:20:20][INFO ]  Server stopped
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
