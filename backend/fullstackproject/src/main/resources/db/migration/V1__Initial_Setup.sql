CREATE TABLE customer(
    id BIGSERIAL PRIMARY KEY,
    name Text  NOT NULL,
    email Text NOT NULL UNIQUE,
    age INT NOT NULL
)
