-- 테스트용 데이터



-- Test Member Data
insert into member(email,password,nickname,role) values
('guest', 'guest', 'guest', 'GUEST'),
('se6816@naver.com','password','se6816','ADMIN'),
('se6815@naver.com','password','se6815','MEMBER'),
('se6817@naver.com','password','se6817','MEMBER'),
('se6818@naver.com','password','se6818','MEMBER');


-- Test Room Data
insert into room(password, max_count, specification, mode, end_date, room_code) values
('1234', '4', 'gradle', 'GAME',now(), 'sessionA'),
('1235', '3', 'row', 'NORMAL',now(), 'sessionB'),
('1236', '2', 'gradle', 'GAME',now(), 'sessionC'),
('1237', '1', 'row', 'NORMAL',now(), 'sessionD');

-- Test Participant Data
insert into participant(room_id, member_id, nickname, owner_yn) values
('1', '1', 'guest', 'Y'),
('1', '3', 'guest', 'N'),
('2', '2', 'se6816', 'Y'),
('3', '3', 'se6815', 'Y'),
('4', '4', 'se6817', 'Y');


