# 생각
- 팔로워들의 장소:
유튜브 구독창 처럼
초기화면은 전체표출


- 특정 팔로워 선택 후 특정 팔로워만 볼 수 있게도 구현
  
# 설계
- 메인페이지
  - 로그인
  - 회원가입
  - (이메일 인증)
  - (보류 : 소셜로그인 (카톡, 구글))


- 세컨드페이지
  - 지도(이미지)
  - 플로팅 챗봇


- 일기장 작성페이지
  - 제목
  - 본문
  - 사진
  - 위치추가
  - 좋아요
  - 댓글
  - 조회수


- 마이페이지
  - 아이디변경
  - 비번변경
  - 닉네임 변경
  - 내가 작성한 글목록 (삭제, 수정)


- 돋보기 기능
  - "#" 검색 및 추천 알고리즘


# region
  - 서울: seoul
  - 부산: busan
  - 대구: daegu
  - 인천: incheon
  - 광주: gwangju
  - 대전: daejeon
  - 울산: ulsan
  - 세종: sejong
  - 경기: gyeonggi
  - 강원: gangwon
  - 충북: chungbuk
  - 충남: chungnam
  - 전북: jeonbuk
  - 전남: jeonnam
  - 경북: gyeongbuk
  - 경남: gyeongnam
  - 제주: jeju

# category
  - 장소: place
  - 음식: food

# mustacheajax

http://localhost:8088

# mysql
```
create database tjournal_db character set utf8mb4 collate utf8mb4_general_ci;
create user 'tjournal_user'@'%' identified by 'tjournal1234!';
grant all privileges on tjournal_db.* to 'tjournal_user'@'%' with grant option;
flush privileges;



CREATE TABLE `member_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `loginId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `createDt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `createId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updateDt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updateId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleteDt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleteId` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleteFlag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_tbl_uniq_loginId` (`loginId`),
  UNIQUE KEY `member_tbl_uniq_nickname` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE `board_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `createDt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `createId` bigint unsigned DEFAULT NULL,
  `updateDt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updateId` bigint unsigned DEFAULT NULL,
  `deleteDt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleteId` bigint unsigned DEFAULT NULL,
  `deleteFlag` tinyint(1) NOT NULL DEFAULT '0',
  `viewQty` int DEFAULT '0',
  `likeQty` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `board_tbl_member_tbl_createId` (`createId`),
  KEY `board_tbl_member_tbl_updateId` (`updateId`),
  KEY `board_tbl_member_tbl_deleteId` (`deleteId`),
  KEY `board_tbl_id_IDX` (`id`,`deleteFlag`) USING BTREE,
  KEY `board_tbl_name_IDX` (`name`,`deleteFlag`) USING BTREE,
  CONSTRAINT `board_tbl_member_tbl_createId` FOREIGN KEY (`createId`) REFERENCES `member_tbl` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `board_tbl_member_tbl_deleteId` FOREIGN KEY (`deleteId`) REFERENCES `member_tbl` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `board_tbl_member_tbl_updateId` FOREIGN KEY (`updateId`) REFERENCES `member_tbl` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE `comment_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `comment` varchar(1000) DEFAULT NULL,
  `tbl` varchar(200) NOT NULL,
  `boardId` bigint unsigned NOT NULL,
  `createDt` varchar(20) DEFAULT NULL,
  `createId` bigint DEFAULT NULL,
  `updateDt` varchar(20) DEFAULT NULL,
  `updateId` bigint DEFAULT NULL,
  `deleteDt` varchar(20) DEFAULT NULL,
  `deleteId` bigint DEFAULT NULL,
  `deleteFlag` tinyint NOT NULL DEFAULT '0',
  `writer` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_tbl_id_deleteFlag_IDX` (`id`,`deleteFlag`) USING BTREE,
  KEY `comment_tbl_tbl_boardId_IDX` (`tbl`,`boardId`) USING BTREE,
  KEY `comment_tbl_tbl_boardId_deleteFlag_IDX` (`tbl`,`boardId`,`deleteFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `sbfile_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ord` int unsigned NOT NULL DEFAULT '1',
  `fileType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `uniqName` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `length` int unsigned NOT NULL DEFAULT '0',
  `description` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tbl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `boardId` bigint unsigned NOT NULL DEFAULT '0',
  `deleteFlag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sbfile_tbl_id_IDX` (`id`,`deleteFlag`) USING BTREE,
  KEY `sbfile_tbl_tbl_boardId_IDX` (`tbl`,`boardId`) USING BTREE,
  KEY `sbfile_tbl_tbl_boardId_deleteFlag_IDX` (`tbl`,`boardId`,`deleteFlag`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



CREATE TABLE `sblike_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `tbl` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `createId` bigint unsigned NOT NULL,
  `boardId` bigint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sblike_tbl_member_tbl_createId` (`createId`),
  KEY `sblike_tbl_tbl_IDX` (`tbl`,`createId`,`boardId`) USING BTREE,
  CONSTRAINT `sblike_tbl_member_tbl_createId` FOREIGN KEY (`createId`) REFERENCES `member_tbl` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```
