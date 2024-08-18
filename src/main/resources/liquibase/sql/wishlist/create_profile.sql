USE [regalo-libre-db]
CREATE TABLE wishlist.profile
(
    profile_id    BIGINT IDENTITY (1,1) NOT NULL,
    name          NVARCHAR(50)          NOT NULL,
    meli_nickname NVARCHAR(255)         NOT NULL,
    app_nickname  VARCHAR(255)          NOT NULL,
    is_private    BIT,
    biography     VARCHAR(500),
    picture_url   VARCHAR(255),
    PRIMARY KEY (profile_id),
    CONSTRAINT UK_app_nickname UNIQUE (app_nickname)
);