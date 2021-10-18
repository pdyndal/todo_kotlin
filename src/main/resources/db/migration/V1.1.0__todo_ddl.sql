
drop table if exists todo;
create table todo
(
    t_id binary(16) auto_increment,
    u_id binary(16) not null,
    title varchar not null,
    content varchar,
    constraint todo_t_id_pk primary key (t_id),
    constraint todo_u_id_fk foreign key (u_id) references user (u_id) on delete cascade
);