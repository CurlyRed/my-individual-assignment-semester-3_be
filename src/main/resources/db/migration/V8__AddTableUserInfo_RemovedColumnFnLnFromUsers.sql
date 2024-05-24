CREATE TABLE `user_information` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `first_name` VARCHAR(255),
    `last_name` VARCHAR(255),
    `city_id` BIGINT,
    `age` INT,
    `gender` VARCHAR(255)
);
ALTER TABLE `user_information` ADD FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)


