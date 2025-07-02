CREATE TABLE account (
    account_id UUID not null PRIMARY KEY,
    name VARCHAR(254) not null,
    email VARCHAR(254) not null UNIQUE,
    cpf VARCHAR(11) not null,
    car_plate VARCHAR(254),
    is_passenger boolean not null,
    is_driver boolean not null,
    password_algorithm VARCHAR(254) not null
);

