drop table user_info;
drop table admin_info;
drop table game_info;
drop table music_info;
drop table beatmap_info;
drop table bga_info;
drop table bof;
drop table include;
drop table team;

select * from team;
select * from user_info;
select * from admin_info;
select * from bof;
select * from music_info;
select * from game_info;
select * from like_info;
delete from like_info where user_id=2 and music_id=2002;
desc admin_info;

truncate table user_info;
truncate table like_info;

select * from music_info;

select * from music_info where music_id>
(select bof_id%100*100 as id from bof where bof_name='BOFXVI')
 and music_id<
 (select (bof_id%100+1)*100 as id from bof where bof_name='BOFXVI')
 order by music_id asc;
 
 select * from music_info where lower(music_name) like '%burn%' or lower(music_creator) like '%burn%';
 
 select * from like_info natural join music_info where user_id=1;
 delete from user_info where user_id=4;
 insert into admin_info values(1,12345);