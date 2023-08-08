-- 테스트용 데이터

-- Test Member Data
insert into member(email,password,nickname,role) values
('se6816@naver.com','password','se6816','ADMIN'),
('se6815@naver.com','password','se6815','MEMBER'),
('se6817@naver.com','password','se6817','MEMBER'),
('se6818@naver.com','password','se6818','MEMBER'),
('guest', 'guest', 'guest', 'GUEST'),
('se6819@naver.com','password','se6819','MEMBER'),
('guest2', 'guest2', 'guest2', 'GUEST');


-- Test Room Data
insert into room(password, max_count, specification, mode, end_date, room_code) values
('1234', '4', 'gradle', 'GAME',now(), 'sessionA'),
('1235', '3', 'row', 'NORMAL',now(), 'sessionB'),
('1236', '2', 'gradle', 'GAME',now(), 'sessionC'),
('1237', '1', 'row', 'NORMAL',now(), 'sessionD');

-- Test Participant Data
insert into participant(room_id, member_id, owner_yn, stream_id, connection_yn) values
('1', '1', 'Y', 'asdfasdfa', 'Y'),
('1', '3', 'N','asdasd', 'Y'),
('2', '2', 'Y','asdfab', 'Y'),
('3', '3', 'Y','cxzvxc', 'Y'),
('4', '4', 'Y','eqrvc', 'Y'),
('1', '5', 'N','dahaxd', 'N'),
('1', '6', 'N','1cbaasdf', 'N'),
('1', '7', 'N','fvbarhad', 'Y');



