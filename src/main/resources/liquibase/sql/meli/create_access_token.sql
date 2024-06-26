USE [wishlist-db]

CREATE TABLE meli.access_token
(
    user_id      BIGINT NOT NULL,
    access_token VARCHAR(255),
    expires_in   INT    NOT NULL,
    scope        VARCHAR(10),
    token_type   VARCHAR(10),
    PRIMARY KEY (user_id)
);