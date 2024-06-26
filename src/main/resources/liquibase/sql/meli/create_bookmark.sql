USE [wishlist-db]

CREATE TABLE meli.bookmark
(
    unique_id       BIGINT IDENTITY (1,1) NOT NULL,
    id              NVARCHAR(255)         NOT NULL,
    currency_id     NVARCHAR(255),
    permalink       NVARCHAR(255),
    price           BIGINT,
    status          VARCHAR(16),
    thumbnail       NVARCHAR(255),
    title           NVARCHAR(255),
    bookmarked_date NVARCHAR(255),
    PRIMARY KEY (unique_id),
    UNIQUE (id),
    CONSTRAINT UK_bookmark_meli_id UNIQUE (id)
);

/*CREATE UNIQUE NONCLUSTERED INDEX UK_product_meli_id
    ON meli.picture (meli_id)
    WHERE meli_id IS NOT NULL;*/
