CREATE TABLE "USER" (
    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstname varchar(100) DEFAULT NULL,
    lastname  varchar(100) DEFAULT NULL,
    email     varchar(100) DEFAULT NULL,
    password  varchar(100) DEFAULT NULL
);