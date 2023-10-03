CREATE TABLE `Movies` (
	`title` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
	`submitter_id` INT,
	`watched` BOOLEAN DEFAULT 0
);

CREATE TABLE `Messages` (
	`id` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
	`server_id` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
	`hard_reactions` TINYINT unsigned DEFAULT '0',
	`not_hard_reactions` TINYINT unsigned,
	`pinned` BOOLEAN DEFAULT 0
);

CREATE TABLE `Users` (
	`id` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
	`votes` INT unsigned DEFAULT '0',
	`server_id` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL
);

CREATE TABLE `Servers` (
	`id` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
	`pin_count` TINYINT unsigned DEFAULT '6',
	`pin_channel_id` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
	`webhook_id` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
	`webhook_token` VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL
);