# 생각
- 팔로워들의 장소:
유튜브 구독창 처럼
- 초기화면은 전체표출
- 특정 팔로워 선택 후 특정 팔로워만 볼 수 있게도 구현

2025-01-29
카카오 api 로그인, 블로그 view 사진 추출  

1️⃣ 지도와 연계한 기능 강화
  - GPS 활용한 내 위치 기반 글쓰기:
    - 현재 사용자가 위치한 곳을 자동으로 불러와서 글을 바로 작성할 수 있도록 하면 편의성과 재미 증가.

  - 경로 저장 및 여행 코스 추천 기능:
    - 여러 핀을 찍으면 자동으로 여행 경로를 추천해주거나, 유저들이 다녀온 인기 코스를 볼 수 있게 하는 기능.
    - 작성된 여행일기를 바탕으로 인기 여행 경로를 랭킹으로 제공.

2️⃣ 사진과 영상 연동
  - 사진 클릭시 해당좌표 이동, 좌표 클릭시 관련 이미지 표출?

3️⃣ 추천과 랭킹 시스템
  - 장소/음식 별 별점 리뷰 시스템:
    - 유저들이 음식점이나 관광지에 별점 평가를 할 수 있게 하여 인기있는 장소를 쉽게 파악 가능.
  - '찜' 기능:
    - 다른 유저의 여행일기를 보고 마음에 드는 장소를 저장해두고 나중에 확인할 수 있는 기능.

4️⃣ 맞춤형 알림
  - 팔로우하는 유저가 새 여행일기를 올리거나, 저장해 둔 지역에 대한 새로운 게시글이 등록되면 알림을 보내주는 기능 추가.

5️⃣ 소셜 공유 및 커뮤니티 확장
  - SNS 연동 및 게시글 공유 기능
    - 인스타그램, 페이스북, 카카오톡 등 다양한 SNS와 간편히 연동 및 공유 가능하도록 설정.
  - 여행기념 배지 시스템
    - 특정 지역을 많이 다니거나 다양한 음식을 경험한 사용자에게 디지털 배지 형태의 성취감을 제공.

6️⃣ 분석과 통계
  - 개인별 여행 통계 제공
    - 다녀온 지역, 장소, 먹어본 음식 등의 데이터를 통계적으로 보여주는 기능을 제공해 개인의 여행 기록을 시각화.




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


- 팔로워, 팔로잉 
  - 사람 목록 표출
  - ( 팔로우, 언팔로우 ) 
    - loginUser id, 회원 id 
    - count  ->  1  ->  언팔
    - count  ->  0  ->  팔로우


# 구현
1️⃣ 지역별 게시판 관리
  - 전국 및 개별 지역(서울, 부산 등 총 17개 시·도)을 선택해 게시글 등록 가능
  - 각 지역을 Enum 클래스로 관리하여 데이터 일관성 유지

2️⃣ 게시글 등록 기능
  - 게시글 제목과 내용 작성 (내용 부분은 contenteditable로 텍스트 및 이미지 자유롭게 구성 가능)
  - 이미지 업로드 시 즉시 미리보기 지원, 이미지 크기 조절 가능
  - 카테고리 설정(예: 음식, 장소 등)

3️⃣ 지도 연동 및 마커 기능
  - 네이버 지도 API를 활용한 지도 표시
  - 지도에서 마우스 우클릭을 통한 마커 추가 기능 제공
  - 마커 추가 시 사용자 입력을 통해 마커 내용 작성
  - 마커 클릭 시 내용 수정 가능
  - 마커 우클릭 시 마커 삭제 가능
  - 리버스 지오코딩을 이용해 선택한 지역 내에만 마커 추가 허용 (지역 제한 기능)
    - 특정 지역을 벗어난 곳을 클릭하면 경고 메시지와 함께 마커 생성 제한

4️⃣ 주소 검색 및 자동완성 기능
  - 네이버 지역 검색 API를 연동하여 사용자가 주소 검색 시 자동완성 제공
  - 주소 선택 시 지도에서 자동으로 검색된 위치로 이동 및 마커 추가 지원
  - 선택된 주소의 좌표를 지도상에 자동으로 표시

5️⃣ 게시글 수정 기능
  - 기존에 등록된 게시글 내용과 이미지를 서버로부터 조회하여 로드
  - 이미지 삭제 및 추가, 본문 수정 지원

6️⃣ 파일 업로드 및 관리
  - 이미지 파일 업로드 및 서버에 저장 (FormData, JSON Blob 활용)
  - 이미지 파일에 타임스탬프 추가하여 파일명 중복 방지
  - 기존 파일 삭제 시 서버에 삭제 요청 전송 및 삭제 상태 처리 지원

7️⃣ 에러 핸들링 및 예외 처리
  - 지역 정보 등 필수 값 누락 시 에러 페이지 처리
  - AJAX 요청 실패 시 적절한 에러 메시지 표시 및 사용자 알림 지원

8️⃣ 데이터 구조화 및 JSON 활용
  - 게시글 및 마커 데이터 JSON 형태로 구조화하여 서버와 효율적인 데이터 교환
  - 서버에서는 클라이언트가 보내는 마커 데이터를 기반으로 신규 마커 생성, 기존 마커 수정·삭제 여부 결정

9️⃣ 편의성 및 UX 고려
  - 게시글 및 이미지 등록 즉시 피드백 제공 (즉각적인 화면 반영)
  - 자동완성, 지도 연동 등 직관적인 사용자 경험 제공
  - 지도상에서 바로 위치 선택 및 편집 가능

🔑 소셜 로그인 및 회원관리 기능
  - 네이버 및 카카오 소셜 로그인 연동
    - OAuth2 기반 네이버, 카카오 계정을 이용한 로그인 및 회원가입 지원
    - 사용자의 편의성 증대를 위해 간편 로그인을 통한 사용자 인증 구현
  - 회원가입 시 비밀번호 암호화 처리
    - Spring Security의 PasswordEncoder 라이브러리를 이용한 비밀번호 암호화
    - 암호화된 비밀번호로 보안성 강화 및 사용자 데이터 보호
    - 신규 회원 등록 시 기본적인 사용자 권한(USER) 부여
    - 회원가입 초기 활성 상태는 비활성(active=false)으로 설정하여 관리자가 활성화를 승인할 수 있도록 구현
      
📧 이메일 인증 기능
  - 이메일 인증 코드 발송
    - 인증 코드 랜덤 생성 및 이메일(HTML 형식)로 전송
    - 인증 코드 유효기간: 발송 후 1일
  - 인증 코드 검증
    - 사용자 입력 코드 검증 및 만료 여부 확인
  - 재요청 제한
    - 인증 이메일 재전송 간격: 최소 1분
    - 제한 시간 초 단위 안내
  - 인증 코드 자동 삭제
    - 매일 12시에 만료된 코드 자동 삭제 (스케줄러 이용)

🔧 주요 기술 스택
  - 프론트엔드
    - HTML, CSS, JavaScript, jQuery, AJAX
    - 네이버 지도 API, 네이버 검색 API

  - 백엔드
    - Spring Boot, REST API, JSON 데이터 처리

  - 데이터 저장
    - DB에 게시글, 마커, 파일 정보 저장 및 관리
    - 이미지 파일 서버 저장 후 관리

✨ 사용자 플로우 예시
  - 게시글 작성 시
    - 게시글 제목 및 본문 작성 → 이미지 업로드 → 지도에서 위치 선택 → 저장
  - 게시글 수정 시
    - 기존 게시글 조회 → 본문 수정 및 이미지 추가/삭제 → 기존 마커 확인 및 수정 → 저장

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
  `snsId` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `provider` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_tbl_uniq_loginId` (`loginId`),
  UNIQUE KEY `member_tbl_uniq_nickname` (`nickname`),
  UNIQUE KEY `member_tbl_uniq_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci



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
  `region` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `category` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `board_tbl_member_tbl_createId` (`createId`),
  KEY `board_tbl_member_tbl_updateId` (`updateId`),
  KEY `board_tbl_member_tbl_deleteId` (`deleteId`),
  KEY `board_tbl_id_IDX` (`id`,`deleteFlag`) USING BTREE,
  KEY `board_tbl_name_IDX` (`name`,`deleteFlag`) USING BTREE,
  CONSTRAINT `board_tbl_member_tbl_createId` FOREIGN KEY (`createId`) REFERENCES `member_tbl` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `board_tbl_member_tbl_deleteId` FOREIGN KEY (`deleteId`) REFERENCES `member_tbl` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `board_tbl_member_tbl_updateId` FOREIGN KEY (`updateId`) REFERENCES `member_tbl` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci



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


CREATE TABLE `follower_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `follower_id` bigint unsigned NOT NULL,
  `following_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `follower_following_uniq` (`follower_id`,`following_id`),
  KEY `follower_tbl_following_id_fk` (`following_id`),
  CONSTRAINT `follower_tbl_follower_id_fk` FOREIGN KEY (`follower_id`) REFERENCES `member_tbl` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `follower_tbl_following_id_fk` FOREIGN KEY (`following_id`) REFERENCES `member_tbl` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `marker_data_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `boardId` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `marker_data_tbl_boardId_IDX` (`boardId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE verification_code (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL,
    expire_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);
```
