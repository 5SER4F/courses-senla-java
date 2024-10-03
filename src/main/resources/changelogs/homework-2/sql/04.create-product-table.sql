CREATE TABLE IF NOT EXISTS product (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  creator_id uuid NOT NULL,
  price  decimal(10, 2) NOT NULL,
  discount float DEFAULT 0,--or decimal (2,2)?
  name varchar NOT NULL,
  date_added date,
  age_rating_id uuid NOT NULL
);