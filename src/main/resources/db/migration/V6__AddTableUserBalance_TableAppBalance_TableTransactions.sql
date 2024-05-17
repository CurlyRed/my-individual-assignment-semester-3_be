CREATE TABLE `user_balances` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `balance` DECIMAL(10, 2),
    `last_update` DATE
);

CREATE TABLE `app_balance` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `balance` DECIMAL(10, 2),
    `last_update` DATE
);

CREATE TABLE `transactions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL REFERENCES `users`(`id`),
    `type` VARCHAR(255) NOT NULL,
    `amount` DECIMAL(10, 2) NOT NULL,
    `description` TEXT,
    `created_at` DATE
);

ALTER TABLE `user_balances` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `transactions` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);