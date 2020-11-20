-- 创建数据库和用户，username 和 password 可以自行更改 By JuniorRay
use mysql;

CREATE DATABASE `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'username'@'127.0.0.1' IDENTIFIED BY 'password';

GRANT ALL ON *.* TO 'username'@'127.0.0.1';

flush privileges;
