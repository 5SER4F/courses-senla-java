SELECT u.id AS user, p.product_id AS product
  FROM user AS u
  LEFT JOIN purchase AS p ON u.id = p.user_id