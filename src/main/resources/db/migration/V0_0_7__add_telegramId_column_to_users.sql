-- ---------------------------------------------------------
-- Add `telegram_id` column to users
-- ---------------------------------------------------------
ALTER TABLE `users`
    ADD COLUMN `telegram_id` VARCHAR(255) NULL DEFAULT NULL AFTER `phone`;