INSERT INTO posts(title, text, tags) VALUES ('пост1', 'пост1_текст', ARRAY['пост1_тег1']);

INSERT INTO comments (post_id, text)
VALUES ((SELECT id FROM posts WHERE title = 'пост1'), 'пост1_комментарий1');
