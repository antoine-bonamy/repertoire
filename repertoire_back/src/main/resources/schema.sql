create table user
(
    id        bigint auto_increment
        primary key,
    firstname varchar(255) null,
    lastname  varchar(255) null,
    email     varchar(255) not null,
    password  varchar(255) null,
    constraint email
        unique (email),
    constraint email_unique
        unique (email)
);

create table contact_group
(
    id      bigint auto_increment
        primary key,
    name    varchar(255) null,
    note    varchar(255) null,
    user_id bigint       null,
    constraint contact_group_ibfk_1
        foreign key (user_id) references user (id)
            on delete set null
);

create index user_id
    on contact_group (user_id);

create table organization
(
    id      bigint auto_increment
        primary key,
    name    varchar(255) null,
    note    varchar(255) null,
    user_id bigint       null,
    constraint fk_user
        foreign key (user_id) references user (id)
            on delete set null
);

create table contact
(
    id              bigint auto_increment
        primary key,
    firstname       varchar(255) null,
    lastname        varchar(255) null,
    email           varchar(255) null,
    phone           varchar(50)  null,
    address         varchar(255) null,
    note            varchar(255) null,
    organization_id bigint       null,
    user_id         bigint       null,
    constraint contact_ibfk_1
        foreign key (organization_id) references organization (id)
            on delete set null,
    constraint contact_ibfk_2
        foreign key (user_id) references user (id)
            on delete set null
);

create index organization_id
    on contact (organization_id);

create index user_id
    on contact (user_id);

create table link_contact_group_contact
(
    group_id   bigint not null,
    contact_id bigint not null,
    primary key (group_id, contact_id),
    constraint link_contact_group_contact_ibfk_1
        foreign key (group_id) references contact_group (id)
            on delete cascade,
    constraint link_contact_group_contact_ibfk_2
        foreign key (contact_id) references contact (id)
            on delete cascade
);

create index contact_id
    on link_contact_group_contact (contact_id);

