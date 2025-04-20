-- ----------------------------------------------------
-- Create `subscriptions` table for financial market subscription
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `subscriptions`
(
    `id`                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`              BIGINT          NOT NULL,
    `market_id`            INT             NOT NULL,
    `symbol`               VARCHAR(255)    NOT NULL,
    `target_type`          VARCHAR(50)     NOT NULL,
    `target_value`         DECIMAL(30, 10) NOT NULL,
    `message`              VARCHAR(255)    NOT NULL,
    `notification_type`    INT             NOT NULL,
    `notification_enabled` BOOLEAN  DEFAULT TRUE,
    `timeout`              BIGINT          NOT NULL,
    `last_triggered`       DATETIME DEFAULT NULL,
    `created_at`           DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`market_id`) REFERENCES `markets` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;
