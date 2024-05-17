CREATE TABLE `contact_information`(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `contact_person` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `product_id`BIGINT NOT NULL
);

ALTER TABLE `contact_information` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);