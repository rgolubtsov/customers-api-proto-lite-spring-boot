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
$ # Pull the Azul Zulu JRE (based on Alpine Linux) image first, if not already there:
$ sudo docker pull azul/zulu-openjdk-alpine:21-jre-headless-latest
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
$ java -jar build/libs/customers-api-lite-0.3.8.jar; echo $?
...
```

To run the microservice as a *true* daemon, i.e. in the background, redirecting all the console output to `/dev/null`, the following form of invocation of its executable can be used:

```
$ java -jar build/libs/customers-api-lite-0.3.8.jar > /dev/null 2>&1 &
[1] <pid>
```

**Note:** This will suppress all the console output only; logging to a logfile and to the Unix syslog will remain unchanged.

The daemonized microservice then can be stopped at any time by issuing the following command:

```
$ kill -SIGTERM <pid>
$
[1]+  Exit 143                java -jar build/libs/customers-api-lite-0.3.8.jar > /dev/null 2>&1
```

### Running a Docker image

**Run** a Docker image of the microservice, deleting all stopped containers prior to that (if any):

```
$ sudo docker rm `sudo docker ps -aq`; \
  export PORT=8765 && sudo docker run -dp${PORT}:${PORT} --name api-lite customersapi/api-lite; echo $?
...
```

### Exploring a Docker image payload

The following is not necessary but might be considered somewhat interesting &mdash; to look up into the running container, and check out that the microservice's Java classes, configs, logfile, and accompanied SQLite database are at their expected places and in effect:

```
$ sudo docker ps -a
CONTAINER ID   IMAGE                   COMMAND                    CREATED              STATUS              PORTS                                       NAMES
<container_id> customersapi/api-lite   "java org.springfram..."   About a minute ago   Up About a minute   0.0.0.0:8765->8765/tcp, :::8765->8765/tcp   api-lite
$
$ sudo docker exec -it api-lite sh; echo $?
/var/tmp/api-lite $
/var/tmp/api-lite $ uname -a
Linux <container_id> 6.8.0-79-generic #79-Ubuntu SMP PREEMPT_DYNAMIC Tue Aug 12 14:42:46 UTC 2025 x86_64 Linux
/var/tmp/api-lite $
/var/tmp/api-lite $ java --version
openjdk 21.0.8 2025-07-15 LTS
OpenJDK Runtime Environment Zulu21.44+17-CA (build 21.0.8+9-LTS)
OpenJDK 64-Bit Server VM Zulu21.44+17-CA (build 21.0.8+9-LTS, mixed mode, sharing)
/var/tmp/api-lite $
/var/tmp/api-lite $ ls -al
total 36
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 22:10 .
drwxrwxrwt    1 root     root          4096 Sep 25 21:10 ..
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 BOOT-INF
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 META-INF
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 data
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 etc
drwxr-xr-x    2 daemon   daemon        4096 Sep 25 22:10 log
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 org
/var/tmp/api-lite $
/var/tmp/api-lite $ ls -al BOOT-INF/ BOOT-INF/classes/ BOOT-INF/classes/com/customers/proto/liteapi/ data/db/ etc/ log/
BOOT-INF/:
total 24
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 .
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 22:10 ..
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 classes
-rw-r--r--    1 daemon   daemon        2312 Feb  1  1980 classpath.idx
-rw-r--r--    1 daemon   daemon         212 Feb  1  1980 layers.idx
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 lib

BOOT-INF/classes/:
total 20
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 .
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 ..
-rw-r--r--    1 daemon   daemon         894 Feb  1  1980 application.properties
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 com
-rw-r--r--    1 daemon   daemon        2279 Feb  1  1980 log4j.properties

BOOT-INF/classes/com/customers/proto/liteapi/:
total 60
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 .
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 ..
-rw-r--r--    1 daemon   daemon        1778 Feb  1  1980 ApiLiteController$ExceptionHandler$Error.class
-rw-r--r--    1 daemon   daemon        4687 Feb  1  1980 ApiLiteController$ExceptionHandler.class
-rw-r--r--    1 daemon   daemon       11489 Feb  1  1980 ApiLiteController.class
-rw-r--r--    1 daemon   daemon        4385 Feb  1  1980 ApiLiteCore.class
-rw-r--r--    1 daemon   daemon        5450 Feb  1  1980 ApiLiteHelper.class
-rw-r--r--    1 daemon   daemon        1577 Feb  1  1980 ApiLiteModel$Contact.class
-rw-r--r--    1 daemon   daemon        1716 Feb  1  1980 ApiLiteModel$Customer.class
-rw-r--r--    1 daemon   daemon        1808 Feb  1  1980 ApiLiteModel.class

data/db/:
total 32
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 .
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 ..
-rw-rw-r--    1 daemon   daemon       24576 Sep 25 19:25 customers-api-lite.db

etc/:
total 8
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 21:10 .
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 22:10 ..
lrwxrwxrwx    1 daemon   daemon          42 Sep 25 21:10 settings.conf -> ../BOOT-INF/classes/application.properties

log/:
total 12
drwxr-xr-x    2 daemon   daemon        4096 Sep 25 22:10 .
drwxr-xr-x    1 daemon   daemon        4096 Sep 25 22:10 ..
-rw-r--r--    1 daemon   daemon        1109 Sep 25 22:10 customers-api-lite.log
/var/tmp/api-lite $
/var/tmp/api-lite $ netstat -plunt
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp        0      0 :::8765                 :::*                    LISTEN      1/java
/var/tmp/api-lite $
/var/tmp/api-lite $ ps aux
PID   USER     TIME  COMMAND
    1 daemon    0:20 java org.springframework.boot.loader.launch.JarLauncher
   26 daemon    0:00 sh
   48 daemon    0:00 ps aux
/var/tmp/api-lite $
/var/tmp/api-lite $ exit # Or simply <Ctrl-D>.
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
< Location: /v1/customers/3
...
< Content-Type: application/json
...
{"id":3,"name":"Jamison Palmer"}
```

2. **Create contact**

```
$ curl -vXPUT http://localhost:8765/v1/customers/contacts \
       -H 'content-type: application/json' \
       -d '{"customer_id":"3","contact":"+12197654320"}'
...
> PUT /v1/customers/contacts HTTP/1.1
...
> content-type: application/json
> Content-Length: 44
...
< HTTP/1.1 201 Created
...
< Location: /v1/customers/3/contacts/phone
...
< Content-Type: application/json
...
{"contact":"+12197654320"}
```

Or create **email** contact:

```
$ curl -vXPUT http://localhost:8765/v1/customers/contacts \
       -H 'content-type: application/json' \
       -d '{"customer_id":"3","contact":"jamison.palmer@example.com"}'
...
> PUT /v1/customers/contacts HTTP/1.1
...
> content-type: application/json
> Content-Length: 58
...
< HTTP/1.1 201 Created
...
< Location: /v1/customers/3/contacts/email
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
[{"id":1,"name":"Jammy Jellyfish"},{"id":2,"name":"Noble Numbat"},{"id":3,"name":"Jamison Palmer"},{"id":4,"name":"Sarah Kitteringham"}]
```

4. **Retrieve customer**

```
$ curl -v http://localhost:8765/v1/customers/3
...
> GET /v1/customers/3 HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
{"id":3,"name":"Jamison Palmer"}
```

5. **List contacts for a given customer**

```
$ curl -v http://localhost:8765/v1/customers/3/contacts
...
> GET /v1/customers/3/contacts HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
[{"contact":"+12197654320"},{"contact":"+12197654321"},{"contact":"+12197654322"},{"contact":"jamison.palmer@example.com"},{"contact":"jp@example.com"},{"contact":"jpalmer@example.com"}]
```

6. **List contacts of a given type for a given customer**

```
$ curl -v http://localhost:8765/v1/customers/3/contacts/phone
...
> GET /v1/customers/3/contacts/phone HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
[{"contact":"+12197654320"},{"contact":"+12197654321"},{"contact":"+12197654322"}]
```

Or list **email** contacts:

```
$ curl -v http://localhost:8765/v1/customers/3/contacts/email
...
> GET /v1/customers/3/contacts/email HTTP/1.1
...
< HTTP/1.1 200 OK
...
< Content-Type: application/json
...
[{"contact":"jamison.palmer@example.com"},{"contact":"jpalmer@example.com"},{"contact":"jp@example.com"}]
```

> ^ The given names in customer accounts and in email contacts (in samples above) are for demonstrational purposes only. They have nothing common WRT any actual, ever really encountered names elsewhere.

### Logging

The microservice has the ability to log messages to a logfile and to the Unix syslog facility. To enable debug logging, the `logger.debug.enabled` setting in the microservice main config file `etc/settings.conf` should be set to `true` *before building the microservice*. When running under Ubuntu Server or Arch Linux (not in a Docker container), logs can be seen and analyzed in an ordinary fashion, by `tail`ing the `log/customers-api-lite.log` logfile:

```
$ tail -f log/customers-api-lite.log
...
[2025-09-25][23:15:50] [DEBUG] [Customers API Lite]
[2025-09-25][23:15:50] [DEBUG] [org.sqlite.JDBC]
[2025-09-25][23:15:50] [INFO ] Server started on port 8765
[2025-09-25][23:20:50] [INFO ] Initializing Spring DispatcherServlet 'dispatcherServlet'
[2025-09-25][23:20:50] [INFO ] Initializing Servlet 'dispatcherServlet'
[2025-09-25][23:20:50] [INFO ] Completed initialization in 3 ms
[2025-09-25][23:20:50] [DEBUG] [PUT]
[2025-09-25][23:20:50] [DEBUG] [Saturday Sunday]
[2025-09-25][23:20:50] [INFO ] HikariPool-1 - Starting...
[2025-09-25][23:20:50] [INFO ] HikariPool-1 - Added connection org.sqlite.jdbc4.JDBC4Connection@848e88b
[2025-09-25][23:20:50] [INFO ] HikariPool-1 - Start completed.
[2025-09-25][23:20:50] [DEBUG] [5|Saturday Sunday]
[2025-09-25][23:25:50] [DEBUG] [PUT]
[2025-09-25][23:25:50] [DEBUG] customer_id=5
[2025-09-25][23:25:50] [DEBUG] [Saturday.Sunday@example.com]
[2025-09-25][23:25:50] [DEBUG] [email|Saturday.Sunday@example.com]
[2025-09-25][23:30:00] [DEBUG] [GET]
[2025-09-25][23:30:00] [DEBUG] customer_id=5
[2025-09-25][23:30:00] [DEBUG] [5|Saturday Sunday]
[2025-09-25][23:35:10] [DEBUG] [GET]
[2025-09-25][23:35:10] [DEBUG] customer_id=5 | contact_type=email
[2025-09-25][23:35:10] [DEBUG] [Saturday.Sunday@example.com]
[2025-09-25][23:40:20] [INFO ] Commencing graceful shutdown. Waiting for active requests to complete
[2025-09-25][23:40:20] [INFO ] Graceful shutdown complete
[2025-09-25][23:40:20] [INFO ] stopping server: Undertow - 2.3.19.Final
[2025-09-25][23:40:20] [INFO ] Destroying Spring FrameworkServlet 'dispatcherServlet'
[2025-09-25][23:40:20] [INFO ] HikariPool-1 - Shutdown initiated...
[2025-09-25][23:40:20] [INFO ] HikariPool-1 - Shutdown completed.
[2025-09-25][23:40:20] [INFO ] Server stopped
```

Messages registered by the Unix system logger can be seen and analyzed using the `journalctl` utility:

```
$ journalctl -f
...
Sep 25 23:15:50 <hostname> java[<pid>]: [Customers API Lite]
Sep 25 23:15:50 <hostname> java[<pid>]: [org.sqlite.JDBC]
Sep 25 23:15:50 <hostname> java[<pid>]: Server started on port 8765
Sep 25 23:20:50 <hostname> java[<pid>]: [PUT]
Sep 25 23:20:50 <hostname> java[<pid>]: [Saturday Sunday]
Sep 25 23:20:50 <hostname> java[<pid>]: [5|Saturday Sunday]
Sep 25 23:25:50 <hostname> java[<pid>]: [PUT]
Sep 25 23:25:50 <hostname> java[<pid>]: customer_id=5
Sep 25 23:25:50 <hostname> java[<pid>]: [Saturday.Sunday@example.com]
Sep 25 23:25:50 <hostname> java[<pid>]: [email|Saturday.Sunday@example.com]
Sep 25 23:30:00 <hostname> java[<pid>]: [GET]
Sep 25 23:30:00 <hostname> java[<pid>]: customer_id=5
Sep 25 23:30:00 <hostname> java[<pid>]: [5|Saturday Sunday]
Sep 25 23:35:10 <hostname> java[<pid>]: [GET]
Sep 25 23:35:10 <hostname> java[<pid>]: customer_id=5 | contact_type=email
Sep 25 23:35:10 <hostname> java[<pid>]: [Saturday.Sunday@example.com]
Sep 25 23:40:20 <hostname> java[<pid>]: Server stopped
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
$ curl http://localhost:8765/v1/customers/=qwerty4838=-i-.--089asdf..nj524987
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
$
$ curl http://localhost:8765/v1/customers/3..,,7/contacts
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
$
$ curl http://localhost:8765/v1/customers/--089asdf../contacts/email
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
$
$ curl -XPUT http://localhost:8765/v1/customers/contacts \
       -H 'content-type: application/json' \
       -d '{"customer_id":"3","contact":"12197654320--089asdf../nj524987"}'
{"error":"HTTP 400 Bad Request: Request is malformed. Please check your inputs."}
```
