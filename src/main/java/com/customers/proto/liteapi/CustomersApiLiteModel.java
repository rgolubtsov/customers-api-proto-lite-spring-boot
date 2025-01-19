/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteModel.java
 * ============================================================================
 * Customers API Lite microservice prototype. Version 0.3.0
 * ============================================================================
 * A Spring Boot-based application, designed and intended to be run
 * as a microservice, implementing a special Customers API prototype
 * with a smart yet simplified data scheme.
 * ============================================================================
 * (See the LICENSE file at the top of the source tree.)
 */

package com.customers.proto.liteapi;

/**
 * The model class of the microservice.
 *
 * @version 0.3.0
 * @since   0.1.1
 */
public class CustomersApiLiteModel {
    /**
     * The SQL query for retrieving all customer profiles.
     *
     * Used by the <code>GET /customers</code> REST endpoint.
     */
    public static final String SQL_GET_ALL_CUSTOMERS
        = "select id ," // as 'Customer ID',"
        + "       name" // as 'Customer Name'"
        + " from"
        + "       customers"
        + " order by"
        + "       id";

    /**
     * The SQL query for retrieving profile details for a given customer.
     *
     * Used by the <code>GET /customers/{customer_id}</code> REST endpoint.
     */
    public static final String SQL_GET_CUSTOMER_BY_ID
        = "select id ," // as 'Customer ID',"
        + "       name" // as 'Customer Name'"
        + " from"
        + "       customers"
        + " where"
        + "      (id = ?)";

    /**
     * The SQL query for retrieving all contacts for a given customer.
     *
     * Used by the <code>GET /customers/{customer_id}/contacts</code>
     * REST endpoint.
     */
    public static final String SQL_GET_ALL_CONTACTS
        = "select phones.contact" // as 'Phone(s)'"
        + " from"
        + "       contact_phones phones,"
        + "       customers      cust"
        + " where"
        + "      (cust.id = phones.customer_id) and"
        + "      (cust.id =                  ?)"
        + " union "
        + "select emails.contact" // as 'Email(s)'"
        + " from"
        + "       contact_emails emails,"
        + "       customers      cust"
        + " where"
        + "      (cust.id = emails.customer_id) and"
        + "      (cust.id =                  ?)";

    /**
     * The SQL queries for retrieving all contacts of a given type
     * for a given customer.
     *
     * Used
     * by the <code>GET /customers/{customer_id}/contacts/{contact_type}</code>
     * REST endpoint.
     */
    public static final String[] SQL_GET_CONTACTS_BY_TYPE
        ={"select phones.contact" // as 'Phone(s)'"
        + " from"
        + "       contact_phones phones,"
        + "       customers      cust"
        + " where"
        + "      (cust.id = phones.customer_id) and"
        + "      (cust.id =                  ?)",
          "select emails.contact" // as 'Email(s)'"
        + " from"
        + "       contact_emails emails,"
        + "       customers      cust"
        + " where"
        + "      (cust.id = emails.customer_id) and"
        + "      (cust.id =                  ?)",
          "select name from customers where (id = ?)"};

    // The intermediate part of an SQL query,
    // used to order contact records by ID.
    public static final String[] SQL_ORDER_CONTACTS_BY_ID
        ={" order by phones.id",
          " order by emails.id"};

    // The terminating part of an SQL query,
    // used to retrieve the last record created.
    public static final String SQL_DESC_LIMIT_1 = " desc limit 1";
}

// vim:set nu et ts=4 sw=4:
