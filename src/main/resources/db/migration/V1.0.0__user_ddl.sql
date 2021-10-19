drop table if exists user;
create table user
(
    u_id bigint auto_increment,
    name varchar not null,
    surname varchar not null,
    constraint user_u_id_pk primary key (u_id)
);