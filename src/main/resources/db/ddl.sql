CREATE SCHEMA IF NOT EXISTS my_blog_back_app;

DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS posts;

CREATE TABLE IF NOT EXISTS posts
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    "text" VARCHAR(256) NOT NULL,
    tags TEXT[] NOT NULL DEFAULT ARRAY[]::TEXT[],
    likes_count BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_posts_tags_gin ON posts USING GIN (tags);

CREATE TABLE IF NOT EXISTS comments
(
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    "text" VARCHAR(256) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE INDEX IF NOT EXISTS idx_comments_post_id ON comments(post_id);
