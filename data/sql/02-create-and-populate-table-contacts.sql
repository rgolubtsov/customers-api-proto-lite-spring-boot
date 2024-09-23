--
-- data/sql/02-create-and-populate-table-contacts.sql
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

create table contacts (id              integer                 not null
                                                               primary key
                                                               autoincrement,
                       contact         character varying(254),
                       contact_type_id integer                 not null
                                                 references contact_types(id)
                                                 on delete restrict);

.tables
.print

pragma table_info(contacts);
.print

select * from contacts;

insert into contacts (contact, contact_type_id)
              values ('+35790X123456',       1);
insert into contacts (contact, contact_type_id)
              values ('contact@example.com', 2);

select * from contacts;

-- vim:set nu et ts=4 sw=4:
