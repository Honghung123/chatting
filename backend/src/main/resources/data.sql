-- Insert data

INSERT INTO users (id,avatar_id,avatar_url,cover_id,cover_url,created_at,email,is_activated,name,password,role ,status,username) VALUES
	 ('79073652-ad60-4b47-8662-f59e13a70633'::uuid,'bpazwyaxc7bwb4xkedqb','http://res.cloudinary.com/duii6cx3i/image/upload/v1739703940/bpazwyaxc7bwb4xkedqb.jpg',NULL,NULL,NULL,'nr7b8gn67@mozmail.com',false,'Nguyen Van A','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'nr7b8gn67'),
	 ('1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'damhonghung123','https://lh3.googleusercontent.com/a/ACg8ocJnfQCQINy8K3A_fimVkskHkTwB0uwTT8by8WfLUmPBDyIGCtzq=s96-c',NULL,NULL,NULL,'damhonghung123@gmail.com',true,'Hung Hong','$2a$10$rXsfbnHiP3ii.5dVBooEPO6o8lang3BI8.7BG6BG0WC0CMS4.nNCu','USER',true,'damhonghung123'),
	 ('2b123456-8c90-4abc-9def-123456789abc'::uuid,NULL,NULL,NULL,NULL,NULL,'user1@gmail.com',true,'Trần Quang Tâm','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'user1'),
	 ('3c234567-9d01-5bcd-0ef1-23456789abcd'::uuid,NULL,NULL,NULL,NULL,NULL,'user2@gmail.com',true,'Trần Huy','$2a$10$rXsfbnHiP3ii.5dVBooEPO6o8lang3BI8.7BG6BG0WC0CMS4.nNCu','USER',true,'user2'),
	 ('4d345678-0e12-6cde-1f23-3456789abcde'::uuid,NULL,NULL,NULL,NULL,NULL,'user3@gmail.com',true,'User Three','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'user3'),
	 ('5e456789-1f23-7def-2345-456789abcdef'::uuid,NULL,NULL,NULL,NULL,NULL,'user4@gmail.com',true,'User Four','$2a$10$rXsfbnHiP3ii.5dVBooEPO6o8lang3BI8.7BG6BG0WC0CMS4.nNCu','USER',true,'user4'),
	 ('6f56789a-2345-8ef0-3456-56789abcdef0'::uuid,NULL,NULL,NULL,NULL,NULL,'user5@gmail.com',true,'User Five','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'user5'),
	 ('6f56789a-2345-8ef0-3456-56789abcdef1'::uuid,NULL,NULL,NULL,NULL,NULL,'user6@gmail.com',true,'User Six','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'user6'),
	 ('2b123456-8c90-4abc-9def-123456789ade'::uuid,NULL,NULL,NULL,NULL,NULL,'user11@gmail.com',true,'Nguyen Van Tuyen','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'vanan'),
	 ('6f56789a-2345-8ef0-3456-56789abcdef5'::uuid,NULL,NULL,NULL,NULL,NULL,'user22@gmail.com',false,'John Smith','$2a$10$rXsfbnHiP3ii.5dVBooEPO6o8lang3BI8.7BG6BG0WC0CMS4.nNCu','USER',true,'johnsmith');
INSERT INTO users (id,avatar_id,avatar_url,cover_id,cover_url,created_at,email,is_activated,name,password,role,status,username) VALUES
	 ('4d345678-0e12-6cde-1f23-3456789abcdf'::uuid,NULL,NULL,NULL,NULL,NULL,'user33@gmail.com',false,'Alan Walkit','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'alanwalker'),
	 ('5e456789-1f23-7def-2345-456789abcdf2'::uuid,NULL,NULL,NULL,NULL,NULL,'user44@gmail.com',false,'Joe Biden','$2a$10$rXsfbnHiP3ii.5dVBooEPO6o8lang3BI8.7BG6BG0WC0CMS4.nNCu','USER',true,'joebien'),
	 ('6f56789a-2345-8ef0-3456-56789abcdef3'::uuid,NULL,NULL,NULL,NULL,NULL,'user55@gmail.com',false,'Helo Five','$2a$10$ymZXeAbuEnWLHvVRdxzNa.TT7ytNB9ZnxucgA4tb2QbLde7DdUoOm','USER',true,'helofive');


INSERT INTO friends (friend_id,user_id,created_at) VALUES
	 ('1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'2025-02-13 21:10:00.131193'),
	 ('2b123456-8c90-4abc-9def-123456789abc'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'2025-02-10 21:10:00.131193'),
	 ('3c234567-9d01-5bcd-0ef1-23456789abcd'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'2025-02-03 21:10:00.131193'),
	 ('4d345678-0e12-6cde-1f23-3456789abcde'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'2025-01-13 21:10:00.131193'),
	 ('5e456789-1f23-7def-2345-456789abcdef'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'2025-01-01 21:10:00.131193');
	 INSERT INTO friends (friend_id,user_id,created_at) VALUES  
	 ('2b123456-8c90-4abc-9def-123456789abc'::uuid,'1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'2025-02-13 21:10:00.131193'),
	 ('3c234567-9d01-5bcd-0ef1-23456789abcd'::uuid,'1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'2025-02-01 21:10:00.131193'),
	 ('4d345678-0e12-6cde-1f23-3456789abcde'::uuid,'1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'2025-01-09 21:10:00.131193'),
	 ('5e456789-1f23-7def-2345-456789abcdef'::uuid,'1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'2025-02-13 21:10:00.131193'), 
	 ('2b123456-8c90-4abc-9def-123456789ade'::uuid,'1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'2025-02-13 21:10:00.131193');
INSERT INTO friends (friend_id,user_id,created_at) VALUES 
	 ('3c234567-9d01-5bcd-0ef1-23456789abcd'::uuid,'2b123456-8c90-4abc-9def-123456789abc'::uuid,'2025-02-13 21:10:00.131193'),
	 ('4d345678-0e12-6cde-1f23-3456789abcde'::uuid,'2b123456-8c90-4abc-9def-123456789abc'::uuid,'2025-02-13 21:10:00.131193'),
	 ('5e456789-1f23-7def-2345-456789abcdef'::uuid,'2b123456-8c90-4abc-9def-123456789abc'::uuid,'2025-02-13 21:10:00.131193'),
	 ('6f56789a-2345-8ef0-3456-56789abcdef0'::uuid,'2b123456-8c90-4abc-9def-123456789abc'::uuid,'2025-02-13 21:10:00.131193'),
	 ('6f56789a-2345-8ef0-3456-56789abcdef1'::uuid,'2b123456-8c90-4abc-9def-123456789abc'::uuid,'2025-02-13 21:10:00.131193'),
	 ('2b123456-8c90-4abc-9def-123456789ade'::uuid,'2b123456-8c90-4abc-9def-123456789abc'::uuid,'2025-02-13 21:10:00.131193');

INSERT INTO conversations (id,conversation_type,created_by,created_at,latest_message_id) VALUES
	 ('afdd983d-32b3-42d5-a016-ffe0f7e87011','DIRECT','79073652-ad60-4b47-8662-f59e13a70633','2025-02-13 21:10:00.131193',null),
	 ('79510725-dc20-47ff-bc5c-928323e20495','DIRECT','79073652-ad60-4b47-8662-f59e13a70633','2025-02-13 21:10:11.733989',null);

INSERT INTO chat_members (conversation_id,user_id,is_admin) VALUES
	 ('afdd983d-32b3-42d5-a016-ffe0f7e87011','79073652-ad60-4b47-8662-f59e13a70633',false),
	 ('afdd983d-32b3-42d5-a016-ffe0f7e87011','1a7995bb-461e-492e-b897-7f474bd021c1',false),
	 ('79510725-dc20-47ff-bc5c-928323e20495','79073652-ad60-4b47-8662-f59e13a70633',false),
	 ('79510725-dc20-47ff-bc5c-928323e20495','2b123456-8c90-4abc-9def-123456789abc',false);

INSERT INTO chat_messages (conversation_id,sender_id,"content",message_content_type,attachment_id,status,created_at) VALUES
	 ('afdd983d-32b3-42d5-a016-ffe0f7e87011','79073652-ad60-4b47-8662-f59e13a70633','Hello','TEXT',NULL,NULL,'2025-02-13 21:10:00.162875'),
	 ('79510725-dc20-47ff-bc5c-928323e20495','79073652-ad60-4b47-8662-f59e13a70633','Hi ! Rose','TEXT',NULL,NULL,'2025-02-13 21:10:11.763332');

UPDATE conversations
SET latest_message_id = 1 WHERE id = 'afdd983d-32b3-42d5-a016-ffe0f7e87011';
UPDATE conversations
SET latest_message_id = 2 WHERE id = '79510725-dc20-47ff-bc5c-928323e20495';

INSERT INTO posts (id,user_id,caption,created_at,updated_at) VALUES
	 ('0db60b5c-99a8-4f3b-83cc-69714ddcbb53'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'Lorem ipsum dolor sit amet, consectetur adipiscing elit.','2025-02-16 16:24:04.214187','2025-02-16 16:24:04.214187'),
	 ('0db60b5c-99a8-4f3b-83cc-69714ddcbb23'::uuid,'1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'Lorem ipsum dolor sit amet, consectetur adipiscing elit.','2025-02-16 16:24:04.214187','2025-02-16 16:24:04.214187'),
	 ('0db60b5c-99a8-4f3b-83cc-69714ddcbb13'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'','2025-02-16 16:24:04.214187','2025-02-16 16:24:04.214187');

INSERT INTO post_attachments (file_id,file_url,file_name,"format",post_id) VALUES
	 ('faj8dlloyohccm9ampjh','http://res.cloudinary.com/duii6cx3i/image/upload/v1739697827/faj8dlloyohccm9ampjh.jpg','Doll_Lover.jpg','jpg','0db60b5c-99a8-4f3b-83cc-69714ddcbb53'::uuid),
	 ('faj8dlloyohccm9ampjt','http://res.cloudinary.com/duii6cx3i/image/upload/v1739697827/faj8dlloyohccm9ampjh.jpg','Doll_Lover.jpg','jpg','0db60b5c-99a8-4f3b-83cc-69714ddcbb13'::uuid),
	 ('faj8dlloyohccm9ampje','http://res.cloudinary.com/duii6cx3i/image/upload/v1739697827/faj8dlloyohccm9ampjh.jpg','Doll_Lover.jpg','jpg','0db60b5c-99a8-4f3b-83cc-69714ddcbb13'::uuid),
	 ('faj8dlloyohccm9ampjr','http://res.cloudinary.com/duii6cx3i/image/upload/v1739697827/faj8dlloyohccm9ampjh.jpg','Doll_Lover.jpg','jpg','0db60b5c-99a8-4f3b-83cc-69714ddcbb13'::uuid);

INSERT INTO stories (id,user_id,caption,created_at,updated_at) VALUES
	 ('ef404737-a35d-4eb1-bf4b-4471e88e5912'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'Lorem ipsum dolor sit amet, consectetur adipiscing elit.','2025-02-16 18:05:43.784743','2025-02-16 18:05:43.784743'),
	 ('ef404737-a35d-4eb1-bf4b-4471e88e5913'::uuid,'1a7995bb-461e-492e-b897-7f474bd021c1'::uuid,'Lorem ipsum dolor sit amet, consectetur adipiscing elit.','2025-02-16 18:05:43.784743','2025-02-16 18:05:43.784743'),
	 ('ef404737-a35d-4eb1-bf4b-4471e88e5914'::uuid,'79073652-ad60-4b47-8662-f59e13a70633'::uuid,'Lorem ipsum dolor sit amet, consectetur adipiscing elit.','2025-02-16 18:05:43.784743','2025-02-16 18:05:43.784743');
	 
INSERT INTO story_attachments (file_id,file_url,file_name,"format",story_id) VALUES
	 ('bpazwyaxc7bwb4xkedqb','http://res.cloudinary.com/duii6cx3i/image/upload/v1739703940/bpazwyaxc7bwb4xkedqb.jpg','Doll_Lover.jpg','jpg','ef404737-a35d-4eb1-bf4b-4471e88e5912'::uuid),
	 ('bpazwyaxc7bwb4xkedqt','http://res.cloudinary.com/duii6cx3i/image/upload/v1739703940/bpazwyaxc7bwb4xkedqb.jpg','Doll_Lover.jpg','jpg','ef404737-a35d-4eb1-bf4b-4471e88e5912'::uuid),
	 ('bpazwyaxc7bwb4xkedqs','http://res.cloudinary.com/duii6cx3i/image/upload/v1739703940/bpazwyaxc7bwb4xkedqb.jpg','Doll_Lover.jpg','jpg','ef404737-a35d-4eb1-bf4b-4471e88e5913'::uuid);
