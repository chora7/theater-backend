

create table performance (
    user_id bigint not null,
    project_id bigint not null,

    role varchar(50),
    specialization varchar(50),
    status varchar(20),

    primary key (user_id, project_id),
    foreign key (user_id) references users(id),
    foreign key (project_id) references project(id)
);
