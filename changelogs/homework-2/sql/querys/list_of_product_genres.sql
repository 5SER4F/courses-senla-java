SELECT p.id, g.name
  FROM products AS p
  LEFT JOIN products_genres AS pg ON p.id = pg. product_id
  LEFT JOIN genres AS g ON pg.genre_id = g.id