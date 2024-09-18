#
# Makefile
# =============================================================================
# Customers API Lite microservice prototype. Version 0.1.0
# =============================================================================
# A Spring Boot-based application, designed and intended to be run
# as a microservice, implementing a special Customers API prototype
# with a smart yet simplified data scheme.
# =============================================================================
# (See the LICENSE file at the top of the source tree.)
#

SERV = build
JARS = $(SERV)/libs

# Specify flags and other vars here.
GRADLE_W = ./gradlew
G_WFLAGS = -q

# Making the first target (JVM classes).
$(SERV):
	$(GRADLE_W) $(G_WFLAGS) compileJava

# Making the second target (JAR bundles).
$(JARS):
	$(GRADLE_W) $(G_WFLAGS) build

.PHONY: all clean

all: $(JARS)

clean:
	$(GRADLE_W) $(G_WFLAGS) clean

# vim:set nu et ts=4 sw=4:
