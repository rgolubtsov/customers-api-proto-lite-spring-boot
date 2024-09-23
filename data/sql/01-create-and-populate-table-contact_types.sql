--
-- data/sql/01-create-and-populate-table-contact_types.sql
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

create table contact_types (id           integer              not null
                                                              primary key
                                                              autoincrement,
                            contact_type character varying(5) not null);

.tables
.print

pragma table_info(contact_types);
.print

select * from contact_types;

insert into contact_types (contact_type) values ('phone');
insert into contact_types (contact_type) values ('email');

select * from contact_types;

-- vim:set nu et ts=4 sw=4:
