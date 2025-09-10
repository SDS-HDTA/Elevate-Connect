DROP DATABASE IF EXISTS co_design_connect;

CREATE database IF NOT EXISTS co_design_connect;

USE co_design_connect;

CREATE TABLE invite_codes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    type tinyint unsigned NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE
    CURRENT_TIMESTAMP
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Invite Code';

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    -- phone VARCHAR(20) UNIQUE, | Pending client feedback, this column will be removed
    password VARCHAR(20) NOT NULL,
    role TINYINT unsigned NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'User Info';

CREATE TABLE verification_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expire_time TIMESTAMP NOT NULL
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Verification Code';

CREATE TABLE projects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creator_id INT NOT NULL,
    status tinyint unsigned NOT NULL DEFAULT 0,
    description TEXT,
    image_url VARCHAR(255),
    area VARCHAR(255),
    channel_id INT,
    category VARCHAR(100),
    deadline DATE,
    tags VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES user(id)
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Projects';

CREATE TABLE post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    author_id INT NOT NULL,
    title VARCHAR(255),
    content TEXT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES user(id) ON DELETE CASCADE
) engine=innodb DEFAULT CHARSET=utf8mb4 comment = 'Posts';

CREATE TABLE project_member (
    id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    user_id INT NOT NULL,
    role VARCHAR(20) DEFAULT 'MEMBER',
    joined_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_project_user (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Replies to posts';

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
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Iteration';

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
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
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

CREATE TABLE files (
    id INT PRIMARY KEY AUTO_INCREMENT,
    projectId INT NOT NULL,
    projectStatus TINYINT NOT NULL,
    type TINYINT,
    iterationId INT NOT NULL,
    name VARCHAR(255),
    source VARCHAR(255),
    creatorId INT NOT NULL,
    createTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updateTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Files';

CREATE TABLE markers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lat DOUBLE NOT NULL,
    lng DOUBLE NOT NULL,
    title VARCHAR(255),
    description VARCHAR(255),
    project_id INT NOT NULL,
    createTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updateTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Map Markers';

-- Insert invite code data
INSERT INTO invite_codes (email, code, type, is_used) VALUES
('matthew@adler.id.au', 'KPQMXRWZ', 0, FALSE),
('test@test.com', 'MABSHFJS', 0, FALSE);

-- Insert user data
INSERT INTO user (first_name, last_name, email, password, role) VALUES
('Matt', 'Adler', 'matthew@adler.id.au', 'test123456', 0);

-- Insert project data
INSERT INTO projects (name, creator_id, description, area, category, deadline) VALUES
('Project 1', 1, 'Project 1', 'Urban Development', 'Community Planning', '2025-09-15'),
('Project 2', 1, 'Project 2', 'Healthcare Innovation', 'Medical Technology', '2025-10-20'),
('Project 3', 1, 'Project 3', 'Environmental Conservation', 'Sustainability', '2025-11-30'),
('Project 4', 1, 'Project 4', 'Educational Technology', 'Digital Learning', '2025-12-10'),
('Project 5', 1, 'Project 5', 'Smart Transportation', 'Infrastructure', '2026-01-25');

-- Insert project memberships
INSERT INTO project_member (project_id, user_id, role) VALUES
(1, 1, "MEMBER");