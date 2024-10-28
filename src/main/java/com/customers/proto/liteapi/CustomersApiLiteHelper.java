/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteHelper.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.1.9
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

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * The helper class for the microservice.
 *
 * @version 0.1.9
 * @since   0.0.1
 */
public class CustomersApiLiteHelper {
    // Helper constants.
    public static final String EMPTY_STRING =  "";
    public static final String SPACE        = " ";
    public static final String SLASH        = "/";
    public static final String EQUALS       = "=";
    public static final String V_BAR        = "|";
    public static final String O_BRACKET    = "[";
    public static final String C_BRACKET    = "]";

    // Common notification messages.
    public static final String MSG_SERVER_STARTED = "Server started on port ";
    public static final String MSG_SERVER_STOPPED = "Server stopped";

    // Application properties key for the debug logging enabler.
    public static final String DBG_LOG_ENBLR = "logger.debug.enabled";

    // Application properties key for the microservice application name.
    public static final String APP_NAME = "spring.application.name";

    // Application properties key for the server port number.
    public static final String SERVER_PORT = "server.port";

    // The name for the data source bean, used to connect to the database.
    public static final String DATA_SOURCE = "dataSource";

    // REST URI path-related constants.
    public static final String REST_PREFIX    =  "customers";
    public static final String REST_CUST_ID   = "{customer_id}";
    public static final String REST_CONTACT   =  "contact";
    public static final String REST_CONTACTS  =  "contacts";
    public static final String REST_CONT_TYPE = "{contact_type}";
    public static final String PHONE          =  "phone";
    public static final String EMAIL          =  "email";

    // HTTP request path variable names.
    public static final String CUST_ID   = "customer_id";
    public static final String CONT_TYPE = "contact_type";

    // Database table and column names for insert operations.
    public static final String DB_T_CUSTOMERS      = "customers";
    public static final String DB_T_CONTACT_PHONES = "contact_phones";
    public static final String DB_T_CONTACT_EMAILS = "contact_emails";
    public static final String DB_T_CUST_C_NAME    = "name";
    public static final String DB_T_CONT_C_CONTACT = "contact";

    // Regex patterns for contact phones and emails.
    public static final String PHONE_REGEX = "^\\+\\d{9,14}";
    public static final String EMAIL_REGEX = ".{1,63}@.{3,190}";

    /** The debug logging enabler. */
    public static boolean dbg = false;

    /** The SLF4J logger. */
    public static final Logger l = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    /** The Unix system logger. */
    public static UnixSyslog s;

    /** The Spring JDBC Client. */
    public static JdbcClient c;

    /** The Spring JDBC DB metadata-driven <em>insert client</em> No.1. */
    public static SimpleJdbcInsert i_cust;

    /** The Spring JDBC DB metadata-driven <em>insert client</em> No.2. */
    public static SimpleJdbcInsert[] i_cont;

    // Helper method. Used to log messages for debugging aims in a free form.
    public static void _dbg(final String message) {
        if (dbg) {
            l.debug(message);
            s.debug(message);
        }
    }
}

// vim:set nu et ts=4 sw=4:
