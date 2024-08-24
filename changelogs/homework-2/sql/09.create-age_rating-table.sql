CREATE TABLE IF NOT EXISTS age_rating (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  name varchar NOT NULL,
  add_by uuid NOT NULL
);
