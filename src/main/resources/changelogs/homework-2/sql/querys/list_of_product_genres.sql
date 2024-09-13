SELECT p.id, g.name
  FROM product AS p
  LEFT JOIN product_genre AS pg ON p.id = pg. product_id
  LEFT JOIN genre AS g ON pg.genre_id = g.id