CREATE database IF NOT EXISTS co_design_connect;
USE co_design_connect;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    type tinyint unsigned NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) engine=innodb DEFAULT CHARSET=utf8 comment = 'User Info';

INSERT INTO users (username, email, phone, password, type) VALUES ('1', '1@1.com', '1', '1', '0');
INSERT INTO users (username, email, phone, password, type) VALUES ('A', '123@123.com', '123', '123', '1');
INSERT INTO users (username, email, phone, password, type) VALUES ('B', '456@456.com', '456', '456', '0');
INSERT INTO users (username, email, phone, password, type) VALUES ('C', '789@789.com', '789', '789', '1');