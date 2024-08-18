USE [regalo-libre-db]

CREATE TABLE meli.bookmark
(
    unique_id       BIGINT IDENTITY (1,1) NOT NULL,
    meli_id         NVARCHAR(255)         NOT NULL,
    currency_id     NVARCHAR(3),
    permalink       NVARCHAR(255),
    price           BIGINT,
    status          VARCHAR(10),
    thumbnail       NVARCHAR(255),
    title           NVARCHAR(255),
    bookmarked_date NVARCHAR(255),

    PRIMARY KEY (unique_id),
    UNIQUE (meli_id),
    CONSTRAINT UK_bookmark_meli_id UNIQUE (meli_id)
);
