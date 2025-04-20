-- ----------------------------------------
-- Create `binance_streams` table
-- ----------------------------------------
CREATE TABLE `binance_streams`
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    stream_name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NULL DEFAULT NULL
) ENGINE = InnoDB;