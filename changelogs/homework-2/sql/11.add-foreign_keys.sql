ALTER TABLE product ADD FOREIGN KEY (creator_id) REFERENCES creator (id) ON DELETE RESTRICT;

ALTER TABLE purchase ADD FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT;

ALTER TABLE purchase ADD FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT;

ALTER TABLE product_genre ADD FOREIGN KEY ( product_id) REFERENCES product (id) ON DELETE RESTRICT;

ALTER TABLE product_genre ADD FOREIGN KEY (genre_id) REFERENCES genre (id);

ALTER TABLE product ADD FOREIGN KEY (age_rating_id) REFERENCES age_rating (id);

ALTER TABLE age_rating ADD FOREIGN KEY (add_by) REFERENCES staff (id);

ALTER TABLE genre ADD FOREIGN KEY (add_by) REFERENCES staff (id);
