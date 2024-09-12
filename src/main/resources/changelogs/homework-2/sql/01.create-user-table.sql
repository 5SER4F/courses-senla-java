CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE IF NOT EXISTS "user" (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  password varchar NOT NULL,
  balance float DEFAULT 0,
  firstname varchar,
  surname varchar,
  nickname varchar NOT NULL,
  birth_date date NOT NULL,
  registration_date timestamp,
  country varchar NOT NULL
);