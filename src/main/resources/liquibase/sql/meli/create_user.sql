USE [wishlist-db]

CREATE TABLE meli.muser
(
    id          BIGINT        NOT NULL,
    nickname    VARCHAR(100)  NOT NULL,
    first_name  NVARCHAR(100) NOT NULL,
    last_name   NVARCHAR(100) NOT NULL,
    email       NVARCHAR(100),
    picture_id  varchar(100),
    picture_url varchar(255),
    PRIMARY KEY (id)
);