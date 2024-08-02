SELECT p.id, SUM(c.cost)
  FROM products AS p
  LEFT JOIN purchases AS c ON p.id = c.product_id
  GROUP BY p.id