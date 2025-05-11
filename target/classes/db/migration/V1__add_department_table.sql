
create table department (
    id serial primary key,
    name varchar(50) unique not null,
    category varchar(30)
);