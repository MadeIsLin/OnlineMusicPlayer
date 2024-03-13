create table music (
    id int primary key auto_increment,
    title varchar(50) not null,
    singer varchar(50) not null,
    time varchar(13) not null,
    url varchar(1000) not null,
    userid int(11) not null
);
