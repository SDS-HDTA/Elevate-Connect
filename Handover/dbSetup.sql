DROP DATABASE IF EXISTS co_design_connect;

CREATE database IF NOT EXISTS co_design_connect;

USE co_design_connect;

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
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (community_id) REFERENCES community(id)
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

-- Insert community data
INSERT INTO community (name, country, short_description) VALUES
('Melbourne Metro', 'Australia', 'Urban development community for Greater Melbourne area'),
('Sydney Harbor', 'Australia', 'Coastal urban planning and development community'),
('Brisbane Green', 'Australia', 'Sustainable development community for Southeast Queensland'),
('Perth Innovation', 'Australia', 'Technology and innovation hub for Western Australia'),
('Adelaide Connect', 'Australia', 'Community-focused development initiatives'),
('Canberra Future', 'Australia', 'National capital development and planning'),
('Darwin North', 'Australia', 'Tropical urban development community'),
('Hobart Heritage', 'Australia', 'Historical preservation and modern development'),
('Gold Coast Tourism', 'Australia', 'Tourism and hospitality development community'),
('Newcastle Industry', 'Australia', 'Industrial and port development community'),
('Wollongong Coastal', 'Australia', 'Coastal development and environmental protection'),
('Geelong Manufacturing', 'Australia', 'Industrial and manufacturing development hub');

-- Insert invite code data
INSERT INTO invite_codes (community_id, email, code, user_role) VALUES
(NULL, 'matthew@adler.id.au', 'KPQMXRWZ', 3),
(1, 'test@test.com', 'MABSHFJS', 0),
(5, 'sarah.johnson@email.com', 'ABC12345', 1),
(5, 'mike.chen@email.com', 'DEF67890', 2),
(4, 'emma.wilson@email.com', 'GHI11223', 0),
(1, 'david.brown@email.com', 'JKL44556', 1),
(4, 'lisa.taylor@email.com', 'MNO77889', 2),
(2, 'james.davis@email.com', 'PQR99001', 0),
(NULL, 'anna.miller@email.com', 'STU22334', 3),
(3, 'tom.anderson@email.com', 'VWX55667', 1),
(1,'sophie.white@email.com', 'YZA88990', 2),
(3, 'alex.garcia@email.com', 'BCD11223', 0);

-- Insert user data
INSERT INTO user (first_name, last_name, email, password, role) VALUES
('Matt', 'Adler', 'matthew@adler.id.au', 'test123456', 3),
('Sarah', 'Johnson', 'sarah.johnson@email.com', 'password123', 1),
('Mike', 'Chen', 'mike.chen@email.com', 'secure456', 2),
('Emma', 'Wilson', 'emma.wilson@email.com', 'mypass789', 0),
('David', 'Brown', 'david.brown@email.com', 'david123', 1),
('Lisa', 'Taylor', 'lisa.taylor@email.com', 'lisa456', 2),
('James', 'Davis', 'james.davis@email.com', 'james789', 0),
('Anna', 'Miller', 'anna.miller@email.com', 'anna012', 3),
('Tom', 'Anderson', 'tom.anderson@email.com', 'tom345', 1),
('Sophie', 'White', 'sophie.white@email.com', 'sophie678', 2),
('Alex', 'Garcia', 'alex.garcia@email.com', 'alex901', 0),
('Rachel', 'Martinez', 'rachel.martinez@email.com', 'rachel234', 1);

-- Insert verification codes data
INSERT INTO verification_codes (email, code, expire_time) VALUES
('matthew@adler.id.au', '123456', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('sarah.johnson@email.com', '234567', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('mike.chen@email.com', '345678', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('emma.wilson@email.com', '456789', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('david.brown@email.com', '567890', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('lisa.taylor@email.com', '678901', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('james.davis@email.com', '789012', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('anna.miller@email.com', '890123', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('tom.anderson@email.com', '901234', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
('sophie.white@email.com', '012345', DATE_ADD(NOW(), INTERVAL 1 HOUR));

-- Insert project data
INSERT INTO projects (creator_id, name, status, description, image_url, area, category, deadline, tags) VALUES
(1, 'Melbourne CBD Revitalization', 1, 'Comprehensive urban renewal project for Melbourne CBD', 'https://example.com/melbourne.jpg', 'Urban Development', 'Community Planning', '2025-09-15', 'urban,renewal,cbd'),
(2, 'Smart Traffic Management', 0, 'AI-powered traffic optimization system', 'https://example.com/traffic.jpg', 'Transportation', 'Smart City', '2025-10-20', 'traffic,ai,smart'),
(3, 'Harbor Bridge Maintenance', 2, 'Structural assessment and maintenance program', 'https://example.com/bridge.jpg', 'Infrastructure', 'Engineering', '2025-11-30', 'bridge,maintenance,infrastructure'),
(4, 'Coastal Protection Initiative', 1, 'Erosion control and marine ecosystem protection', 'https://example.com/coastal.jpg', 'Environmental', 'Conservation', '2025-12-10', 'coastal,environment,protection'),
(5, 'Green Energy Grid', 0, 'Renewable energy distribution network', 'https://example.com/energy.jpg', 'Energy', 'Sustainability', '2026-01-25', 'renewable,energy,grid'),
(6, 'Urban Farming Project', 1, 'Community-based urban agriculture initiative', 'https://example.com/farming.jpg', 'Agriculture', 'Community', '2026-02-15', 'farming,community,urban'),
(7, 'Digital Innovation Hub', 2, 'Technology startup incubator and workspace', 'https://example.com/innovation.jpg', 'Technology', 'Innovation', '2026-03-20', 'technology,startup,innovation'),
(8, 'Autonomous Vehicle Testing', 0, 'Self-driving car pilot program', 'https://example.com/autonomous.jpg', 'Transportation', 'Technology', '2026-04-10', 'autonomous,vehicle,testing'),
(9, 'Community Health Center', 1, 'Integrated healthcare facility development', 'https://example.com/health.jpg', 'Healthcare', 'Community Services', '2026-05-05', 'health,community,medical'),
(10, 'Education Technology Platform', 0, 'Digital learning management system', 'https://example.com/education.jpg', 'Education', 'Technology', '2026-06-01', 'education,technology,learning'),
(11, 'National Archive Digitization', 2, 'Historical document preservation project', 'https://example.com/archive.jpg', 'Cultural Heritage', 'Preservation', '2026-07-15', 'archive,digital,heritage'),
(12, 'Parliamentary Efficiency System', 1, 'Government process optimization platform', 'https://example.com/parliament.jpg', 'Government', 'Efficiency', '2026-08-20', 'government,efficiency,system');

-- Insert project memberships
INSERT INTO project_member (project_id, user_id, role) VALUES
(1, 1, 'ADMIN'),
(1, 2, 'MEMBER'),
(2, 2, 'ADMIN'),
(2, 3, 'MEMBER'),
(3, 3, 'ADMIN'),
(3, 4, 'MEMBER'),
(4, 4, 'ADMIN'),
(4, 5, 'MEMBER'),
(5, 5, 'ADMIN'),
(5, 6, 'MEMBER'),
(6, 6, 'ADMIN'),
(6, 7, 'MEMBER'),
(7, 7, 'ADMIN'),
(7, 8, 'MEMBER'),
(8, 8, 'ADMIN'),
(8, 9, 'MEMBER'),
(9, 9, 'ADMIN'),
(9, 10, 'MEMBER'),
(10, 10, 'ADMIN'),
(10, 11, 'MEMBER'),
(11, 11, 'ADMIN'),
(11, 12, 'MEMBER'),
(12, 12, 'ADMIN'),
(12, 1, 'MEMBER');

-- Insert iteration data
INSERT INTO iteration (project_id, project_status, iterated_time, title, start_date, end_date) VALUES
(1, 1, 1, 'Planning Phase', '2025-01-01', '2025-03-31'),
(1, 1, 2, 'Design Phase', '2025-04-01', '2025-06-30'),
(2, 0, 1, 'Research Phase', '2025-02-01', '2025-04-30'),
(3, 2, 1, 'Assessment Phase', '2025-01-15', '2025-04-15'),
(3, 2, 2, 'Implementation Phase', '2025-04-16', '2025-07-15'),
(4, 1, 1, 'Environmental Study', '2025-03-01', '2025-05-31'),
(5, 0, 1, 'Feasibility Study', '2025-02-15', '2025-05-15'),
(6, 1, 1, 'Community Consultation', '2025-01-01', '2025-03-31'),
(7, 2, 1, 'Technology Assessment', '2025-02-01', '2025-04-30'),
(8, 0, 1, 'Safety Evaluation', '2025-03-15', '2025-06-15'),
(9, 1, 1, 'Needs Assessment', '2025-01-15', '2025-04-15'),
(10, 0, 1, 'Platform Development', '2025-02-01', '2025-05-01');

-- Insert tasks data
INSERT INTO tasks (task_id, project_id, iteration_id, code, content, status, project_status, creator_id, assignee_id) VALUES
(0, 1, 1, 'TASK-001', 'Conduct site survey and analysis', 1, 1, 1, 2),
(0, 1, 1, 'TASK-002', 'Prepare preliminary design sketches', 0, 1, 1, 1),
(0, 1, 2, 'TASK-003', 'Develop detailed architectural plans', 0, 1, 1, 2),
(0, 2, 3, 'TASK-004', 'Research AI traffic algorithms', 1, 0, 2, 3),
(0, 2, 3, 'TASK-005', 'Analyze current traffic patterns', 0, 0, 2, 2),
(0, 3, 4, 'TASK-006', 'Structural integrity assessment', 2, 2, 3, 4),
(0, 3, 5, 'TASK-007', 'Develop maintenance schedule', 1, 2, 3, 3),
(0, 4, 6, 'TASK-008', 'Marine ecosystem impact study', 1, 1, 4, 5),
(0, 5, 7, 'TASK-009', 'Grid infrastructure planning', 0, 0, 5, 6),
(0, 6, 8, 'TASK-010', 'Community engagement sessions', 1, 1, 6, 7),
(0, 7, 9, 'TASK-011', 'Technology stack evaluation', 2, 2, 7, 8),
(0, 8, 10, 'TASK-012', 'Safety protocol development', 0, 0, 8, 9);

-- Insert posts data
INSERT INTO post (project_id, author_id, title, content) VALUES
(1, 1, 'Project Kickoff Meeting', 'Welcome everyone to the Melbourne CBD Revitalization project! Our first meeting is scheduled for next week.'),
(1, 2, 'Site Survey Results', 'Completed the initial site survey. Found some interesting historical foundations that we need to consider.'),
(2, 2, 'AI Algorithm Progress', 'The machine learning model is showing promising results in traffic pattern recognition.'),
(2, 3, 'Traffic Data Collection', 'Gathered 6 months of traffic data from key intersections. Ready for analysis phase.'),
(3, 3, 'Bridge Inspection Complete', 'Structural assessment reveals minor wear but overall good condition. Maintenance plan attached.'),
(3, 4, 'Maintenance Timeline', 'Proposed maintenance schedule to minimize traffic disruption during peak hours.'),
(4, 4, 'Environmental Impact Assessment', 'Initial studies show positive impact on marine life with proposed coastal protection measures.'),
(4, 5, 'Community Feedback Session', 'Local residents are very supportive of the coastal protection initiative.'),
(5, 5, 'Grid Design Proposal', 'Renewable energy grid design ready for review. Solar and wind integration included.'),
(5, 6, 'Energy Storage Solutions', 'Researching battery storage options for grid stability during peak demand.'),
(6, 6, 'Urban Farm Location Scouting', 'Identified 3 potential sites for community urban farming project.'),
(7, 7, 'Innovation Hub Layout', 'Architectural plans for the digital innovation hub are ready for stakeholder review.');

-- Insert replies data
INSERT INTO reply (post_id, author_id, content) VALUES
(1, 2, 'Looking forward to the kickoff meeting! I have some initial ideas to share.'),
(1, 1, 'Great! Please prepare a brief presentation for the team.'),
(2, 1, 'Excellent work on the survey. The historical findings add interesting complexity.'),
(3, 3, 'The AI approach seems very promising. When can we see a demo?'),
(3, 2, 'Demo scheduled for next Friday. Will share preliminary results.'),
(4, 2, 'Perfect timing on the data collection. This will feed directly into our model.'),
(5, 4, 'Good news on the bridge condition. Preventive maintenance is always best.'),
(6, 3, 'The timeline looks reasonable. Have you coordinated with traffic management?'),
(7, 5, 'The environmental benefits are substantial. Great work on the assessment.'),
(8, 4, 'Community support is crucial for project success. Well done on the engagement.'),
(9, 6, 'The grid design looks comprehensive. How does it handle peak load scenarios?'),
(10, 5, 'Battery storage is key. Have you considered distributed storage options?');

-- Insert files data
INSERT INTO files (projectId, projectStatus, type, iterationId, name, source, creatorId) VALUES
(1, 1, 1, 1, 'Site_Survey_Report.pdf', '/uploads/projects/1/site_survey.pdf', 1),
(1, 1, 2, 1, 'Preliminary_Sketches.dwg', '/uploads/projects/1/sketches.dwg', 2),
(2, 0, 1, 3, 'Traffic_Analysis_Data.xlsx', '/uploads/projects/2/traffic_data.xlsx', 3),
(2, 0, 3, 3, 'AI_Model_Presentation.pptx', '/uploads/projects/2/ai_presentation.pptx', 2),
(3, 2, 1, 4, 'Bridge_Inspection_Report.pdf', '/uploads/projects/3/inspection.pdf', 3),
(3, 2, 2, 5, 'Maintenance_Schedule.xlsx', '/uploads/projects/3/maintenance.xlsx', 4),
(4, 1, 1, 6, 'Environmental_Impact_Study.pdf', '/uploads/projects/4/env_study.pdf', 4),
(5, 0, 2, 7, 'Grid_Design_Plans.dwg', '/uploads/projects/5/grid_plans.dwg', 5),
(6, 1, 3, 8, 'Community_Feedback.docx', '/uploads/projects/6/feedback.docx', 6),
(7, 2, 1, 9, 'Technology_Assessment.pdf', '/uploads/projects/7/tech_assess.pdf', 7),
(8, 0, 2, 10, 'Safety_Protocols.pdf', '/uploads/projects/8/safety.pdf', 8),
(9, 1, 1, 11, 'Health_Center_Plans.pdf', '/uploads/projects/9/health_plans.pdf', 9);

-- Insert markers data
INSERT INTO markers (lat, lng, title, description, project_id) VALUES
(-37.8136, 144.9631, 'Melbourne CBD Site A', 'Primary development zone for CBD revitalization', 1),
(-37.8100, 144.9650, 'Melbourne CBD Site B', 'Secondary development area with heritage considerations', 1),
(-33.8688, 151.2093, 'Sydney Harbor Bridge', 'Main bridge structure requiring maintenance assessment', 3),
(-33.8567, 151.2152, 'Harbor Protection Zone', 'Coastal protection implementation area', 4),
(-27.4698, 153.0251, 'Brisbane Energy Hub', 'Central node for renewable energy grid', 5),
(-27.4650, 153.0300, 'Urban Farm Site 1', 'Primary location for community farming project', 6),
(-31.9505, 115.8605, 'Perth Innovation District', 'Technology hub development zone', 7),
(-31.9400, 115.8500, 'Autonomous Vehicle Test Track', 'Designated testing area for self-driving cars', 8),
(-34.9285, 138.6007, 'Adelaide Health Center', 'Community health facility location', 9),
(-35.2809, 149.1300, 'Canberra Archive Center', 'National archive digitization facility', 11),
(-35.2900, 149.1250, 'Parliament Tech Hub', 'Government efficiency system headquarters', 12),
(-42.8821, 147.3272, 'Hobart Heritage Site', 'Historical preservation and development area', 1);

-- Insert token data
INSERT INTO token (type, refresh_token, access_token) VALUES
('JWT', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh1', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.access1'),
('JWT', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh2', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.access2'),
('JWT', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh3', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.access3'),
('OAuth', 'oauth_refresh_token_1', 'oauth_access_token_1'),
('OAuth', 'oauth_refresh_token_2', 'oauth_access_token_2'),
('OAuth', 'oauth_refresh_token_3', 'oauth_access_token_3'),
('API_KEY', 'api_refresh_key_1', 'api_access_key_1'),
('API_KEY', 'api_refresh_key_2', 'api_access_key_2'),
('API_KEY', 'api_refresh_key_3', 'api_access_key_3'),
('SESSION', 'session_refresh_1', 'session_access_1'),
('SESSION', 'session_refresh_2', 'session_access_2'),
('SESSION', 'session_refresh_3', 'session_access_3');