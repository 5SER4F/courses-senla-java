CREATE TABLE staff (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  password varchar NOT NULL,
  firstname varchar NOT NULL,
  surname varchar NOT NULL,
  birth_date timestamp NOT NULL,
  registration_date timestamp NOT NULL
);