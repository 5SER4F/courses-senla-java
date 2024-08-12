CREATE TABLE IF NOT EXISTS "user" (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  password varchar NOT NULL,
  firstname varchar,
  surname varchar,
  nickname varchar NOT NULL,
  birth_date timestamp NOT NULL,
  registration_date timestamp,
  country varchar NOT NULL
);