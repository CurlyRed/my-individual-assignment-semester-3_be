ALTER TABLE `transactions`
    ADD COLUMN `product_id` BIGINT,
ADD CONSTRAINT fk_product
FOREIGN KEY (`product_id`) REFERENCES `products`(`id`);