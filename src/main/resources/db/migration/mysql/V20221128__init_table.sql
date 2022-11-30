CREATE TABLE user(
    user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(63) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(63) NOT NULL,
    profile VARCHAR(255),
    salt VARCHAR(63),
    refresh_token VARCHAR(255)
);

CREATE TABLE board(
    board_id VARCHAR(63) NOT NULL PRIMARY KEY,
    title VARCHAR(63),
    context LONGTEXT NOT NULL,
    create_at TIMESTAMP,
    hashtag VARCHAR(255),
    photo VARCHAR(255),
    count_visit BIGINT,
    count_likes BIGINT,
    likes_id VARCHAR(63),
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES user(user_id)
);

CREATE TABLE likes(
    likes_id VARCHAR(63) NOT NULL PRIMARY KEY,
    board_id VARCHAR(63),
    FOREIGN KEY(board_id) REFERENCES board(board_id),
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES user(user_id)
);

CREATE TABLE restaurant(
    restaurant_id VARCHAR(63) NOT NULL PRIMARY KEY,
    name VARCHAR(63),
    category VARCHAR(63),
    place_type VARCHAR(63),
    address VARCHAR(255),
    total_grade double,
    x_coordinate VARCHAR(63),
    y_coordinate VARCHAR(63)
);

CREATE TABLE review(
    review_id VARCHAR(63) NOT NULL PRIMARY KEY,
    create_at TIMESTAMP,
    context VARCHAR(255),
    photo VARCHAR(255),
    grade double,
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES user(user_id),
    restaurant_id VARCHAR(63),
    FOREIGN KEY(restaurant_id) REFERENCES restaurant(restaurant_id)
);

CREATE TABLE menu(
    menu_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(63),
    photo VARCHAR(255),
    price BIGINT,
    description VARCHAR(63),
    restaurant_id VARCHAR(63),
    FOREIGN KEY(restaurant_id) REFERENCES restaurant(restaurant_id)
);

CREATE TABLE photo(
    photo_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255),
    restaurant_id VARCHAR(63),
    FOREIGN KEY(restaurant_id) REFERENCES restaurant(restaurant_id)
);

CREATE TABLE travel_course(
    travel_course_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(63),
    course VARCHAR(255)
);

CREATE TABLE gps(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    latitude double,
    longitude double,
    travel_course_id BIGINT,
    FOREIGN KEY(travel_course_id) REFERENCES travel_course(travel_course_id)
);

CREATE TABLE course_detail(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(63),
    context VARCHAR(255),
    travel_course_id BIGINT,
    FOREIGN KEY(travel_course_id) REFERENCES travel_course(travel_course_id)
);

CREATE TABLE wharf(
    wharf_id BIGINT NULL AUTO_INCREMENT PRIMARY KEY,
    wharf_course VARCHAR(63)
);

CREATE TABLE wharf_timetable(
    wharf_timetable_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    departure_time VARCHAR(63),
    month_type VARCHAR(63),
    wharf_id BIGINT,
    FOREIGN KEY(wharf_id) REFERENCES wharf(wharf_id)
);
