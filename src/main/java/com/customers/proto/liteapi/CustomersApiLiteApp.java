/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteApp.java
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

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import org.graylog2.syslog4j.impl.unix.UnixSyslogConfig;
import org.graylog2.syslog4j.impl.unix.UnixSyslog;
import org.graylog2.syslog4j.SyslogIF;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;

/**
 * The startup class of the microservice.
 *
 * @version 0.0.9
 * @since   0.0.1
 */
@SpringBootApplication
public class CustomersApiLiteApp implements DisposableBean {
    /**
     * The microservice entry point.
     *
     * @param args An array of command-line arguments.
     */
    public static void main(final String[] args) {
        // Starting up the bundled web server.
        ConfigurableApplicationContext ctx
            = SpringApplication.run(CustomersApiLiteApp.class, args);

        // Opening the system logger.
        // Calling <syslog.h> openlog(NULL, LOG_CONS | LOG_PID, LOG_DAEMON);
        UnixSyslogConfig cfg = new UnixSyslogConfig();
        cfg.setIdent(null); cfg.setFacility(SyslogIF.FACILITY_DAEMON);
        s = new UnixSyslog(); s.initialize (SyslogIF.UNIX_SYSLOG,cfg);

        ConfigurableEnvironment env = ctx.getEnvironment();

        boolean debug_log_enabled
            = Boolean.parseBoolean(env.getProperty(DBG_LOG_ENBLR));

        if (debug_log_enabled) {
            String app_name = env.getProperty(APP_NAME);

            l.debug(O_BRACKET + app_name + C_BRACKET);
            s.debug(O_BRACKET + app_name + C_BRACKET);
        }

        // Getting the port number used to run the bundled web server.
        String server_port = env.getProperty(SERVER_PORT);

        l.info(MSG_SERVER_STARTED + server_port);
        s.info(MSG_SERVER_STARTED + server_port);
    }

    /** Gets called when the server is about to be stopped. */
    @Override
    public void destroy() throws Exception {
        l.info(MSG_SERVER_STOPPED);
        s.info(MSG_SERVER_STOPPED);

        // Closing the system logger.
        // Calling <syslog.h> closelog();
        s.shutdown();
    }
}

// vim:set nu et ts=4 sw=4:
