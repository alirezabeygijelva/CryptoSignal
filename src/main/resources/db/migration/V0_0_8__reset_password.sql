-- ---------------------------------------------------------
-- Create `reset_password_tokens` table
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS `reset_password_tokens`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     BIGINT                 NOT NULL,
    `reset_token` VARCHAR(255)           NOT NULL UNIQUE,
    `expired_at`  DATETIME               NOT NULL,
    `used`        BOOLEAN  DEFAULT FALSE NOT NULL,
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;