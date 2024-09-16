drop table if exists message_entity;
drop table if exists member;
drop table if exists message_entity_bad_word;
drop table if exists protocol_entity;

create table message_entity(
    id integer primary key auto_increment,
    content text not null,
    name varchar(32) not null,
    discriminator varchar(4) not null,
    time datetime not null
);

create table member(
    id integer primary key auto_increment,
    name varchar(32) not null,
    discriminator varchar(4) not null
);

create table message_entity_bad_word(
    id integer primary key auto_increment,
    message text not null
);

create table bot_message(
    id integer primary key auto_increment,
    message varchar(200) not null
);

create table protocol_entity(
    id integer primary key auto_increment,
    time datetime,
    typ varchar(20) not null ,
    content text not null
)
