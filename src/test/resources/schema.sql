-- repertoire.`USER` definition

CREATE TABLE USER
(
    id        bigint NOT NULL AUTO_INCREMENT,
    firstname varchar(100) DEFAULT NULL,
    lastname  varchar(100) DEFAULT NULL,
    email     varchar(100) DEFAULT NULL,
    password  varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;