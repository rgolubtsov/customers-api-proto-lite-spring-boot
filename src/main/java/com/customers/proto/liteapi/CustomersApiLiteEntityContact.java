/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteEntityContact.java
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

/**
 * The POJO representation of the Contact entity, returning in the response.
 *
 * @version 0.1.9
 * @since   0.1.5
 */
public class CustomersApiLiteEntityContact {
    /** The customer's contact. */
    private String contact;

    /**
     * The accessor method for the customer's contact.
     *
     * @return The customer's contact.
     */
    public String getContact() {
        return contact;
    }

    /**
     * The mutator method for the customer's contact.
     *
     * @param _contact A new value for the customer's contact.
     */
    public void setContact(final String _contact) {
        contact = _contact;
    }
}

// vim:set nu et ts=4 sw=4:
