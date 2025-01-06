# mysql
```
CREATE TABLE `member_tbl` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `loginId` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `nickname` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1,
  `createDt` varchar(20) COLLATE utf8mb4_general_ci NULL,
  `createId` varchar(20) COLLATE utf8mb4_general_ci NULL,
  `updateDt` varchar(20) COLLATE utf8mb4_general_ci NULL,
  `updateId` varchar(20) COLLATE utf8mb4_general_ci NULL,
  `deleteDt` varchar(20) COLLATE utf8mb4_general_ci NULL,
  `deleteId` varchar(20) COLLATE utf8mb4_general_ci NULL,
  `deleteFlag` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_tbl_uniq_loginId` (`loginId`),
  UNIQUE KEY `member_tbl_uniq_nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```
