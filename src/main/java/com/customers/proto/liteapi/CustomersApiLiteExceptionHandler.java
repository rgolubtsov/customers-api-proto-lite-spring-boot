/*
 * src/main/java/com/customers/proto/liteapi/
 * CustomersApiLiteExceptionHandler.java
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

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;

/**
 * The exception handler class for the controller of the microservice.
 * It is mainly dedicated to handle client errors and respond accordingly
 * with one of the <strong>4xx Client Error</strong> section's errors.
 *
 * @version 0.3.0
 * @since   0.2.5
 */
@ControllerAdvice
public class CustomersApiLiteExceptionHandler
    extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception      ex,
        Object         body,
        HttpHeaders    headers,
        HttpStatusCode statusCode,
        WebRequest     request) {

        if (statusCode.value() == HttpStatus.METHOD_NOT_ALLOWED.value()) {
            return new ResponseEntity<Object>(body, headers, statusCode);
        }

        var error = new CustomersApiLiteEntityError();

        error.setError(ERR_REQ_MALFORMED);

        var resp = new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);

        var body_ = resp.getBody();

        _dbg(O_BRACKET + ((CustomersApiLiteEntityError) body_).getError()
           + C_BRACKET);

        return resp;
    }
}

// vim:set nu et ts=4 sw=4:
