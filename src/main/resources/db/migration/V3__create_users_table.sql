
create table users (
    id serial primary key,
    username varchar(20),
    password varchar(30),

    roles varchar(20)
);