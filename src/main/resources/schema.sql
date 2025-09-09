create table if not exists posts
(
    id bigserial primary key,
    title varchar(256) not null,
    text varchar(256) not null,
    likes_count bigint not null
);

create table if not exists tags
(
    id bigserial primary key,
    post_id bigint not null,
    tag varchar(256) not null,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE INDEX idx_tags_post_id ON tags(post_id);

create table if not exists comments
(
    id bigserial primary key,
    post_id bigint not null,
    text varchar(256) not null,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE INDEX idx_comments_post_id ON comments(post_id);

insert into posts(title, text, likes_count) values ('пост1', 'пост1_текст', 0);

insert into tags(post_id, tag) values ((select id from posts where title = 'пост1'), 'пост1_тег1');

insert into comments (post_id, text)
values ((select id from posts where title = 'пост1'), 'пост1_комментарий1');
