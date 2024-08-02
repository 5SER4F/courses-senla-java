ALTER TABLE library ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT;

ALTER TABLE library ADD FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE RESTRICT;

ALTER TABLE products ADD FOREIGN KEY (creator_id) REFERENCES creators (id) ON DELETE RESTRICT;

ALTER TABLE purchases ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT;

ALTER TABLE purchases ADD FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE RESTRICT;

ALTER TABLE products_genres ADD FOREIGN KEY ( product_id) REFERENCES products (id) ON DELETE RESTRICT;

ALTER TABLE products_genres ADD FOREIGN KEY (genre_id) REFERENCES genres (id);

ALTER TABLE products ADD FOREIGN KEY (age_rating_id) REFERENCES age_ratings (id);

ALTER TABLE age_ratings ADD FOREIGN KEY (add_by) REFERENCES staff (id);

ALTER TABLE genres ADD FOREIGN KEY (add_by) REFERENCES staff (id);
