CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS "user" (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  role varchar NOT NULL,
  username varchar UNIQUE NOT NULL,
  password varchar NOT NULL,
  registration_date timestamp DEFAULT LOCALTIMESTAMP,
  email varchar UNIQUE NOT NULL,
  account_status varchar DEFAULT 'CREATED'
);