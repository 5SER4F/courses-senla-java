--Для частой выборки жанров конкретного продукта
CREATE INDEX idx_products_genres_ product_id ON products_genres ( product_id);

--Для частого подсчета прибыли от продукта
CREATE INDEX idx_purchases_product_id ON purchases (product_id);

--Для частой выборки библиотеки пользователя
CREATE INDEX idx_library_user_id ON library (user_id);