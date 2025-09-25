#
# Makefile
# =============================================================================
# Customers API Lite microservice prototype. Version 0.3.8
# =============================================================================
# A Spring Boot-based application, designed and intended to be run
# as a microservice, implementing a special Customers API prototype
# with a smart yet simplified data scheme.
# =============================================================================
# (See the LICENSE file at the top of the source tree.)
#

SERV    = build
JARS    = $(SERV)/libs
DB_PATH = data/db
DB_FILE = customers-api-lite.db.xz

# Specify flags and other vars here.
GRADLE_W = ./gradlew
G_WFLAGS = -q
UNXZ     = unxz

# Making the first target (JVM classes).
$(SERV):
	$(GRADLE_W) $(G_WFLAGS) compileJava

# Making the second target (JAR bundles).
$(JARS):
	$(GRADLE_W) $(G_WFLAGS) build && \
	if [ -f $(DB_PATH)/$(DB_FILE) ]; then \
	    $(UNXZ) $(DB_PATH)/$(DB_FILE); \
	fi

.PHONY: all clean

all: $(JARS)

clean:
	$(GRADLE_W) $(G_WFLAGS) clean

# vim:set nu et ts=4 sw=4:
