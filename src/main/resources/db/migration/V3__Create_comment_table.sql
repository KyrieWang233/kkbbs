CREATE TABLE comment
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  parent_id BIGINT NOT NULL,
  type INT NOT NULL,
  comment_creator BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  receiver_name VARCHAR(50),
  gmt_create BIGINT NOT NULL,
  gmt_modified BIGINT NOT NULL,
  like_count BIGINT DEFAULT 0,
  comment_count INT DEFAULT 0,
  content VARCHAR(1024)

);