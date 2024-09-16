/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteController.java
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

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;

/**
 * The controller class of the microservice.
 *
 * @version 0.0.9
 * @since   0.0.9
 */
@RestController
@RequestMapping(SLASH + REST_PREFIX)
public class CustomersApiLiteController {
    /**
     * The &quot;<code>/customers/new</code>&quot; <b>PUT</b> endpoint.
     * <br />
     * <br />Creates a new customer (puts customer's data to the database
     * for their search and retrieval).
     *
     * @return The ResponseEntity object with a specific HTTP status code
     *         provided (and the response body in JSON representation
     *         in case of the request payload is not valid).
     */
    @PutMapping(SLASH + REST_NEW)
    public String add_customer() {
        // TODO: Implement creating a new customer.

        return (SLASH + REST_NEW);
    }
}

// vim:set nu et ts=4 sw=4:
