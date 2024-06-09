CREATE TABLE IF NOT EXISTS uzer (
    'id' INT AUTO_INCREMENT PRIMARY KEY,
    'name' VARCHAR(32) DEFAULT "Anon",
    'email' VARCHAR(255) NOT NULL,
    'password' VARCHAR(72) NOT NULL,
    'is_deleted' BOOLEAN DEFAULT false,
    'deleted_at' DATETIME,
    'created_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    'updated_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS category (
    'id' INT AUTO_INCREMENT PRIMARY KEY,
    'uzer_id' INT NOT NULL REFERENCES uzer(id),
    'name' VARCHAR(32) DEFAULT "My Category",
    'created_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    'updated_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project (
    'id' INT AUTO_INCREMENT PRIMARY KEY,
    'uzer_id' INT NOT NULL REFERENCES uzer(id),
    'category_id' INT NOT NULL REFERENCES category(id),
    'name' VARCHAR(32) DEFAULT "My Project",
    'description' TEXT,
    'status' ENUM('created', 'started', 'completed', 'paused', 'stopped') DEFAULT 'created',
    'priority' ENUM('lowest', 'low', 'medium', 'high', 'highest') DEFAULT 'low',
    'start_time' DATETIME,
    'end_time' DATETIME,
    'due_time' DATETIME,
    'estimated_hr' DOUBLE,
    'daily_hr' DOUBLE,
    'is_deleted' BOOLEAN DEFAULT false,
    'deleted_at' DATETIME,
    'created_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    'updated_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS task (
    'id' INT AUTO_INCREMENT PRIMARY KEY,
    'uzer_id' INT NOT NULL REFERENCES uzer(id),
    'project_id' INT NOT NULL REFERENCES project(id),
    'description' TEXT,
    'status' ENUM('generated', 'planned', 'completed') DEFAULT 'generated',
    'priority' ENUM('lowest', 'low', 'medium', 'high', 'highest') DEFAULT 'low',
    'start_time' DATETIME,
    'duration_hr' DOUBLE DEFAULT 2,
    'created_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    'updated_at' TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);