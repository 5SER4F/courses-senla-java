CREATE TABLE IF NOT EXISTS creator (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  password varchar NOT NULL,
  name varchar NOT NULL,
  registration_date timestamp
);