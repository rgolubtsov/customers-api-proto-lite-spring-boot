--
-- data/sql/03-create-and-populate-table-contact_emails.sql
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

create table contact_emails (id          integer       not null primary key
                                                                autoincrement,
                             contact     varchar(254),
                             customer_id integer       references customers(id)
                                                       on delete restrict);

.tables
.print

pragma table_info(contact_emails);
.print

select * from contact_emails;

-- ----------------------------------------------------------------------------
insert into contact_emails (contact, customer_id)
                    values ('jammy.jellyfish@example.com', 1);
insert into contact_emails (contact, customer_id)
                    values (     'jjellyfish@example.com', 1);
insert into contact_emails (contact, customer_id)
                    values (             'jj@example.org', 1);
-- ----------------------------------------------------------------------------
insert into contact_emails (contact, customer_id)
                    values ('noble.numbat@example.com', 2);
insert into contact_emails (contact, customer_id)
                    values (     'nnumbat@example.com', 2);
insert into contact_emails (contact, customer_id)
                    values (          'nn@example.org', 2);
-- ----------------------------------------------------------------------------

select * from contact_emails;

-- vim:set nu et ts=4 sw=4:
