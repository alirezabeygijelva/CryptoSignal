-- ----------------------------------------------------
-- Create `users` table
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users`
(
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `client_id`  BIGINT UNSIGNED NOT NULL UNIQUE,
    `first_name` VARCHAR(255)    NOT NULL,
    `last_name`  VARCHAR(255)    NOT NULL,
    `email`      VARCHAR(255)    NOT NULL UNIQUE,
    `phone`      VARCHAR(20)     NOT NULL UNIQUE,
    `password`   VARCHAR(255)    NOT NULL,
    `enabled`    BOOLEAN  DEFAULT FALSE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DELIMITER $$
CREATE TRIGGER `before_insert_users`
    BEFORE INSERT
    ON `users`
    FOR EACH ROW
BEGIN
    SET `NEW`.`client_id` = (SELECT FLOOR(RAND() * 900000000000) + 100000000000);
END$$
DELIMITER ;
