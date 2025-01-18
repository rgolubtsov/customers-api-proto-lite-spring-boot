/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteHelper.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.2.5
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
import java.util.Properties;

import org.graylog2.syslog4j.impl.unix.UnixSyslog;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * The helper class for the microservice.
 *
 * @version 0.2.5
 * @since   0.0.1
 */
public class CustomersApiLiteHelper {
    // Helper constants.
    public static final int    EXIT_FAILURE =   1; //    Failing exit status.
    public static final int    EXIT_SUCCESS =   0; // Successful exit status.
    public static final String EMPTY_STRING =  "";
    public static final String SPACE        = " ";
    public static final String SLASH        = "/";
    public static final String EQUALS       = "=";
    public static final String V_BAR        = "|";
    public static final String O_BRACKET    = "[";
    public static final String C_BRACKET    = "]";

    // Common error messages.
    public static final String ERR_PORT_VALID_MUST_BE_POSITIVE_INT
        = "Valid server port must be a positive integer value, "
        + "in the range 1024 .. 49151. Please set the correct "
        + "server port number in application properties.";
    public static final String ERR_APP_PROPS_UNABLE_TO_GET
        = "Unable to get application properties.";
    public static final String ERR_CANNOT_START_SERVER
        = "FATAL: Cannot start server ";
    public static final String ERR_ADDR_ALREADY_IN_USE
        = "due to address requested already in use. Quitting...";
    public static final String ERR_SERV_UNKNOWN_REASON
        = "for an unknown reason. Quitting...";
    public static final String ERR_REQ_MALFORMED
        = "HTTP 400 Bad Request: Request is malformed. "
        + "Please check your inputs.";

    // Common notification messages.
    public static final String MSG_SERVER_STARTED = "Server started on port ";
    public static final String MSG_SERVER_STOPPED = "Server stopped";

    /** The application properties filename. */
    public static final String APP_PROPS = "application.properties";

    /** The minimum port number allowed. */
    public static final int MIN_PORT = 1024;

    /** The maximum port number allowed. */
    public static final int MAX_PORT = 49151;

    /** The default server port number. */
    public static final int DEF_PORT = 8080;

    // Application properties key for the debug logging enabler.
    public static final String DBG_LOG_ENBLR = "logger.debug.enabled";

    // Application properties key for the microservice application name.
    public static final String APP_NAME = "spring.application.name";

    // Application properties key for the server port number.
    public static final String SERVER_PORT = "server.port";

    // The name for the data source bean, used to connect to the database.
    public static final String DATA_SOURCE = "dataSource";

    // REST URI path-related constants.
    public static final String REST_VERSION   =  "v1";
    public static final String REST_PREFIX    =  "customers";
    public static final String REST_CUST_ID   = "{customer_id}";
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
    public static final String DB_T_CONT_C_CUST_ID = "customer_id";

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

    /**
     * Retrieves the port number used to run the server,
     * from application properties.
     *
     * @return The port number on which the server has to be run.
     */
    public static int get_server_port() {
        var server_port_ = _get_props().getProperty(SERVER_PORT);
        var server_port  = 0;

        try { server_port = Integer.parseInt(server_port_); }
        catch (NumberFormatException e) { /* Using the last `else' block. */ }

        if (server_port != 0) {
            if ((server_port >= MIN_PORT) && (server_port <= MAX_PORT)) {
                return server_port;
            } else {
                l.error(ERR_PORT_VALID_MUST_BE_POSITIVE_INT);

                System.exit(EXIT_FAILURE);
            }
        } else {
            l.error(ERR_PORT_VALID_MUST_BE_POSITIVE_INT);

            System.exit(EXIT_FAILURE);
        }

        return DEF_PORT; // <== For the sake of suppressing compilation errors.
    }

    // Helper method. Used to get the application properties object.
    private static final Properties _get_props() {
        var props = new Properties();

        var loader = CustomersApiLiteHelper.class.getClassLoader();

        var data = loader.getResourceAsStream(APP_PROPS);

        try {
            props.load(data);
            data.close();
        } catch (java.io.IOException e) {
            l.error(ERR_APP_PROPS_UNABLE_TO_GET);
        }

        return props;
    }
}

// vim:set nu et ts=4 sw=4:
