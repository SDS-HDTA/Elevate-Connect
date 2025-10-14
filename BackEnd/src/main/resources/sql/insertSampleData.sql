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
INSERT INTO invite_codes (community_id, email, code, user_role, country, organization) VALUES
(NULL, 'matthew@adler.id.au', 'KPQMXRWZ', 3, NULL, NULL),
(1, 'test@test.com', 'MABSHFJS', 0, NULL, NULL),
(NULL, 'sarah.johnson@email.com', 'ABC12345', 1, 'Australia', NULL),
(NULL, 'mike.chen@email.com', 'DEF67890', 2, NULL, 'Tech Solutions Ltd'),
(4, 'emma.wilson@email.com', 'GHI11223', 0, NULL, NULL),
(NULL, 'david.brown@email.com', 'JKL44556', 1, 'Australia', NULL),
(NULL, 'lisa.taylor@email.com', 'MNO77889', 2, NULL, 'Digital Innovations'),
(2, 'james.davis@email.com', 'PQR99001', 0, NULL, NULL),
(NULL, 'anna.miller@email.com', 'STU22334', 3, NULL, NULL),
(NULL, 'tom.anderson@email.com', 'VWX55667', 1, 'Australia', NULL),
(NULL,'sophie.white@email.com', 'YZA88990', 2, NULL, 'White Tech Group'),
(3, 'alex.garcia@email.com', 'BCD11223', 0, NULL, NULL);

-- Insert user data. All passwords are 'test#123'
INSERT INTO user (community_id, first_name, last_name, email, password, role, country, phone, organization) VALUES
(NULL, 'Matt', 'Adler', 'matthew@adler.id.au', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 3, NULL, NULL, NULL),
(NULL, 'Sarah', 'Johnson', 'sarah.johnson@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 1, 'Australia', '+61212345678', NULL),
(NULL, 'Mike', 'Chen', 'mike.chen@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 2, NULL, '+61298765432' ,'Tech Solutions Ltd'),
(1, 'Emma', 'Wilson', 'emma.wilson@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 0, NULL, '+61234567890', NULL),
(NULL, 'David', 'Brown', 'david.brown@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 1, 'Australia', '+61245678901', NULL),
(NULL, 'Lisa', 'Taylor', 'lisa.taylor@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 2, NULL, '+61234567890', 'Digital Innovations'),
(2, 'James', 'Davis', 'james.davis@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 0, NULL, '+61234567890', NULL),
(NULL, 'Anna', 'Miller', 'anna.miller@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 3, NULL, '+61234567890', NULL),
(NULL, 'Tom', 'Anderson', 'tom.anderson@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 1, 'Australia', '+61234567890', NULL),
(NULL, 'Sophie', 'White', 'sophie.white@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 2, NULL, '+61234567890', 'White Tech Group'),
(3, 'Alex', 'Garcia', 'alex.garcia@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 0, NULL, '+61234567890', NULL),
(NULL, 'Rachel', 'Martinez', 'rachel.martinez@email.com', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 1, 'Australia', '+61234567890', NULL),
(1, 'Community Insight Partner', 'test', 'cip@test.user', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 0, NULL, '+61234567890', NULL),
(NULL, 'Country Colab Partner', 'test', 'ccp@test.user', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 1, 'Australia', '+61234567890', NULL),
(NULL, 'Humanitarian Impact Partner', 'test', 'hip@test.user', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 2, NULL, '+61234567890', 'Tech Solutions Ltd'),
(NULL, 'Elevate Facilitation Lead', 'test', 'efl@test.user', '$2a$12$9H3WCEFi8.2z/MHGYQpEV.RJHMijaGbNv6hTtfQ0VCMglrnYXWjay', 3, NULL, '+61234567890', NULL);

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
INSERT INTO project (creator_id, community_id, name, current_stage, description, category, target_date) VALUES
(1, 1, 'Melbourne CBD Revitalization', 0, 'Comprehensive urban renewal project for Melbourne CBD', 1, '2025-09-15'),
(2, 1, 'Smart Traffic Management', 2, 'AI-powered traffic optimization system', 2, '2025-10-20'),
(3, 2, 'Harbor Bridge Maintenance', 4, 'Structural assessment and maintenance program', 3, '2025-11-30'),
(4, 2, 'Coastal Protection Initiative', 3, 'Erosion control and marine ecosystem protection', 4, '2025-12-10'),
(5, 3, 'Green Energy Grid', 5, 'Renewable energy distribution network', 5, '2026-01-25'),
(6, 3, 'Urban Farming Project', 0, 'Community-based urban agriculture initiative', 1, '2026-02-15'),
(7, 4, 'Digital Innovation Hub', 3, 'Technology startup incubator and workspace', 2, '2026-03-20'),
(8, 4, 'Autonomous Vehicle Testing', 1, 'Self-driving car pilot program', 2, '2026-04-10'),
(9, 5, 'Community Health Center', 1, 'Integrated healthcare facility development', 1, '2026-05-05'),
(10, 5, 'Education Technology Platform', 4, 'Digital learning management system', 2, '2026-06-01'),
(11, 6, 'National Archive Digitization', 2, 'Historical document preservation project', 4, '2026-07-15'),
(12, 6, 'Parliamentary Efficiency System', 5, 'Government process optimization platform', 3, '2026-08-20');

-- Insert project memberships
INSERT INTO project_member (project_id, user_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(2, 3),
(3, 3),
(3, 4),
(4, 4),
(4, 5),
(5, 5),
(5, 6),
(6, 6),
(6, 7),
(7, 7),
(7, 8),
(8, 8),
(8, 9),
(9, 9),
(9, 10),
(10, 10),
(10, 11),
(11, 11),
(11, 12),
(12, 12),
(12, 1);

-- Insert iteration data
INSERT INTO iteration (project_id, project_status, iterated_time, title, start_date, end_date) VALUES
(1, 0, 1, 'Initial Planning', '2024-12-01', '2024-12-31'),
(1, 1, 2, 'Planning Phase', '2025-01-01', '2025-03-31'),
(1, 1, 3, 'Design Phase', '2025-04-01', '2025-06-30'),
(2, 0, 1, 'Research Phase', '2025-02-01', '2025-04-30'),
(3, 0, 1, 'Initial Assessment', '2024-12-15', '2025-01-14'),
(3, 2, 2, 'Assessment Phase', '2025-01-15', '2025-04-15'),
(3, 2, 3, 'Implementation Phase', '2025-04-16', '2025-07-15'),
(4, 0, 1, 'Project Initiation', '2025-02-01', '2025-02-28'),
(4, 1, 2, 'Environmental Study', '2025-03-01', '2025-05-31'),
(5, 0, 1, 'Feasibility Study', '2025-02-15', '2025-05-15'),
(6, 0, 1, 'Project Setup', '2024-12-01', '2024-12-31'),
(6, 1, 2, 'Community Consultation', '2025-01-01', '2025-03-31'),
(7, 0, 1, 'Project Kickoff', '2025-01-01', '2025-01-31'),
(7, 2, 2, 'Technology Assessment', '2025-02-01', '2025-04-30'),
(8, 0, 1, 'Safety Evaluation', '2025-03-15', '2025-06-15'),
(9, 0, 1, 'Project Initiation', '2024-12-15', '2025-01-14'),
(9, 1, 2, 'Needs Assessment', '2025-01-15', '2025-04-15'),
(10, 0, 1, 'Platform Development', '2025-02-01', '2025-05-01'),
(11, 0, 1, 'Project Planning', '2025-01-01', '2025-02-28'),
(11, 2, 2, 'Digitization Phase', '2025-03-01', '2025-06-30'),
(12, 0, 1, 'Requirements Analysis', '2025-01-15', '2025-03-15'),
(12, 5, 2, 'System Implementation', '2025-03-16', '2025-07-31');

-- Insert tasks data
INSERT INTO tasks (task_id, project_id, iteration_id, code, content, status, project_status, creator_id, assignee_id) VALUES
(0, 1, 1, 'TASK-001', 'Conduct site survey and analysis', 1, 1, 1, 2),
(0, 1, 2, 'TASK-002', 'Prepare preliminary design sketches', 0, 1, 1, 1),
(0, 1, 3, 'TASK-003', 'Develop detailed architectural plans', 0, 1, 1, 2),
(0, 2, 4, 'TASK-004', 'Research AI traffic algorithms', 1, 0, 2, 3),
(0, 2, 4, 'TASK-005', 'Analyze current traffic patterns', 0, 0, 2, 2),
(0, 3, 5, 'TASK-006', 'Structural integrity assessment', 2, 2, 3, 4),
(0, 3, 6, 'TASK-007', 'Develop maintenance schedule', 1, 2, 3, 3),
(0, 4, 8, 'TASK-008', 'Marine ecosystem impact study', 1, 1, 4, 5),
(0, 5, 10, 'TASK-009', 'Grid infrastructure planning', 0, 0, 5, 6),
(0, 6, 11, 'TASK-010', 'Community engagement sessions', 1, 1, 6, 7),
(0, 7, 13, 'TASK-011', 'Technology stack evaluation', 2, 2, 7, 8),
(0, 8, 15, 'TASK-012', 'Safety protocol development', 0, 0, 8, 9);

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

-- Insert file data
INSERT INTO file (iteration_id, creator_id, type, name, source) VALUES
(1, 1, 1, 'Site_Survey_Report.pdf', '/uploads/projects/1/site_survey.pdf'),
(1, 2, 2, 'Preliminary_Sketches.dwg', '/uploads/projects/1/sketches.dwg'),
(4, 3, 1, 'Traffic_Analysis_Data.xlsx', '/uploads/projects/2/traffic_data.xlsx'),
(4, 2, 3, 'AI_Model_Presentation.pptx', '/uploads/projects/2/ai_presentation.pptx'),
(5, 3, 1, 'Bridge_Inspection_Report.pdf', '/uploads/projects/3/inspection.pdf'),
(6, 4, 2, 'Maintenance_Schedule.xlsx', '/uploads/projects/3/maintenance.xlsx'),
(8, 4, 1, 'Environmental_Impact_Study.pdf', '/uploads/projects/4/env_study.pdf'),
(10, 5, 2, 'Grid_Design_Plans.dwg', '/uploads/projects/5/grid_plans.dwg'),
(11, 6, 3, 'Community_Feedback.docx', '/uploads/projects/6/feedback.docx'),
(13, 7, 1, 'Technology_Assessment.pdf', '/uploads/projects/7/tech_assess.pdf'),
(15, 8, 2, 'Safety_Protocols.pdf', '/uploads/projects/8/safety.pdf'),
(16, 9, 1, 'Health_Center_Plans.pdf', '/uploads/projects/9/health_plans.pdf');

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

-- Insert additional file data
INSERT INTO file (iteration_id, creator_id, type, name, source) VALUES
(18, 1, 1, 'Project_Requirements.pdf', '/uploads/projects/10/requirements.pdf');