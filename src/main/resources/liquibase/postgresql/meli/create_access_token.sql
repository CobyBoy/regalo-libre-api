CREATE TABLE meli.access_token
(
    user_id       BIGINT       NOT NULL,
    access_token  VARCHAR(255) NOT NULL,
    token_type    VARCHAR(15),
    scope         VARCHAR(20),
    expires_in    INTEGER      NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    expires_at    TIMESTAMP(6),
    PRIMARY KEY (user_id)
);