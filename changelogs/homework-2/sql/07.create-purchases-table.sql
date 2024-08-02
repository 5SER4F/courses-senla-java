CREATE TABLE purchases (
  id integer PRIMARY KEY,
  user_id integer NOT NULL,
  product_id integer NOT NULL,
  cost float NOT NULL,
  purchase_date timestamp
);
