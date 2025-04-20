-- ----------------------------------------------------
-- Create `roles` table
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `roles`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL UNIQUE,
    `authorities` TEXT         NOT NULL
);

-- ----------------------------------------------------
-- Create `user_roles` table
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_roles`
(
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
);