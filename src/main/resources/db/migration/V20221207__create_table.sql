CREATE TABLE user(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(63) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(63) NOT NULL,
    profile VARCHAR(255),
    salt VARCHAR(63),
    refresh_token VARCHAR(255)
);

CREATE TABLE board(
    id VARCHAR(63) NOT NULL PRIMARY KEY,
    title VARCHAR(63) NOT NULL,
    context LONGTEXT NOT NULL,
    create_at TIMESTAMP,
    hashtag VARCHAR(255),
    photo VARCHAR(255),
    count_visit BIGINT,
    count_likes BIGINT,
    likes_id VARCHAR(63),
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE board_comment(
    id VARCHAR(63) NOT NULL PRIMARY KEY,
    context LONGTEXT NOT NULL,
    create_at TIMESTAMP,
    user_id BIGINT,
    board_id VARCHAR(63),
    FOREIGN KEY(board_id) REFERENCES board(id),
    FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE likes(
    id VARCHAR(63) NOT NULL PRIMARY KEY,
    board_id VARCHAR(63),
    user_id BIGINT,
    FOREIGN KEY(board_id) REFERENCES board(id),
    FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE restaurant(
    id VARCHAR(63) NOT NULL PRIMARY KEY,
    name VARCHAR(63),
    category VARCHAR(63),
    place_type VARCHAR(63),
    address VARCHAR(255),
    total_grade DOUBLE,
    x_coordinate VARCHAR(63),
    y_coordinate VARCHAR(63)
);

CREATE TABLE review(
    id VARCHAR(63) NOT NULL PRIMARY KEY,
    create_at TIMESTAMP,
    context VARCHAR(255),
    photo VARCHAR(255),
    grade DOUBLE,
    user_id BIGINT,
    restaurant_id VARCHAR(63),
    FOREIGN KEY(user_id) REFERENCES user(id),
    FOREIGN KEY(restaurant_id) REFERENCES restaurant(id)
);

CREATE TABLE menu(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(63),
    photo VARCHAR(255),
    price TINYINT,
    description VARCHAR(255),
    restaurant_id VARCHAR(63),
    FOREIGN KEY(restaurant_id) REFERENCES restaurant(id)
);

CREATE TABLE photo(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255),
    restaurant_id VARCHAR(63),
    FOREIGN KEY(restaurant_id) REFERENCES restaurant(id)
);

CREATE TABLE travel_course(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(63),
    course VARCHAR(255)
);

CREATE TABLE travel_place(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    place_name VARCHAR(255) NOT NULL,
    photo VARCHAR(255),
    intro VARCHAR(255),
    context LONGTEXT
);

CREATE TABLE gps(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE,
    longitude DOUBLE,
    travel_course_id BIGINT,
    travel_place_id BIGINT,
    FOREIGN KEY(travel_place_id) REFERENCES travel_place(id),
    FOREIGN KEY(travel_course_id) REFERENCES travel_course(id)
);

CREATE TABLE harbor(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    harbor_name VARCHAR(63) NOT NULL
);

CREATE TABLE harbor_timetable(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    destination VARCHAR(63) NOT NULL,
    period VARCHAR(255) NOT NULL,
    operating_time VARCHAR(255) NOT NULL,
    harbor_id BIGINT,
    FOREIGN KEY(harbor_id) REFERENCES harbor(id)
);

CREATE TABLE course_detail(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(63),
    context VARCHAR(255),
    travel_course_id BIGINT,
    FOREIGN KEY(travel_course_id) REFERENCES travel_course(id)
);

CREATE TABLE ship_fare(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    age_group VARCHAR(63) NOT NULL,
    round_trip TINYINT,
    enter_island TINYINT,
    leave_island TINYINT,
    harbor_id BIGINT,
    FOREIGN KEY(harbor_id) REFERENCES harbor(id)
);