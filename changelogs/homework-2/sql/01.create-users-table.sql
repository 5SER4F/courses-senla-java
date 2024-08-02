CREATE TABLE users (
  id integer PRIMARY KEY,
  password varchar NOT NULL,
  firstname varchar,
  surname varchar,
  nickname varchar NOT NULL,
  birth_date timestamp NOT NULL,
  registration_date timestamp,
  country timestamp NOT NULL
);