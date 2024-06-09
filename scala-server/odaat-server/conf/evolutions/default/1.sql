# Project schema

# --- !Ups
CREATE TABLE IF NOT EXISTS `odaat`.`project` (
    `title` VARCHAR(50) PRIMARY KEY,
    `duration` DOUBLE,
    `completed_at` DATE,
    `due` DATE,
    `priority` VARCHAR(10) DEFAULT 'low',
    `description` VARCHAR(500),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `odaat`.`task` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `project_title` VARCHAR(50) NOT NULL,
    `date` DATE,
    `duration` DOUBLE,
    `status` VARCHAR(20) DEFAULT 'inprog',
    `task` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`project_title`) REFERENCES project(`title`) ON DELETE CASCADE
)
DEFAULT CHARACTER SET = utf8;

INSERT INTO `project` (
    title, duration, due, priority, description
) VALUES (
    'Sample Project', 10.0, '2025-01-01', 'medium', 'A Sample Project'
);

INSERT INTO `task` (
    project_title, date, duration, task
) VALUES (
    'Sample Project', CURRENT_TIMESTAMP, 5.0, 'A Sample Task'
);