/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteController.java
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

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.List;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;
import static com.customers.proto.liteapi.CustomersApiLiteModel.*;

/**
 * The controller class of the microservice.
 *
 * @version 0.1.5
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
     * @param payload The <code>Map<String, Object></code> object,
     *                containing the request body exactly in the form
     *                as <code>{"name":"{customer_name}"}</code>.
     *                It should usually be passed with the accompanied request
     *                header <code>content-type</code> just like the following:
     *                <br /><code>-H 'content-type: application/json' -d '{"name":"{customer_name}"}'</code>
     *
     * @return The <code>ResponseEntity</code> object with a specific
     *         HTTP status code provided (and the response body
     *         in JSON representation in case of request payload is not valid).
     *
     */ // PUT /customers -----------------------------------------------------
    @PutMapping
    public ResponseEntity<CustomersApiLiteEntityCustomer> add_customer(
        @RequestBody Map<String,String> payload) {

        _dbg(O_BRACKET + payload.get(DB_T_CUST_C_NAME) + C_BRACKET);

        i_cust.execute(payload);

        var customer = c.sql(SQL_GET_ALL_CUSTOMERS + SQL_DESC_LIMIT_1)
                        .query(CustomersApiLiteEntityCustomer.class)
                        .single();

        var resp = new ResponseEntity<CustomersApiLiteEntityCustomer>(
            customer, HttpStatus.CREATED);

        _dbg(O_BRACKET + resp.getBody().getName() + C_BRACKET);

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
    public ResponseEntity<List> list_customers() {
        var customers = c.sql(SQL_GET_ALL_CUSTOMERS)
                         .query(CustomersApiLiteEntityCustomer.class)
                         .list();

        if (customers.isEmpty()) {
            customers.add(new CustomersApiLiteEntityCustomer());
        }

        var resp = new ResponseEntity<List>(customers, HttpStatus.OK);

        _dbg(O_BRACKET + ((CustomersApiLiteEntityCustomer)
                           resp.getBody().get(0)).getName() + C_BRACKET);

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
    public ResponseEntity<CustomersApiLiteEntityCustomer> get_customer(
        @PathVariable String customer_id) {

        _dbg(CUST_ID + EQUALS + customer_id);

        var cust_id = 0L;

        try {
            cust_id = Long.parseLong(customer_id);
        } catch (NumberFormatException e) {
            _dbg(O_BRACKET + O_BRACKET + customer_id
               + C_BRACKET + C_BRACKET);
        }

        var customer = c.sql(SQL_GET_CUSTOMER_BY_ID)
                        .param(cust_id)
                        .query(CustomersApiLiteEntityCustomer.class)
                        .optional()
                        .orElse(null);

        if (customer == null) {
            customer = new CustomersApiLiteEntityCustomer();
        }

        var resp = new ResponseEntity<CustomersApiLiteEntityCustomer>(
            customer, HttpStatus.OK);

        _dbg(O_BRACKET + resp.getBody().getName() + C_BRACKET);

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
    public ResponseEntity<List> list_contacts(
        @PathVariable String customer_id) {

        _dbg(CUST_ID + EQUALS + customer_id);

        var cust_id = 0L;

        try {
            cust_id = Long.parseLong(customer_id);
        } catch (NumberFormatException e) {
            _dbg(O_BRACKET + O_BRACKET + customer_id
               + C_BRACKET + C_BRACKET);
        }

        var contacts = c.sql(SQL_GET_ALL_CONTACTS)
                        .param(cust_id) // <== For retrieving phones.
                        .param(cust_id) // <== For retrieving emails.
                        .query(CustomersApiLiteEntityContact.class)
                        .list();

        if (contacts.isEmpty()) {
            contacts.add(new CustomersApiLiteEntityContact());
        }

        var resp = new ResponseEntity<List>(contacts, HttpStatus.OK);

        _dbg(O_BRACKET + ((CustomersApiLiteEntityContact)
                           resp.getBody().get(0)).getContact() + C_BRACKET);

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
    public ResponseEntity<List> list_contacts_by_type(
        @PathVariable String customer_id,
        @PathVariable String contact_type) {

        _dbg(CUST_ID   + EQUALS + customer_id + SPACE + V_BAR + SPACE
           + CONT_TYPE + EQUALS + contact_type);

        var cust_id = 0L;

        try {
            cust_id = Long.parseLong(customer_id);
        } catch (NumberFormatException e) {
            _dbg(O_BRACKET + O_BRACKET + customer_id
               + C_BRACKET + C_BRACKET);
        }

        var sql_query = EMPTY_STRING;

               if (contact_type.compareToIgnoreCase(PHONE) == 0) {
            sql_query = SQL_GET_CONTACTS_BY_TYPE[0];
        } else if (contact_type.compareToIgnoreCase(EMAIL) == 0) {
            sql_query = SQL_GET_CONTACTS_BY_TYPE[1];
        } else {
            sql_query = SQL_GET_CONTACTS_BY_TYPE[2];
        }

        var contacts = c.sql(sql_query)
                        .param(cust_id)
                        .query(CustomersApiLiteEntityContact.class)
                        .list();

        if (contacts.isEmpty()) {
            contacts.add(new CustomersApiLiteEntityContact());
        }

        var resp = new ResponseEntity<List>(contacts, HttpStatus.OK);

        _dbg(O_BRACKET + ((CustomersApiLiteEntityContact)
                           resp.getBody().get(0)).getContact() + C_BRACKET);

        return resp;
    }
}

// vim:set nu et ts=4 sw=4:
