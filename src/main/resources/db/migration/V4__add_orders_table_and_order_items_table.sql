create table orders
(
    id           bigint auto_increment primary key,
    customer_id  bigint not null,
    status       varchar(20) not null,
    created_at   timestamp default current_timestamp not null,
    total_price  decimal(10, 2) not null,
    constraint fk_orders_customer
        foreign key (customer_id) references users (id)
            on update cascade
            on delete cascade
);

create table orders_items
(
    id          bigint auto_increment primary key,
    order_id    bigint not null,
    product_id  bigint not null,
    unit_price  decimal(10, 2) not null,
    quantity    int not null,
    total_price decimal(10, 2) not null,
    constraint fk_order_items_order
        foreign key (order_id) references orders (id)
            on update cascade
            on delete cascade,
    constraint fk_order_items_product
        foreign key (product_id) references products (id)
            on update cascade
            on delete cascade
);
