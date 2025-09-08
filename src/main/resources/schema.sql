create table if not exists posts (
    id bigserial primary key,
    title varchar(256) not null,
    text varchar(256) not null
);

insert into posts(title, text) values ('пост 01', 'текст..');
