DROP DATABASE IF EXISTS elevate_connect;
CREATE database IF NOT EXISTS elevate_connect;
USE elevate_connect;

CREATE TABLE community (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    short_description TEXT,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Community';

CREATE TABLE invite_codes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    community_id INT,
    email VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    user_role tinyint unsigned NOT NULL,
    country VARCHAR(255),
    organization VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (community_id) REFERENCES community(id)
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Invite Code';

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    community_id INT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role TINYINT unsigned NOT NULL,
    country VARCHAR(255),
    phone VARCHAR(255),
    organization VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (community_id) REFERENCES community(id)
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'User Info';

CREATE TABLE verification_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expire_time TIMESTAMP NOT NULL
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Verification Code';

CREATE TABLE project (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creator_id INT NOT NULL,
    community_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    current_stage tinyint unsigned NOT NULL DEFAULT 0,
    description TEXT,
    category tinyint unsigned NOT NULL,
    target_date DATE NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES user(id),
    FOREIGN KEY (community_id) REFERENCES community(id)
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Projects';

CREATE TABLE post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    author_id INT NOT NULL,
    title VARCHAR(255),
    content TEXT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES user(id) ON DELETE CASCADE
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Posts';

CREATE TABLE project_member (
    id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    user_id INT NOT NULL,
    joined_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_project_user (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Project Member';

CREATE TABLE reply (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    author_id INT NOT NULL,
    content TEXT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Replies to posts';

CREATE TABLE iteration (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    project_status TINYINT NOT NULL,
    iterated_time INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    start_date DATE DEFAULT NULL,
    end_date DATE DEFAULT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Iteration';

CREATE TABLE file (
    id INT PRIMARY KEY AUTO_INCREMENT,
    iteration_id INT,
    creator_id INT NOT NULL,
    type TINYINT NOT NULL,
    name VARCHAR(255),
    source VARCHAR(255),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (iteration_id) REFERENCES iteration(id),
    FOREIGN KEY (creator_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Files';

CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_id INT DEFAULT 0,
    project_id INT NOT NULL,
    iteration_id INT NOT NULL,
    code VARCHAR(20) NOT NULL,
    content TEXT,
    status TINYINT NOT NULL DEFAULT 0,
    project_status TINYINT NOT NULL,
    creator_id INT NOT NULL,
    assignee_id INT DEFAULT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (creator_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (iteration_id) REFERENCES iteration(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Tasks';

CREATE TABLE token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    access_token VARCHAR(255) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token';

CREATE TABLE marker (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    lat DOUBLE NOT NULL,
    lng DOUBLE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    type TINYINT NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Map Markers';

CREATE TABLE country (
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Country';

/*
Inserting this column last as there is cyclical referencing.
No matter what order the tables (project, iteration and file) are created in, there will
always be a column trying to reference a table that doesn't exist (see ERD)
*/
ALTER TABLE project
ADD project_image_id INT NOT NULL,
ADD FOREIGN KEY (project_image_id) REFERENCES file(id);