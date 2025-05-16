
create table department (
    id serial primary key,
    name varchar(50) unique not null,
    category varchar(30)
);

create table users (
    id serial primary key,
    username varchar(20) unique,
    password varchar(255),

    roles varchar(20)
);

create table assignment (
    department_id bigint not null,
    user_id bigint not null,

    primary key(department_id, user_id),
    foreign key (department_id) references department(id),
    foreign key (user_id) references users(id)
);

create table location (
    id serial primary key,
    country varchar(80),
    city varchar(50),
    address varchar(50) unique,
    description varchar(255)
);

create table project (
    id serial primary key,
    title varchar(255) not null unique,
    description text,
    visible boolean not null,
    status varchar(50),
    start_date date,
    end_date date,
    location_id bigint,

    foreign key (location_id) references location(id)
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