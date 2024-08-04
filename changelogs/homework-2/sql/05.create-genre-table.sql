CREATE TABLE genre (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  name varchar NOT NULL,
  add_by uuid NOT NULL
);
