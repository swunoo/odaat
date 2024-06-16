-- Insert data into uzer table
INSERT INTO uzer (name, email, password) VALUES
('John Doe', 'john@example.com', 'password123'),
('Alice Smith', 'alice@example.com', 'securepassword'),
('Bob Johnson', 'bob@example.com', 'bobspassword'),
('Emily Brown', 'emily@example.com', 'password1234'),
('Michael Wilson', 'michael@example.com', 'password5678'),
('Emma Davis', 'emma@example.com', 'password987'),
('James Taylor', 'james@example.com', 'password456'),
('Olivia Brown', 'olivia@example.com', 'password654'),
('William Martinez', 'william@example.com', 'password321'),
('Sophia Rodriguez', 'sophia@example.com', 'password789');

-- Insert data into category table
INSERT INTO category (uzer_id, name) VALUES
(1, 'Work'),
(1, 'Personal'),
(2, 'Study'),
(3, 'Hobby'),
(3, 'Fitness'),
(4, 'Travel'),
(4, 'Shopping'),
(5, 'Family'),
(6, 'Health'),
(6, 'Finance');

-- Insert data into project table
INSERT INTO project (uzer_id, category_id, name, description, status, priority, start_time, end_time, due_time, estimated_hr, daily_hr) VALUES
(1, 1, 'Website Redesign', 'Redesign company website with modern look and feel', 'created', 'medium', '2024-06-16 08:00:00', '2024-06-01 17:00:00', '2024-07-15 23:59:59', 120, 6),
(1, 1, 'Marketing Campaign', 'Launch new marketing campaign targeting social media platforms', 'created', 'high', '2024-07-01 09:00:00', '2024-09-01 18:00:00', '2024-06-15 23:59:59', 180, 8),
(2, 2, 'Research Paper', 'Write research paper on emerging technologies in artificial intelligence', 'created', 'medium', '2024-06-15 10:00:00', '2024-06-15 19:00:00', '2024-06-15 23:59:59', 150, 7),
(3, 3, 'Gardening Project', 'Start gardening project to grow vegetables and herbs', 'created', 'low', '2024-06-20 11:00:00', '2024-06-20 20:00:00', '2024-07-25 23:59:59', 100, 5),
(4, 4, 'Photography Course', 'Enroll in online photography course to improve skills', 'created', 'medium', '2024-06-25 12:00:00', '2024-06-25 21:00:00', '2024-06-16 23:59:59', 120, 6),
(5, 5, 'Home Workout Plan', 'Create home workout plan to stay fit and healthy', 'created', 'low', '2024-06-16 13:00:00', '2024-06-30 22:00:00', '2024-06-15 23:59:59', 90, 4),
(6, 6, 'Financial Planning', 'Plan monthly budget and savings goals', 'created', 'medium', '2024-06-05 14:00:00', '2024-09-05 23:00:00', '2024-06-20 23:59:59', 150, 7),
(7, 7, 'Vacation Trip', 'Plan vacation trip to exotic destination', 'created', 'high', '2024-06-16 15:00:00', '2024-09-10 00:00:00', '2024-06-25 23:59:59', 180, 8),
(8, 8, 'Family Gathering', 'Organize family gathering for upcoming holiday', 'created', 'medium', '2024-07-15 16:00:00', '2024-09-15 01:00:00', '2024-09-01 23:59:59', 120, 6),
(9, 9, 'Healthy Cooking Challenge', 'Participate in healthy cooking challenge', 'created', 'low', '2024-06-17 17:00:00', '2024-09-20 02:00:00', '2024-09-05 23:59:59', 90, 4),
(10, 10, 'Investment Strategy', 'Develop investment strategy for long-term financial growth', 'created', 'medium', '2024-07-25 18:00:00', '2024-09-25 03:00:00', '2024-09-10 23:59:59', 150, 7);

-- Insert data into task table
INSERT INTO task (uzer_id, project_id, description, status, priority, start_time, duration_hr) VALUES
(1, 1, 'Create wireframes for website', 'planned', 'medium', '2024-06-02 09:00:00', 4),
(1, 1, 'Design homepage layout', 'planned', 'high', '2024-06-16 10:00:00', 6),
(1, 2, 'Design promotional banners', 'generated', 'medium', NULL, 2),
(2, 3, 'Research on machine learning algorithms', 'generated', 'medium', NULL, 2),
(3, 4, 'Prepare soil for planting', 'generated', 'low', '2024-06-16 11:00:00', 3),
(4, 5, 'Complete module 1 - Introduction to photography', 'generated', 'medium', NULL, 2),
(5, 6, 'Create budget spreadsheet', 'planned', 'medium', '2024-06-15 14:00:00', 5),
(6, 7, 'Book flight tickets', 'generated', 'high', NULL, 2),
(7, 8, 'Prepare guest list', 'generated', 'low', '2024-06-17 16:00:00', 3),
(8, 9, 'Try new healthy recipes', 'generated', 'medium', NULL, 2),
(9, 10, 'Research investment options', 'planned', 'medium', '2024-06-16 18:00:00', 4),
(10, 1, 'Review website content', 'generated', 'low', '2024-06-15 18:00:00', 2);