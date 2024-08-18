USE [regalo-libre-db]

CREATE TABLE meli.access_token
(
    user_id       BIGINT       NOT NULL,
    access_token  VARCHAR(255) NOT NULL,
    token_type    VARCHAR(10),
    scope         VARCHAR(10),
    expires_in    INT          NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    expires_at    datetime2(6)
        PRIMARY KEY (user_id)
);