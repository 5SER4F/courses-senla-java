--Для частой выборки жанров конкретного продукта
CREATE INDEX IF NOT EXISTS idx_product_genre_product_id ON product_genre (product_id);

--Для частого подсчета прибыли от продукта
CREATE INDEX IF NOT EXISTS  idx_purchase_product_id ON purchase (product_id);

--Для частой выборки библиотеки пользователя
CREATE INDEX IF NOT EXISTS  idx_library_user_id ON purchase (user_id);