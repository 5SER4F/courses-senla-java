CREATE TABLE IF NOT EXISTS creator (
  id uuid PRIMARY KEY,
  name varchar NOT NULL,
  balance float DEFAULT 0,
  last_settlement_date timestamp DEFAULT LOCALTIMESTAMP
);