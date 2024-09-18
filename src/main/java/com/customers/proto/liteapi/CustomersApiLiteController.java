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
@RestController // <method> /customers ----------------------------------------
@RequestMapping(SLASH + REST_PREFIX)
public class CustomersApiLiteController {
    /**
     * The <code>PUT /customers</code> endpoint.
     * <br />
     * <br />Creates a new customer (puts customer's data to the database
     * for their further search and retrieval).
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided (and the response body
     *         in JSON representation in case of request payload is not valid).
     *
     */ // PUT /customers -----------------------------------------------------
    @PutMapping
    public String add_customer() {
        // TODO: Implement creating a new customer.

        return (O_BRACKET + SLASH + C_BRACKET);
    }

    /**
     * The <code>PUT /customers/{customer_id}/contact</code> endpoint.
     * <br />
     * <br />Creates a new contact for a given customer (puts a contact,
     * regarding a given customer, to the database
     * for its search and retrieval).
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided (and the response body
     *         in JSON representation in case of request payload is not valid).
     *
     */ // PUT /customers/{customer_id}/contact -------------------------------
    @PutMapping(SLASH + REST_CUST_ID + SLASH + REST_CONTACT)
    public String add_contact() {
        // TODO: Implement creating a new contact.

        return (SLASH + REST_CUST_ID + SLASH + REST_CONTACT);
    }

    /**
     * The <code>GET /customers</code> endpoint.
     * <br />
     * <br />Retrieves from the database and lists all customer profiles.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing a list of all customer
     *         profiles (in the response body in JSON representation).
     *
     */ // GET /customers -----------------------------------------------------
    @GetMapping
    public String list_customers() {
        // TODO: Implement retrieving and listing all customer profiles.

        return (SLASH);
    }

    /**
     * The <code>GET /customers/{customer_id}</code> endpoint.
     * <br />
     * <br />Retrieves profile details for a given customer from the database.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing profile details
     *         for a given customer (in the response body
     *         in JSON representation).
     *
     */ // GET /customers/{customer_id} ---------------------------------------
    @GetMapping(SLASH + REST_CUST_ID)
    public String get_customer() {
        // TODO: Implement retrieving profile details for a given customer.

        return (SLASH + REST_CUST_ID);
    }

    /**
     * The <code>GET /customers/{customer_id}/contacts</code> endpoint.
     * <br />
     * <br />Retrieves from the database and lists all contacts
     * associated with a given customer.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing a list of all contacts
     *         associated with a given customer (in the response body
     *         in JSON representation).
     *
     */ // GET /customers/{customer_id}/contacts ------------------------------
    @GetMapping(SLASH + REST_CUST_ID + SLASH + REST_CONTACTS)
    public String list_contacts() {
        // TODO: Implement retrieving and listing all contacts
        //       for a given customer.

        return (SLASH + REST_CUST_ID + SLASH + REST_CONTACTS);
    }

    /**
     * The <code>GET /customers/{customer_id}/contacts/{contact_type}</code>
     * endpoint.
     * <br />
     * <br />Retrieves from the database and lists all contacts of a given type
     * associated with a given customer.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing a list of all contacts
     *         of a given type associated with a given customer
     *         (in the response body in JSON representation).
     *
     */ // GET /customers/{customer_id}/contacts/{contact_type} ---------------
    @GetMapping(SLASH + REST_CUST_ID + SLASH + REST_CONTACTS
                                     + SLASH + REST_CONT_TYPE)
    public String list_contacts_by_type() {
        // TODO: Implement retrieving and listing all contacts of a given type
        //       for a given customer.

        return (SLASH + REST_CUST_ID + SLASH + REST_CONTACTS
                                     + SLASH + REST_CONT_TYPE);
    }
}

// vim:set nu et ts=4 sw=4:
