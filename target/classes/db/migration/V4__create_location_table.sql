
create table location (
    id serial primary key,
    country varchar(80),
    city varchar(50),
    address varchar(50),
    description varchar(255)
);