CREATE database IF NOT EXISTS co_design_connect;
USE co_design_connect;

DROP TABLE IF EXISTS invite_codes;
CREATE TABLE invite_codes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    type tinyint unsigned NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) engine=innodb DEFAULT CHARSET=utf8 comment = 'Invite Code';
INSERT INTO invite_codes (email, code, type) VALUES
('111@111.com', '11111111', '0'),
('222@222.com', '22222222', '1');

DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    password VARCHAR(20) NOT NULL,
    type tinyint unsigned NOT NULL,
    invite_code_id INT UNIQUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (invite_code_id) REFERENCES invite_codes(id)
) engine=innodb DEFAULT CHARSET=utf8 comment = 'User Info';
INSERT INTO users (username, email, phone, password, type) VALUES
('1', '1@1.com', '1', '1', '0'),
('A', '123@123.com', '123', '123', '1'),
('B', '456@456.com', '456', '456', '0'),
('C', '789@789.com', '789', '789', '1');