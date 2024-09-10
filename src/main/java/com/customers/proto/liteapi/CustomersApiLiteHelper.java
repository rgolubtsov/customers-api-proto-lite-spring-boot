/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteHelper.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.0.1
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

/**
 * The helper class for the microservice.
 *
 * @version 0.0.1
 * @since   0.0.1
 */
public class CustomersApiLiteHelper {
    // Common notification messages.
    public static final String MSG_SERVER_STARTED = "Server started on port ";
    public static final String MSG_SERVER_STOPPED = "Server stopped";

    // Application properties key for the server port number.
    public static final String SERVER_PORT = "server.port";

    /** The SLF4J logger. */
    public static final Logger l = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
}

// vim:set nu et ts=4 sw=4:
