/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteEntityError.java
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

/**
 * The POJO representation of the Error entity, returning in the response.
 * Mainly it regards to one of the <strong>4xx Client Error</strong> section's
 * errors occurred during processing the request.
 *
 * @version 0.2.5
 * @since   0.2.5
 */
public class CustomersApiLiteEntityError {
    /** The error message. */
    private String error;

    /**
     * The accessor method for the error message.
     *
     * @return The error message.
     */
    public String getError() {
        return error;
    }

    /**
     * The mutator method for the error message.
     *
     * @param _error A new value for the error message.
     */
    public void setError(final String _error) {
        error = _error;
    }
}

// vim:set nu et ts=4 sw=4:
