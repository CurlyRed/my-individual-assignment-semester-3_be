CREATE TABLE `chats` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `buyer_id` BIGINT,
                         `seller_id` BIGINT,
                         `product_id` BIGINT,
                         `deleted` BOOLEAN,
                         `created_at` DATE
);

CREATE TABLE `messages` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `chat_id` BIGINT,
                            `sender_id` BIGINT,
                            `content` TEXT NOT NULL,
                            `timestamp` DATE
);

ALTER TABLE `chats`
    ADD CONSTRAINT `fk_chats_buyer` FOREIGN KEY (`buyer_id`) REFERENCES `users`(`id`),
    ADD CONSTRAINT `fk_chats_seller` FOREIGN KEY (`seller_id`) REFERENCES `users`(`id`),
    ADD CONSTRAINT `fk_chats_product` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`);

ALTER TABLE `messages`
    ADD CONSTRAINT `fk_messages_chat` FOREIGN KEY (`chat_id`) REFERENCES `chats`(`id`),
    ADD CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `users`(`id`);

CREATE INDEX `idx_messages_chat_id` ON `messages`(`chat_id`);
CREATE INDEX `idx_chats_buyer_id` ON `chats`(`buyer_id`);
CREATE INDEX `idx_chats_seller_id` ON `chats`(`seller_id`);
