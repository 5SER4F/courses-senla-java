CREATE TABLE IF NOT EXISTS genre (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  name varchar NOT NULL
);
