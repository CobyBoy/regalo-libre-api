CREATE TABLE auth0user
(
    auth0_user_id BIGINT       NOT NULL,
    profile_id    BIGINT       NOT NULL,
    name          VARCHAR(100) NOT NULL,
    nickname      VARCHAR(255) NOT NULL,
    picture_url   VARCHAR(255),
    sub           VARCHAR(50)  NOT NULL,
    updated_at    VARCHAR(30)  NOT NULL,
    PRIMARY KEY (auth0_user_id),
    CONSTRAINT chk_nickname_length CHECK (LENGTH(nickname) >= 3),
    CONSTRAINT FK_auth0user_profile FOREIGN KEY (profile_id) REFERENCES profile (profile_id) ON DELETE CASCADE
);

-- Create the unique index
CREATE UNIQUE INDEX UK_idx_unique_profile_id
    ON auth0user (profile_id)
    WHERE profile_id IS NOT NULL;