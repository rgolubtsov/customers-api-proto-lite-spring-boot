/*
 *src/main/java/com/customers/proto/liteapi/CustomersApiLiteEntityCustomer.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.1.1
 * ============================================================================
 * A Spring Boot-based application, designed and intended to be run
 * as a microservice, implementing a special Customers API prototype
 * with a smart yet simplified data scheme.
 * ============================================================================
 * (See the LICENSE file at the top of the source tree.)
 */

package com.customers.proto.liteapi;

/**
 * The POJO representation of the Customer entity, returning in the response.
 *
 * @version 0.1.1
 * @since   0.1.1
 */
public class CustomersApiLiteEntityCustomer {
    /** The customer ID. */
    private final long id;

    /** The customer name. */
    private final String name;

    /**
     * The accessor method for the customer ID.
     *
     * @return The customer ID.
     */
    public long getId() {
        return id;
    }

    /**
     * The accessor method for the customer name.
     *
     * @return The customer name.
     */
    public String getName() {
        return name;
    }

    /**
     * The effective constructor for the Customer entity.
     *
     * @param _id   The customer ID.
     * @param _name The customer name.
     */
    public CustomersApiLiteEntityCustomer(final long _id, final String _name) {
        id   = _id;
        name = _name;
    }
}

// vim:set nu et ts=4 sw=4:
