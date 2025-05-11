
create table assignment (
    department_id bigint not null,
    user_id bigint not null,

    primary key(department_id, user_id),
    foreign key (department_id) references department(id),
    foreign key (user_id) references users(id)
);