
drop table if exists todo;
create table todo
(
    t_id bigint auto_increment,
    u_id bigint not null,
    title varchar not null,
    content varchar,
    constraint todo_t_id_pk primary key (t_id),
    constraint todo_u_id_fk foreign key (u_id) references user (u_id) on delete cascade
);