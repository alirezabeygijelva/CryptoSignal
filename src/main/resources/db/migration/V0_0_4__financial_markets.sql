-- ----------------------------------------------------
-- Create `markets` table
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS `markets`
(
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL UNIQUE,
    `market_type` VARCHAR(255) NOT NULL,
    `description` TEXT         NULL DEFAULT NULL,
    `created_at`  DATETIME          DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;