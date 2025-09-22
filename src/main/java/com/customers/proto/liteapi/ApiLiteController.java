/*
 * src/main/java/com/customers/proto/liteapi/ApiLiteController.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.3.1
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.List;

import java.net.URI;
import java.net.URISyntaxException;

import static com.customers.proto.liteapi.ApiLiteHelper.*;
import static com.customers.proto.liteapi.ApiLiteModel.*;

/**
 * The controller class of the microservice. Defines all the REST API endpoints
 * implemented for <em>this</em> microservice by design.
 * <br />
 * <br />&lt;HTTP request method&gt; <code>/v1/customers</code>.
 *
 * @version 0.3.1
 * @since   0.1.0
 */
@RestController
@RequestMapping(SLASH + REST_VERSION + SLASH + REST_PREFIX)
public class ApiLiteController {
    /**
     * The <code>PUT /v1/customers</code> endpoint.
     * <br />
     * <br />Creates a new customer (puts customer data to the database).
     *
     * @param payload The <code>Map<String,String></code> object,
     *                containing the request body exactly in the form
     *                as <code>{"name":"{customer_name}"}</code>.
     *                It should be passed with the accompanied request header
     *                <code>content-type</code> just like the following:
     *                <br /><code>-H 'content-type: application/json' -d '{"name":"{customer_name}"}'</code>
     *                <br /><code>{customer_name}</code> is a name assigned
     *                to a newly created customer.
     *
     * @return The <code>ResponseEntity<CustomersApiLiteEntityCustomer></code>
     *         object with the <code>201 Created</code> HTTP status code,
     *         the <code>Location</code> response header (among others),
     *         and the response body in JSON representation, containing profile
     *         details of a newly created customer.
     *         May return client or server error depending on incoming request.
     */
    @PutMapping
    public ResponseEntity<CustomersApiLiteEntityCustomer> add_customer(
        @RequestBody Map<String,String> payload) throws URISyntaxException {

        _dbg(O_BRACKET + payload.get(DB_T_CUST_C_NAME) + C_BRACKET);

        i_cust.execute(payload);

        var customer = c.sql(SQL_GET_ALL_CUSTOMERS + SQL_DESC_LIMIT_1)
                        .query(CustomersApiLiteEntityCustomer.class)
                        .single();

        var hdrs = new HttpHeaders();
            hdrs.setLocation(new URI(SLASH + REST_VERSION
                                   + SLASH + REST_PREFIX
                                   + SLASH + customer.getId()));

        var resp = new ResponseEntity<CustomersApiLiteEntityCustomer>(
            customer, hdrs, HttpStatus.CREATED); // <== HTTP 201 Created

        var body = resp.getBody();

        _dbg(O_BRACKET + body.getId() + V_BAR + body.getName() + C_BRACKET);

        return resp;
    }

    /**
     * The <code>PUT /v1/customers/contacts</code> endpoint.
     * <br />
     * <br />Creates a new contact for a given customer (puts a contact
     * regarding a given customer to the database).
     *
     * @param payload The <code>Map<String,String></code> object,
     *                containing the request body exactly in the form
     *                as <code>{"customer_id":"{customer_id}","contact":"{customer_contact}"}</code>.
     *                It should be passed with the accompanied request header
     *                <code>content-type</code> just like the following:
     *                <br /><code>-H 'content-type: application/json' -d '{"customer_id":"{customer_id}","contact":"{customer_contact}"}'</code>
     *                <br /><code>{customer_id}</code> is the customer ID used
     *                to associate a newly created contact with this customer.
     *                <br /><code>{customer_contact}</code> is a newly created
     *                contact (phone or email).
     *
     * @return The <code>ResponseEntity<CustomersApiLiteEntityContact></code>
     *         object with the <code>201 Created</code> HTTP status code,
     *         the <code>Location</code> response header (among others),
     *         and the response body in JSON representation, containing details
     *         of a newly created customer contact (phone or email).
     *         May return client or server error depending on incoming request.
     */
    @PutMapping(SLASH + REST_CONTACTS)
    public ResponseEntity<CustomersApiLiteEntityContact> add_contact(
        @RequestBody Map<String,String> payload) throws URISyntaxException {

        var customer_id      = payload.get(DB_T_CONT_C_CUST_ID);
        var customer_contact = payload.get(DB_T_CONT_C_CONTACT);

        _dbg(CUST_ID + EQUALS + customer_id);
        _dbg(O_BRACKET + customer_contact + C_BRACKET);

        var cust_id = 0L;

        try {
            cust_id = Long.parseLong(customer_id);
        } catch (NumberFormatException e) {
            _dbg(O_BRACKET + O_BRACKET + customer_id
               + C_BRACKET + C_BRACKET);
        }

        var sql_query = EMPTY_STRING;

        // Parsing and validating a customer contact: phone or email.
        var contact_type = _parse_contact(customer_contact);

               if (contact_type.compareToIgnoreCase(PHONE) == 0) {
            i_cont[0].execute(payload);

            sql_query = SQL_GET_CONTACTS_BY_TYPE[0]
                      + SQL_ORDER_CONTACTS_BY_ID[0];
        } else if (contact_type.compareToIgnoreCase(EMAIL) == 0) {
            i_cont[1].execute(payload);

            sql_query = SQL_GET_CONTACTS_BY_TYPE[1]
                      + SQL_ORDER_CONTACTS_BY_ID[1];
        } else {
            throw new NullPointerException(); // FIXME: Replace this!
        }

        var contact = c.sql(sql_query + SQL_DESC_LIMIT_1)
                       .param(cust_id)
                       .query(CustomersApiLiteEntityContact.class)
                       .single();

        var hdrs = new HttpHeaders();
            hdrs.setLocation(new URI(SLASH + REST_VERSION
                                   + SLASH + REST_PREFIX
                                   + SLASH + customer_id
                                   + SLASH + REST_CONTACTS
                                   + SLASH + contact_type));

        var resp = new ResponseEntity<CustomersApiLiteEntityContact>(
            contact, hdrs, HttpStatus.CREATED); // <== HTTP 201 Created

        var body = resp.getBody();

        _dbg(O_BRACKET + contact_type + V_BAR + body.getContact() + C_BRACKET);

        return resp;
    }

    /**
     * The <code>GET /v1/customers</code> endpoint.
     * <br />
     * <br />Retrieves from the database and lists all customer profiles.
     *
     * @return The <code>ResponseEntity<List></code> object
     *         with the <code>200 OK</code> HTTP status code and the response
     *         body in JSON representation, containing a list of all customer
     *         profiles.
     *         May return client or server error depending on incoming request.
     */
    @GetMapping
    public ResponseEntity<List> list_customers() {
        var customers = c.sql(SQL_GET_ALL_CUSTOMERS)
                         .query(CustomersApiLiteEntityCustomer.class)
                         .list();

        if (customers.isEmpty()) {
            customers.add(new CustomersApiLiteEntityCustomer());
        }

        var resp = new ResponseEntity<List>(customers, HttpStatus.OK);

        var body = resp.getBody().get(0);

        _dbg(O_BRACKET + ((CustomersApiLiteEntityCustomer) body).getId()
           + V_BAR     + ((CustomersApiLiteEntityCustomer) body).getName()
           + C_BRACKET);

        return resp;
    }

    /**
     * The <code>GET /v1/customers/{customer_id}</code> endpoint.
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
     */
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

        var body = resp.getBody();

        _dbg(O_BRACKET + body.getId() + V_BAR + body.getName() + C_BRACKET);

        return resp;
    }

    /**
     * The <code>GET /v1/customers/{customer_id}/contacts</code> endpoint.
     * <br />
     * <br />Retrieves from the database and lists all contacts
     * associated with a given customer.
     *
     * @param customer_id The customer ID used to retrieve contacts
     *                    which belong to this customer.
     *
     * @return The <code>ResponseEntity<List></code> object
     *         with the <code>200 OK</code> HTTP status code and the response
     *         body in JSON representation, containing a list of all contacts
     *         associated with a given customer.
     *         May return client or server error depending on incoming request.
     */
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

        var body = resp.getBody().get(0);

        _dbg(O_BRACKET + ((CustomersApiLiteEntityContact) body).getContact()
           + C_BRACKET);

        return resp;
    }

    /**
     * The <code>GET /v1/customers/{customer_id}/contacts/{contact_type}</code>
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
     * @return The <code>ResponseEntity<List></code> object
     *         with the <code>200 OK</code> HTTP status code and the response
     *         body in JSON representation, containing a list of all contacts
     *         of a given type associated with a given customer.
     *         May return client or server error depending on incoming request.
     */
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

        var body = resp.getBody().get(0);

        _dbg(O_BRACKET + ((CustomersApiLiteEntityContact) body).getContact()
           + C_BRACKET);

        return resp;
    }

    // Helper method. Used to parse and validate a customer contact.
    //                Returns the type of contact: phone or email.
    private String _parse_contact(final String contact) {
             if (contact.matches(PHONE_REGEX)) return PHONE;
        else if (contact.matches(EMAIL_REGEX)) return EMAIL;
        else return EMPTY_STRING;
    }
}

// vim:set nu et ts=4 sw=4:
