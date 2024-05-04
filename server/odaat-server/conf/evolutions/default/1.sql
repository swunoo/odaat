# Project schema

# --- !Ups
CREATE TABLE IF NOT EXISTS `odaat`.`project` (
    `title` VARCHAR(50) NOT NULL,
    `duration` DOUBLE,
    `completed_at` DATE,
    `deadline` DATE,
    `priority` VARCHAR(10) DEFAULT 'low',
    `description` VARCHAR(500),
    PRIMARY KEY (`title`))
DEFAULT CHARACTER SET = utf8

# --- !Downs
DROP TABLE 'project'