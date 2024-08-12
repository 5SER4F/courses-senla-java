SELECT p.id, SUM(c.cost)
  FROM product AS p
  LEFT JOIN purchase AS c ON p.id = c.product_id
  GROUP BY p.id