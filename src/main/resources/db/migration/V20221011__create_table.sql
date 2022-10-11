CREATE TABLE users(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(63) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(63) NOT NULL,
    profile VARCHAR(255),
    salt VARCHAR(63),
    refresh_token VARCHAR(255)
);

CREATE TABLE board (
    board_id VARCHAR(63) NOT NULL PRIMARY KEY,
    title VARCHAR(63),
    context VARCHAR(2500) NOT NULL,
    create_at TIMESTAMP,
    hashtag VARCHAR(255),
    count_visit BIGINT,
    count_likes BIGINT,
    likes_id VARCHAR(63),
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE likes (
    likes_id VARCHAR(63) NOT NULL PRIMARY KEY,
    board_id VARCHAR(63),
    FOREIGN KEY(board_id) REFERENCES board(board_id),
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE menu(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(63),
    price BIGINT,
    description VARCHAR(63),
);

CREATE TABLE review(

)


