CREATE TABLE user_following
(
    follower_id    BIGINT,
    followee_id    BIGINT,
    followed_date  TIMESTAMP,
    is_following   BOOLEAN,
    is_followed_by BOOLEAN,
    PRIMARY KEY (follower_id, followee_id),
    CONSTRAINT fk_follower FOREIGN KEY (follower_id) REFERENCES auth0user (auth0_user_id),
    CONSTRAINT fk_followee FOREIGN KEY (followee_id) REFERENCES auth0user (auth0_user_id)
);