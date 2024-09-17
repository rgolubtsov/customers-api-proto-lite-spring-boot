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
import org.springframework.web.bind.annotation.GetMapping;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;

/**
 * The controller class of the microservice.
 *
 * @version 0.0.9
 * @since   0.0.9
 */
@RestController
@RequestMapping(SLASH + REST_PREFIX) // <method> /customers -------------------
public class CustomersApiLiteController {
    /**
     * The &quot;<code>/customers/new</code>&quot; <b>PUT</b> endpoint.
     * <br />
     * <br />Creates a new customer (puts customer's data to the database
     * for their search and retrieval).
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided (and the response body
     *         in JSON representation in case of request payload is not valid).
     */
    @PutMapping(SLASH + REST_NEW) // PUT /customers/new -----------------------
    public String add_customer() {
        // TODO: Implement creating a new customer.

        return (SLASH + REST_NEW);
    }

    /**
     * The &quot;<code>/customers/new-contact</code>&quot; <b>PUT</b> endpoint.
     * <br />
     * <br />Creates a new contact for a given customer (puts a contact,
     * regarding a given customer, to the database
     * for its search and retrieval).
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided (and the response body
     *         in JSON representation in case of request payload is not valid).
     */
    @PutMapping(SLASH + REST_NEW_CONTACT) // PUT /customers/new-contact -------
    public String add_contact() {
        // TODO: Implement creating a new contact.

        return (SLASH + REST_NEW_CONTACT);
    }

    /**
     * The &quot;<code>/customers</code>&quot; <b>GET</b> endpoint.
     * <br />
     * <br />Retrieves from the database and lists all customer profiles.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing a list of all customer
     *         profiles (in the response body in JSON representation).
     */
    @GetMapping // GET /customers ---------------------------------------------
    public String list_customers() {
        // TODO: Implement retrieving and listing all customer profiles.

        return (SLASH);
    }

    /**
     * The &quot;<code>/customers/{customer_id}</code>&quot;
     * <b>GET</b> endpoint.
     * <br />
     * <br />Retrieves profile details for a single customer from the database.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing profile details
     *         for a single customer (in the response body
     *         in JSON representation).
     */
    @GetMapping(SLASH + REST_GET) // GET /customers/{customer_id} -------------
    public String get_customer() {
        // TODO: Implement retrieving profile details for a customer.

        return (SLASH + REST_GET);
    }

    /**
     * The &quot;<code>/customers/{customer_id}/contacts</code>&quot;
     * <b>GET</b> endpoint.
     * <br />
     * <br />Retrieves from the database and lists all contacts
     * associated with a given customer.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing a list of all contacts
     *         associated with a given customer (in the response body
     *         in JSON representation).
     */ // GET /customers/{customer_id}/contacts ------------------------------
    @GetMapping(SLASH + REST_GET + SLASH + REST_CONTACTS)
    public String list_contacts() {
        // TODO: Implement retrieving and listing all contacts
        //       for a given customer.

        return (SLASH + REST_GET + SLASH + REST_CONTACTS);
    }
}

// vim:set nu et ts=4 sw=4:
