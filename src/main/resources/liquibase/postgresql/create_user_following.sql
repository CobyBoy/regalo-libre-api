CREATE TABLE user_following
(
    follower_auth0_user_id BIGINT,
    followee_auth0_user_id BIGINT,
    followed_date          TIMESTAMP,
    is_following           BOOLEAN,
    is_followed_by         BOOLEAN,
    PRIMARY KEY (follower_auth0_user_id, followee_auth0_user_id),
    CONSTRAINT fk_follower FOREIGN KEY (follower_auth0_user_id) REFERENCES auth0user (auth0_user_id),
    CONSTRAINT fk_followee FOREIGN KEY (followee_auth0_user_id) REFERENCES auth0user (auth0_user_id)
);
