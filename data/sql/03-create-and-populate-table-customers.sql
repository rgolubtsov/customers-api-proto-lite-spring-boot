--
-- data/sql/03-create-and-populate-table-customers.sql
-- ============================================================================
-- Customers API Lite microservice prototype. Version 0.1.1
-- ============================================================================
-- A Spring Boot-based application, designed and intended to be run
-- as a microservice, implementing a special Customers API prototype
-- with a smart yet simplified data scheme.
-- ============================================================================
-- (See the LICENSE file at the top of the source tree.)
--

.headers on
.mode    column

.tables
.print

create table customers (id        integer               not null
                                                        primary key
                                                        autoincrement,
                       name       character varying(64) not null,
                       contact_id integer               references contacts(id)
                                                        on delete restrict);

.tables
.print

pragma table_info(customers);
.print

select * from customers;

insert into customers (name, contact_id) values ('Jammy Jellyfish', 1);
insert into customers (name, contact_id) values ('Jammy Jellyfish', 2);

select * from customers;

-- vim:set nu et ts=4 sw=4:
