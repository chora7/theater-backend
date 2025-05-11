

/*

create table assignment (
    department_id bigint not null,
    user_id bigint not null,

    primary key(department_id, user_id),
    foreign key (department_id) references department(id),
    foreign key (user_id) references users(id)
);

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

create table project (
    id serial primary key,
    title varchar(255) not null,
    description text,
    visible boolean not null,
    status varchar(50),
    start_date date,
    end_date date,
    location_id bigint,

    foreign key (location_id) references location(id)
);


*/