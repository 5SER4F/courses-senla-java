CREATE TABLE IF NOT EXISTS customer (
  id uuid PRIMARY KEY,
  balance float DEFAULT 0,
  firstname varchar,
  surname varchar,
  nickname varchar NOT NULL,
  birth_date date NOT NULL,
  country varchar NOT NULL
);