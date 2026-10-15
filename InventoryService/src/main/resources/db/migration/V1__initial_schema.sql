create TABLE events(
    id UUID PRIMARY KEY,
    event_name varchar(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    capacity INT NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL
);