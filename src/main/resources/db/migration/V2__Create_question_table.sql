CREATE TABLE question
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(50),
  description TEXT,
  gmt_create BIGINT,
  gmt_modified BIGINT,
  creator BIGINT,
  comment_count INT DEFAULT 0,
  view_count INT DEFAULT 0,
  like_count INT DEFAULT 0,
  tag VARCHAR(256)
);