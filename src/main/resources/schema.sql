create table if not exists posts
(
    id bigserial primary key,
    title varchar(256) not null,
    text varchar(256) not null
);

create table if not exists comments
(
    id bigserial primary key,
    post_id bigserial not null,
    text varchar(256) not null,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE INDEX idx_comments_post_id ON comments(post_id);

insert into posts(title, text) values ('пост 01', 'текст..');

insert into comments (post_id, text)
values ((select id from posts where title = 'пост 01'), 'комментарий 01');
