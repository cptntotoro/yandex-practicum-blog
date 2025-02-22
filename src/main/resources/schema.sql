CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS posts, tags, post_tags, comments CASCADE;

CREATE TABLE IF NOT EXISTS posts (
    post_uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    image_url TEXT,
    text_content TEXT,
    text_preview VARCHAR(280),
    likes INT DEFAULT 0 CHECK (likes >= 0),
    date_time TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_posts_date_time ON posts(date_time);

CREATE TABLE IF NOT EXISTS tags (
    tag_uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS post_tags (
    pt_uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    post_uuid UUID REFERENCES posts(post_uuid) ON DELETE CASCADE NOT NULL,
    tag_uuid UUID REFERENCES tags(tag_uuid) ON DELETE CASCADE NOT NULL
);

CREATE INDEX idx_post_tags_post_uuid ON post_tags(post_uuid);
CREATE INDEX idx_post_tags_tag_uuid ON post_tags(tag_uuid);

CREATE TABLE IF NOT EXISTS comments (
    comment_uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    post_uuid UUID REFERENCES posts(post_uuid) ON DELETE CASCADE NOT NULL,
    text_content TEXT NOT NULL,
    date_time TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comments_post_uuid ON comments(post_uuid);
CREATE INDEX idx_comments_date_time ON comments(date_time);

-- Функция автообновления text_preview
CREATE OR REPLACE FUNCTION update_text_preview()
    RETURNS TRIGGER AS '
BEGIN
    NEW.text_preview :=
            CASE
                WHEN length(NEW.text_content) <= 280 THEN NEW.text_content
                ELSE substring(NEW.text_content FROM 1 FOR 280)
                END;
    RETURN NEW;
END;
' LANGUAGE plpgsql;

-- Триггер автообновления text_preview
CREATE TRIGGER trg_update_text_preview
    BEFORE INSERT OR UPDATE ON posts
    FOR EACH ROW
EXECUTE FUNCTION update_text_preview();