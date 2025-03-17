create database co_design_connect;

CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO user (username) VALUES ('A');
INSERT INTO user (username) VALUES ('B');
INSERT INTO user (username) VALUES ('C');