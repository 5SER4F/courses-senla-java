CREATE TABLE purchase (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  user_id uuid NOT NULL,
  product_id uuid NOT NULL,
  final_cost decimal(10,2) NOT NULL,
  purchase_date timestamp
);
