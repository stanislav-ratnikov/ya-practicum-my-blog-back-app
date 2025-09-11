CREATE SCHEMA IF NOT EXISTS my_blog_back_app;

SET search_path TO my_blog_back_app;

DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS posts;

CREATE TABLE IF NOT EXISTS posts
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    "text" VARCHAR(256) NOT NULL,
    tags TEXT[] NOT NULL,
    like_count BIGINT NOT NULL
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

INSERT INTO posts(title, text, tags, like_count) VALUES ('пост1', 'пост1_текст', ARRAY['пост1_тег1'], 0);

INSERT INTO comments (post_id, text)
VALUES ((SELECT id FROM posts WHERE title = 'пост1'), 'пост1_комментарий1');
