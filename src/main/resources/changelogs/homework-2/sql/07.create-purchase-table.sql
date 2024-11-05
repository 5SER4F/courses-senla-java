CREATE TABLE IF NOT EXISTS purchase (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid (),
  customer_id uuid NOT NULL,
  product_id uuid NOT NULL,
  final_cost decimal(10,2) NOT NULL,
  purchase_date timestamp DEFAULT LOCALTIMESTAMP,
  purchase_status varchar DEFAULT 'CREATED'
);
