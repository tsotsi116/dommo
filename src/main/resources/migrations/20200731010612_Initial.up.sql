CREATE TABLE users (
    id serial not null primary key,
    username varchar(50) not null,
    password varchar(255) not null,
    display_name varchar(140),
    email varchar(255) not null,
    phone varchar(20),
    user_type varchar(20) default 'CUSTOMER',
    is_activated boolean default false,
    last_login_at timestamp without time zone,
    created timestamp without time zone,
    modified timestamp without time zone,
    deleted timestamp without time zone
);

create table mobile_money_services (
    id integer not null primary key,
    service_name varchar(50) not null,
    is_activated boolean default false,
    modified timestamp without time zone
);

insert into mobile_money_services(id, service_name, is_activated, modified) VALUES
(0, 'Test Service', true, now()),
(1, 'TNM Mpamba', true, now()),
(2, 'Airtel Money', true, now()),
(3, 'NBM Mo626', false, now()),
(4, 'NBS 322', false, now()),
(5, 'FDH 522', false, now());

create table mobile_money_payments (
    id varchar(255) primary key not null,
    -- Details of the upstream system
    website_url varchar(255) not null,
    txn_id varchar(100),
    product_id varchar(200),
    customer_id varchar(200),
    -- Subscriber Payment Detail
    depositor_name varchar(255) not null,
    phone_or_account_number varchar(50) not null,
    service_txn_reference varchar(100) not null,
    mobile_money_service_id integer not null,
    amount decimal(18, 3),
    service_txn_details varchar(100),
    ip_address varchar(255),
    user_agent text,
    -- State of the payment
    verified_by integer,
    verified_amount decimal(18, 3),
    is_verified boolean default false,
    is_cancelled boolean default false,
    cancelled_by integer default null,
    -- Events
    failed_callback_url text,
    verified_callback_url text,
    -- Timestamps
    created timestamp without time zone,
    modified timestamp without time zone,
    deleted timestamp without time zone
);

COMMENT ON COLUMN mobile_money_payments.product_id IS 'The identifier for the product/service being purchased by the Subscriber/Customer on the upstream system';
COMMENT ON COLUMN mobile_money_payments.customer_id IS 'The identifier of the Subscriber/Customer on the upstream system';
COMMENT ON COLUMN mobile_money_payments.customer_id IS 'The identifier of the Subscriber/Customer on the upstream system';

