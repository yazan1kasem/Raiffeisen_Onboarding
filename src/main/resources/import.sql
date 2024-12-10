INSERT INTO u_user (u_id, u_username, u_password, created_at, updated_at, u_role) VALUES (UUID(), 'user1', 'password1',  NOW(), NOW(), 'USER');
INSERT INTO u_user (u_id, u_username, u_password, created_at, updated_at, u_role) VALUES (UUID(), 'user2', 'password2',  NOW(), NOW(), 'USER');
INSERT INTO u_user (u_id, u_username, u_password, created_at, updated_at, u_role) VALUES (UUID(), 'admin1', 'adminpass1',  NOW(), NOW(), 'ADMIN');
INSERT INTO u_user (u_id, u_username, u_password, created_at, updated_at, u_role) VALUES (UUID(), 'admin2', 'adminpass2',  NOW(), NOW(), 'ADMIN');
INSERT INTO u_user (u_id, u_username, u_password, created_at, updated_at, u_role) VALUES (UUID(), 'user3', 'password3', NOW(), NOW(), 'USER');

INSERT INTO p_position (p_id, p_bez) VALUES (UUID(), 'Manager');
INSERT INTO p_position (p_id, p_bez) VALUES (UUID(), 'Team Lead');
INSERT INTO p_position (p_id, p_bez) VALUES (UUID(), 'Software Developer');
INSERT INTO p_position (p_id, p_bez) VALUES (UUID(), 'Intern');
INSERT INTO p_position (p_id, p_bez) VALUES (UUID(), 'Administrator');

INSERT INTO d_device (d_id, d_gerät, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Laptop', 'Windows 11', 'Office Suite', 'work');
INSERT INTO d_device (d_id, d_gerät, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Desktop', 'Ubuntu', 'Development Tools', 'dev');
INSERT INTO d_device (d_id, d_gerät, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Tablet', 'iOS', 'Productivity Apps', 'mobile');
INSERT INTO d_device (d_id, d_gerät, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Smartphone', 'Android', 'Communication Apps', 'phone');
INSERT INTO d_device (d_id, d_gerät, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Server', 'Linux', 'Database', 'server');

INSERT INTO d_department (d_id, d_geraet, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'HR Equipment', 'HR Tools', 'Payroll Software', 'hr');
INSERT INTO d_department (d_id, d_geraet, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'IT Equipment', 'Tech Tools', 'Monitoring Software', 'it');
INSERT INTO d_department (d_id, d_geraet, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Finance Equipment', 'Finance Tools', 'Budgeting Software', 'finance');
INSERT INTO d_department (d_id, d_geraet, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Marketing Equipment', 'Marketing Tools', 'Analytics Software', 'marketing');
INSERT INTO d_department (d_id, d_geraet, d_administration, d_software, d_suchbegriff) VALUES (UUID(), 'Operations Equipment', 'Ops Tools', 'Logistics Software', 'operations');

INSERT INTO c_checklist (c_id, c_saved, c_überschrift, d_c_id, a_c_id, p_c_id) VALUES (UUID(), true, 'New Hire Setup', (SELECT d_id FROM d_device LIMIT 1 OFFSET 0), (SELECT d_id FROM d_department LIMIT 1 OFFSET 0), (SELECT p_id FROM p_position LIMIT 1 OFFSET 0));
INSERT INTO c_checklist (c_id, c_saved, c_überschrift, d_c_id, a_c_id, p_c_id) VALUES (UUID(), false, 'System Update', (SELECT d_id FROM d_device LIMIT 1 OFFSET 1), (SELECT d_id FROM d_department LIMIT 1 OFFSET 1), (SELECT p_id FROM p_position LIMIT 1 OFFSET 1));
INSERT INTO c_checklist (c_id, c_saved, c_überschrift, d_c_id, a_c_id, p_c_id) VALUES (UUID(), true, 'Weekly Maintenance', (SELECT d_id FROM d_device LIMIT 1 OFFSET 2), (SELECT d_id FROM d_department LIMIT 1 OFFSET 2), (SELECT p_id FROM p_position LIMIT 1 OFFSET 2));
INSERT INTO c_checklist (c_id, c_saved, c_überschrift, d_c_id, a_c_id, p_c_id) VALUES (UUID(), false, 'Security Audit', (SELECT d_id FROM d_device LIMIT 1 OFFSET 3), (SELECT d_id FROM d_department LIMIT 1 OFFSET 3), (SELECT p_id FROM p_position LIMIT 1 OFFSET 3));
INSERT INTO c_checklist (c_id, c_saved, c_überschrift, d_c_id, a_c_id, p_c_id) VALUES (UUID(), true, 'Marketing Strategy Review', (SELECT d_id FROM d_device LIMIT 1 OFFSET 4), (SELECT d_id FROM d_department LIMIT 1 OFFSET 4), (SELECT p_id FROM p_position LIMIT 1 OFFSET 4));

INSERT INTO c_checkliste_has_u_user (c_checkliste_c_id, u_user_u_id) VALUES ((SELECT c_id FROM c_checklist LIMIT 1 OFFSET 0), (SELECT u_id FROM u_user LIMIT 1 OFFSET 0));
INSERT INTO c_checkliste_has_u_user (c_checkliste_c_id, u_user_u_id) VALUES ((SELECT c_id FROM c_checklist LIMIT 1 OFFSET 1), (SELECT u_id FROM u_user LIMIT 1 OFFSET 1));
INSERT INTO c_checkliste_has_u_user (c_checkliste_c_id, u_user_u_id) VALUES ((SELECT c_id FROM c_checklist LIMIT 1 OFFSET 2), (SELECT u_id FROM u_user LIMIT 1 OFFSET 2));
INSERT INTO c_checkliste_has_u_user (c_checkliste_c_id, u_user_u_id) VALUES ((SELECT c_id FROM c_checklist LIMIT 1 OFFSET 3), (SELECT u_id FROM u_user LIMIT 1 OFFSET 3));
INSERT INTO c_checkliste_has_u_user (c_checkliste_c_id, u_user_u_id) VALUES ((SELECT c_id FROM c_checklist LIMIT 1 OFFSET 4), (SELECT u_id FROM u_user LIMIT 1 OFFSET 4));
