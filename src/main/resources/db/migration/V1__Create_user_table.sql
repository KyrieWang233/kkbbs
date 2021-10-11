CREATE TABLE user
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL ,
    account_id VARCHAR(100),
    name VARCHAR(50),
    avatar_url TEXT,
    password VARCHAR(50),
    salt VARCHAR(200),
    gmt_create BIGINT,
    gmt_modified BIGINT,
    role VARCHAR(30),
    register_way VARCHAR(30)
);