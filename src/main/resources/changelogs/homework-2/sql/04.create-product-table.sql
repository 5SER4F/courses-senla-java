CREATE TABLE IF NOT EXISTS product (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  creator_id uuid NOT NULL,
  price  decimal(10, 2) NOT NULL,
  discount float DEFAULT 0,
  name varchar NOT NULL,
  date_added date,
  age_rating varchar NOT NULL,
  product_status varchar DEFAULT 'CREATED'
);