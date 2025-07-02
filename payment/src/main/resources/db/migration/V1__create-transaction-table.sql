CREATE TABLE transaction (
    transaction_id UUID not null PRIMARY KEY,
    ride_id UUID not null,
    amount DOUBLE PRECISION not null,
    date TIMESTAMP not null,
    status VARCHAR(20) not null
);
