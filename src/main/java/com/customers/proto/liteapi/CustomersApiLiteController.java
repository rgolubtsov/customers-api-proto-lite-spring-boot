/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteController.java
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

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;

/**
 * The controller class of the microservice.
 *
 * @version 0.1.1
 * @since   0.1.0
 */
@RestController // <method> /customers ----------------------------------------
@RequestMapping(SLASH + REST_PREFIX)
public class CustomersApiLiteController {
    /**
     * The <code>PUT /customers</code> endpoint.
     * <br />
     * <br />Creates a new customer (puts customer data to the database).
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided (and the response body
     *         in JSON representation in case of request payload is not valid).
     *
     */ // PUT /customers -----------------------------------------------------
    @PutMapping
    public ResponseEntity<String> add_customer() {
        // TODO: Implement creating a new customer.

        var resp = new ResponseEntity<String>(
            O_BRACKET + SLASH + C_BRACKET, HttpStatus.CREATED);

        String respBody = resp.getBody();

        _dbg(respBody);

        return resp;
    }

    /**
     * The <code>PUT /customers/{customer_id}/contact</code> endpoint.
     * <br />
     * <br />Creates a new contact for a given customer (puts a contact
     * regarding a given customer to the database).
     *
     * @param customer_id The customer ID used to associate a newly created
     *                    contact with this customer.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided (and the response body
     *         in JSON representation in case of request payload is not valid).
     *
     */ // PUT /customers/{customer_id}/contact -------------------------------
    @PutMapping(SLASH + REST_CUST_ID + SLASH + REST_CONTACT)
    public ResponseEntity<String> add_contact(
        @PathVariable String customer_id) {

        _dbg(CUST_ID + EQUALS + customer_id);

        // TODO: Implement creating a new contact.

        var resp = new ResponseEntity<String>(
            SLASH + customer_id + SLASH + REST_CONTACT, HttpStatus.CREATED);

        String respBody = resp.getBody();

        _dbg(respBody);

        return resp;
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
    public ResponseEntity<String> list_customers() {
        // TODO: Implement retrieving and listing all customer profiles.
        /* FIXME: sqlite> .width -11
                  sqlite> select id   as 'Customer ID',
                     ...>        name as 'Customer Name'
                     ...>  from
                     ...>        customers
                     ...>  order by
                     ...>        id; */

        var resp = new ResponseEntity<String>(SLASH, HttpStatus.OK);

        String respBody = resp.getBody();

        _dbg(respBody);

        return resp;
    }

    /**
     * The <code>GET /customers/{customer_id}</code> endpoint.
     * <br />
     * <br />Retrieves profile details for a given customer from the database.
     *
     * @param customer_id The customer ID used to retrieve
     *                    customer profile data.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing profile details
     *         for a given customer (in the response body
     *         in JSON representation).
     *
     */ // GET /customers/{customer_id} ---------------------------------------
    @GetMapping(SLASH + REST_CUST_ID)
    public ResponseEntity<String> get_customer(
        @PathVariable String customer_id) {

        _dbg(CUST_ID + EQUALS + customer_id);

        // TODO: Implement retrieving profile details for a given customer.
        /* FIXME: sqlite> .width -11
                  sqlite> select   cust.id      as 'Customer ID',
                     ...>          cust.name    as 'Customer Name',
                     ...>        phones.contact as 'Phone(s)',
                     ...>        emails.contact as 'Email(s)'
                     ...>  from
                     ...>        customers      cust,
                     ...>        contact_phones phones,
                     ...>        contact_emails emails
                     ...>  where
                     ...>       (cust.id = phones.customer_id ) and
                     ...>       (cust.id = emails.customer_id ) and
                     ...>       (cust.id =       {customer_id})
                     ...>  order by
                     ...>        emails.contact; */

        var resp = new ResponseEntity<String>(
            SLASH + customer_id, HttpStatus.OK);

        String respBody = resp.getBody();

        _dbg(respBody);

        return resp;
    }

    /**
     * The <code>GET /customers/{customer_id}/contacts</code> endpoint.
     * <br />
     * <br />Retrieves from the database and lists all contacts
     * associated with a given customer.
     *
     * @param customer_id The customer ID used to retrieve contacts
     *                    which belong to this customer.
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing a list of all contacts
     *         associated with a given customer (in the response body
     *         in JSON representation).
     *
     */ // GET /customers/{customer_id}/contacts ------------------------------
    @GetMapping(SLASH + REST_CUST_ID + SLASH + REST_CONTACTS)
    public ResponseEntity<String> list_contacts(
        @PathVariable String customer_id) {

        _dbg(CUST_ID + EQUALS + customer_id);

        // TODO: Implement retrieving and listing all contacts
        //       for a given customer.

        var resp = new ResponseEntity<String>(
            SLASH + customer_id + SLASH + REST_CONTACTS, HttpStatus.OK);

        String respBody = resp.getBody();

        _dbg(respBody);

        return resp;
    }

    /**
     * The <code>GET /customers/{customer_id}/contacts/{contact_type}</code>
     * endpoint.
     * <br />
     * <br />Retrieves from the database and lists all contacts of a given type
     * associated with a given customer.
     *
     * @param customer_id  The customer ID used to retrieve contacts
     *                     which belong to this customer.
     * @param contact_type The particular type of contacts to retrieve
     *                     (e.g. phone, email, postal address, etc.).
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided, containing a list of all contacts
     *         of a given type associated with a given customer
     *         (in the response body in JSON representation).
     *
     */ // GET /customers/{customer_id}/contacts/{contact_type} ---------------
    @GetMapping(SLASH + REST_CUST_ID + SLASH + REST_CONTACTS
                                     + SLASH + REST_CONT_TYPE)
    public ResponseEntity<String> list_contacts_by_type(
        @PathVariable String customer_id,
        @PathVariable String contact_type) {

        _dbg(CUST_ID   + EQUALS + customer_id + SPACE + V_BAR + SPACE
           + CONT_TYPE + EQUALS + contact_type);

        // TODO: Implement retrieving and listing all contacts of a given type
        //       for a given customer.

        var resp = new ResponseEntity<String>(
            SLASH + customer_id + SLASH + REST_CONTACTS
                                + SLASH + contact_type, HttpStatus.OK);

        String respBody = resp.getBody();

        _dbg(respBody);

        return resp;
    }
}

// vim:set nu et ts=4 sw=4:
