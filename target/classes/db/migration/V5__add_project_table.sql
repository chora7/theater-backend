
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