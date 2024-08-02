CREATE TABLE products (
  id integer PRIMARY KEY,
  creator_id integer NOT NULL,
  name varchar NOT NULL,
  date_added timestamp,
  age_rating_id integer NOT NULL
);