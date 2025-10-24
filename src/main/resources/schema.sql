-- CREATE DATABASE deokhugam;
-- CREATE USER deokhugam WITH ENCRYPTED PASSWORD 'deokhugam1234';

DROP TABLE IF EXISTS review_likes;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS ranks;

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(300) UNIQUE      NOT NULL,
    nickname   VARCHAR(50) UNIQUE       NOT NULL,
    password   VARCHAR(300)             NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    deleted_at timestamp with time zone
);

CREATE TABLE books
(
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(256),
    author        VARCHAR(256),
    description   VARCHAR(1024),
    publisher     VARCHAR(256),
    published_at  date,
    isbn          VARCHAR(1024),
    thumbnail_url VARCHAR(1024),
    created_at    timestamp with time zone NOT NULL,
    updated_at    timestamp with time zone,
    deleted_at    timestamp with time zone
);

CREATE TABLE reviews
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT                   NOT NULL,
    book_id    BIGINT                   NOT NULL,
    content    VARCHAR(1024),
    rating     INT                      NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    deleted_at timestamp with time zone,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id)
        REFERENCES users (id),
    CONSTRAINT fk_reviews_book FOREIGN KEY (book_id)
        REFERENCES books (id),
    CONSTRAINT unique_book_user UNIQUE (book_id, user_id)
);

CREATE TABLE review_likes
(
    id         BIGSERIAL PRIMARY KEY,
    review_id  BIGINT                   NOT NULL,
    user_id    BIGINT                   NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    deleted_at timestamp with time zone,
    CONSTRAINT fk_review_likes_review FOREIGN KEY (review_id)
        REFERENCES reviews (id),
    CONSTRAINT fk_review_likes_user FOREIGN KEY (user_id)
        REFERENCES users (id),
    CONSTRAINT unique_review_user UNIQUE (review_id, user_id)
);

CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    review_id  BIGINT                   NOT NULL,
    user_id    BIGINT                   NOT NULL,
    content    VARCHAR(1024),
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    deleted_at timestamp with time zone,
    CONSTRAINT fk_comments_review FOREIGN KEY (review_id)
        REFERENCES reviews (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id)
        REFERENCES users (id)
);

CREATE TABLE notifications
(
    id         BIGSERIAL PRIMARY KEY,
    review_id  BIGINT                   NOT NULL,
    user_id    BIGINT                   NOT NULL,
    type       VARCHAR(20),
    content    VARCHAR(1024),
    confirmed  BOOLEAN DEFAULT false,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    deleted_at timestamp with time zone,
    CONSTRAINT fk_notifications_review FOREIGN KEY (review_id)
        REFERENCES reviews (id),
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id)
        REFERENCES users (id)
);

CREATE TABLE ranks
(
    id         BIGSERIAL PRIMARY KEY,
    target     VARCHAR(20)              NOT NULL,
    target_id  BIGINT,
    type       VARCHAR(20),
    rank_no    INT,
    score      DECIMAL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    deleted_at timestamp with time zone
);

COMMENT
ON COLUMN notifications.type IS 'REVIEW_LIKED, REVIEW_COMMENTED, REVIEW_RANKED';
COMMENT
ON COLUMN ranks.type IS '일간(DAILY) 주간(WEEKLY) 월간(MONTHLY) 역대(ALL_TIME)';
COMMENT
ON COLUMN ranks.target IS 'BOOK REVIEW USER';