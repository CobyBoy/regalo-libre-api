CREATE TABLE user_bookmark
(
    user_id BIGINT       NOT NULL,
    meli_id VARCHAR(255) NOT NULL,
    CONSTRAINT FK_user_bookmark_auth0user FOREIGN KEY (user_id) REFERENCES auth0user (auth0_user_id) ON DELETE CASCADE,
    CONSTRAINT FK_userbookmark_bookmark FOREIGN KEY (meli_id) REFERENCES bookmark (meli_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, meli_id)
);