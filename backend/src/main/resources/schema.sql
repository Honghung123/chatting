DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- User schema
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(250) NOT NULL,
    username VARCHAR(250) NOT NULL,
    email VARCHAR(250) NOT NULL,
    password VARCHAR(250) NOT NULL,
    role VARCHAR(250) NOT NULL,
    is_activated BOOLEAN DEFAULT FALSE,
    status BOOLEAN NOT NULL,
    avatar_url TEXT,
    avatar_id VARCHAR(250),
    cover_url TEXT,
    cover_id VARCHAR(250),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Friends
CREATE TABLE friends ( 
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    friend_id UUID REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, friend_id)
);

-- Chat schema
CREATE TABLE conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_type VARCHAR(20) CHECK (conversation_type IN ('SELF', 'DIRECT', 'GROUP')) NOT NULL,
    created_by UUID REFERENCES users(id) ON DELETE CASCADE DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE chat_members (
    conversation_id UUID REFERENCES conversations(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    is_admin BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (conversation_id, user_id)
);

CREATE TABLE attachments (
    file_id VARCHAR(250) PRIMARY KEY,
    file_url TEXT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    format VARCHAR(10) DEFAULT 'Unknown'
);

CREATE TABLE chat_messages (
    id SERIAL PRIMARY KEY,
    conversation_id UUID REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id UUID REFERENCES users(id) ON DELETE CASCADE, 
    content TEXT DEFAULT '',
    message_content_type VARCHAR(20) CHECK (message_content_type IN ('TEXT', 'IMAGE', 'VIDEO', 
        'AUDIO', 'FILE')), 
    attachment_id VARCHAR(250) REFERENCES attachments(file_id) ON DELETE CASCADE,
    status VARCHAR(20) CHECK (status IN ('SENT', 'DELIVERED', 'READ')) DEFAULT 'SENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE conversations
ADD COLUMN latest_message_id INTEGER REFERENCES chat_messages(id) ON DELETE CASCADE;

-- Notification schema
CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    sender_id UUID REFERENCES users(id) ON DELETE CASCADE,
    recipient_id UUID REFERENCES users(id) ON DELETE CASCADE,
    target_id VARCHAR(50) NOT NULL,
    title VARCHAR(250) NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post schema
CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    caption TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post_attachments (
    file_id VARCHAR(250) PRIMARY KEY,
    file_url TEXT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    format VARCHAR(10) DEFAULT 'Unknown',
    post_id UUID REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE post_likes (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID REFERENCES posts(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, post_id)
);

CREATE TABLE post_comments (
    id SERIAL PRIMARY KEY,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID REFERENCES posts(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post_comment_likes (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    comment_id INTEGER REFERENCES post_comments(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, comment_id)
);

CREATE TABLE stories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    caption TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE story_attachments (
    file_id VARCHAR(250) PRIMARY KEY,
    file_url TEXT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    format VARCHAR(10) DEFAULT 'Unknown',
    story_id UUID REFERENCES stories(id) ON DELETE CASCADE
);
