#
# Dockerfile
# =============================================================================
# Customers API Lite microservice prototype. Version 0.3.7
# =============================================================================
# A Spring Boot-based application, designed and intended to be run
# as a microservice, implementing a special Customers API prototype
# with a smart yet simplified data scheme.
# =============================================================================
# (See the LICENSE file at the top of the source tree.)
#

# === Stage 1: Extract JAR layers =============================================
FROM       azul/zulu-openjdk-alpine:21-jre-headless-latest AS layers
USER       nobody
WORKDIR    var/tmp
COPY       build/libs/customers-api-lite-0.3.7.jar api-lite.jar
RUN        ["java", "-Djarmode=tools", "-jar", "api-lite.jar", "extract", "--layers", "--launcher", "--destination", "layers"]

# === Stage 2: Run the microservice ===========================================
FROM       azul/zulu-openjdk-alpine:21-jre-headless-latest
USER       daemon
WORKDIR    var/tmp
ARG        LAYERS=var/tmp/layers
COPY       --from=layers ${LAYERS}/dependencies       api-lite/
COPY       --from=layers ${LAYERS}/spring-boot-loader api-lite/
COPY       --from=layers ${LAYERS}/application        api-lite/
COPY       etc                                        api-lite/etc/
COPY       data/db                                    api-lite/data/db/
WORKDIR    api-lite
USER       root
RUN        ["ln", "-sfnv", "../BOOT-INF/classes/application.properties", "etc/settings.conf"]
RUN        ["chown", "-R", "daemon:daemon", "."]
USER       daemon
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

# vim:set nu ts=4 sw=4:
