/*
 * src/main/java/com/customers/proto/liteapi/CustomersApiLiteApp.java
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

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import org.graylog2.syslog4j.impl.unix.UnixSyslogConfig;
import org.graylog2.syslog4j.impl.unix.UnixSyslog;
import org.graylog2.syslog4j.SyslogIF;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import static com.customers.proto.liteapi.CustomersApiLiteHelper.*;

/**
 * The startup class of the microservice.
 *
 * @version 0.1.9
 * @since   0.0.1
 */
@SpringBootApplication
public class CustomersApiLiteApp implements DisposableBean {
    /**
     * The microservice entry point.
     *
     * @param args An array of command-line arguments.
     */
    public static void main(final String[] args) {
        // Starting up the bundled web server.
        var ctx = SpringApplication.run(CustomersApiLiteApp.class, args);

        var env = ctx.getEnvironment();

        dbg = Boolean.parseBoolean(env.getProperty(DBG_LOG_ENBLR));

        // Opening the system logger.
        // Calling <syslog.h> openlog(NULL, LOG_CONS | LOG_PID, LOG_DAEMON);
        var cfg = new UnixSyslogConfig();
        cfg.setIdent(null); cfg.setFacility(SyslogIF.FACILITY_DAEMON);
        s = new UnixSyslog(); s.initialize (SyslogIF.UNIX_SYSLOG,cfg);

        _dbg(O_BRACKET + env.getProperty(APP_NAME) + C_BRACKET);

        var ds = (HikariDataSource) ctx.getBean(DATA_SOURCE);

        _dbg(O_BRACKET + ds.getDriverClassName() + C_BRACKET);
        _dbg(O_BRACKET + ds.getJdbcUrl()         + C_BRACKET);

        c = JdbcClient.create(ds);

        i_cust   = new SimpleJdbcInsert(ds).withTableName(DB_T_CUSTOMERS);
        i_cont   = new SimpleJdbcInsert[2];
        i_cont[0]= new SimpleJdbcInsert(ds).withTableName(DB_T_CONTACT_PHONES);
        i_cont[1]= new SimpleJdbcInsert(ds).withTableName(DB_T_CONTACT_EMAILS);

        // Getting the port number used to run the bundled web server.
        var server_port = env.getProperty(SERVER_PORT);

        l.info(MSG_SERVER_STARTED + server_port);
        s.info(MSG_SERVER_STARTED + server_port);
    }

    /** Gets called when the server is about to be stopped. */
    @Override
    public void destroy() throws Exception {
        l.info(MSG_SERVER_STOPPED);
        s.info(MSG_SERVER_STOPPED);

        // Closing the system logger.
        // Calling <syslog.h> closelog();
        s.shutdown();
    }
}

// vim:set nu et ts=4 sw=4:
