CREATE TABLE bookmark
(
    unique_id       BIGSERIAL PRIMARY KEY,
    meli_id         VARCHAR(255) NOT NULL,
    currency_id     VARCHAR(3),
    permalink       VARCHAR(255),
    price           BIGINT,
    status          VARCHAR(10),
    thumbnail       VARCHAR(255),
    title           VARCHAR(255),
    bookmarked_date VARCHAR(255),

    CONSTRAINT UK_bookmark_meli_id UNIQUE (meli_id)
);
