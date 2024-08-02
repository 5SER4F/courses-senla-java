CREATE TABLE staff (
  id integer PRIMARY KEY,
  password varchar NOT NULL,
  firstname varchar NOT NULL,
  surname varchar NOT NULL,
  birth_date timestamp NOT NULL,
  registration_date timestamp NOT NULL
);