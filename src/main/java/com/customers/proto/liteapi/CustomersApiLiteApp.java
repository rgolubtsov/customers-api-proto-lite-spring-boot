/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteApp.java
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

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;

/**
 * The startup class of the microservice.
 *
 * @version 0.0.1
 * @since   0.0.1
 */
@SpringBootApplication
public class CustomersApiLiteApp {
    /**
     * The microservice entry point.
     *
     * @param args An array of command-line arguments.
     */
    public static void main(final String[] args) {
        // Starting up the bundled web server.
        ConfigurableApplicationContext ctx
            = SpringApplication.run(CustomersApiLiteApp.class, args);

        // Getting the port number used to run the bundled web server.
        String server_port = ctx.getEnvironment().getProperty(SERVER_PORT);

        l.info(MSG_SERVER_STARTED + server_port);
    }
}

// vim:set nu et ts=4 sw=4:
