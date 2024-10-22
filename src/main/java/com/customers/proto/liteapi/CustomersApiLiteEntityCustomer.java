/*
 *src/main/java/com/customers/proto/liteapi/CustomersApiLiteEntityCustomer.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.1.5
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
 * @version 0.1.5
 * @since   0.1.1
 */
public class CustomersApiLiteEntityCustomer {
    /** The customer ID. */
    private long id;

    /** The customer name. */
    private String name;

    /**
     * The accessor method for the customer ID.
     *
     * @return The customer ID.
     */
    public long getId() {
        return id;
    }

    /**
     * The mutator method for the customer ID.
     *
     * @param _id A new value for the customer ID.
     */
    public void setId(final long _id) {
        id = _id;
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
     * The mutator method for the customer name.
     *
     * @param _name A new value for the customer name.
     */
    public void setName(final String _name) {
        name = _name;
    }
}

// vim:set nu et ts=4 sw=4:
