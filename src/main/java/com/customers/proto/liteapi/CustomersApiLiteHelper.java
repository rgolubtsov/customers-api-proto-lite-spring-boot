/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteHelper.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.0.9
 * ============================================================================
 * A Spring Boot-based application, designed and intended to be run
 * as a microservice, implementing a special Customers API prototype
 * with a smart yet simplified data scheme.
 * ============================================================================
 * (See the LICENSE file at the top of the source tree.)
 */

package com.customers.proto.liteapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import org.graylog2.syslog4j.impl.unix.UnixSyslog;

/**
 * The helper class for the microservice.
 *
 * @version 0.0.9
 * @since   0.0.1
 */
public class CustomersApiLiteHelper {
    // Helper constants.
    public static final String SLASH        = "/";
    public static final String O_BRACKET    = "[";
    public static final String C_BRACKET    = "]";

    // Common notification messages.
    public static final String MSG_SERVER_STARTED = "Server started on port ";
    public static final String MSG_SERVER_STOPPED = "Server stopped";

    // Application properties key for the debug logger enabler.
    public static final String DBG_LOG_ENBLR = "logger.debug.enabled";

    // Application properties key for the microservice application name.
    public static final String APP_NAME = "spring.application.name";

    // Application properties key for the server port number.
    public static final String SERVER_PORT = "server.port";

    // REST URI path-related constants.
    public static final String REST_PREFIX   =  "customers";
    public static final String REST_CUST_ID  = "{customer_id}";
    public static final String REST_CONTACT  =  "contact";
    public static final String REST_CONTACTS =  "contacts";

    /** The SLF4J logger. */
    public static final Logger l = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    /** The Unix system logger. */
    public static UnixSyslog s;
}

// vim:set nu et ts=4 sw=4:
