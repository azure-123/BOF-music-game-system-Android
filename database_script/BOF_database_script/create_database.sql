use bof_database;

create table user_info
(
	user_id int auto_increment,
    user_password varchar(255),
    user_nickname varchar(255),
    user_sex varchar(5) check(user_sex in('f','m',null)),
    user_birthday date,
    primary key(user_id)
);

create table admin_info
(
	admin_id int,
    admin_password varchar(255),
    primary key(admin_id)
);

create table game_info
(
	game_name varchar(255),
    game_cname varchar(255),
    game_time date,
    game_company varchar(255),
    game_platform varchar(255),
    game_intro varchar(255),
    primary key(game_name)
);

create table music_info
(
	music_id int,
    music_name varchar(255) not null,
    music_creator varchar(255),
    music_link varchar(255),
    primary key(music_id)
);

create table beatmap_info
(
	beatmap_id int,
    beatmap_creator varchar(255),
    beatmap_link varchar(255),
    primary key(beatmap_id)
);

create table bga_info
(
	bga_id int,
    bga_creator varchar(255),
    bga_link varchar(255),
	primary key(bga_id)
);

create table bof
(
	bof_id int,
	bof_name varchar(20),
    first_id int not null,
    second_id int not null,
    third_id int not null,
    bof_link varchar(255),
    primary key(bof_name)
);

create table include
(
	include_id int,
    include_game varchar(255),
    include_level1 float,
    include_level2 float,
    include_level3 float,
    include_level4 float,
    include_flag int,#0 means no plus
    primary key(include_id,include_game)
);

create table team
(
	team_music_id int,
    team_bof_name varchar(20),
    team_name varchar(255),
    team_place int,
    primary key(team_music_id)
);

create table like_info
(
	user_id int,
    music_id int,
    primary key(user_id,music_id)
);
