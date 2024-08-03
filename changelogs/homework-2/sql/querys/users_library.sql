SELECT u.id AS user, l.product_id AS product
  FROM users AS u
  LEFT JOIN library AS l ON u.id = l.user_id