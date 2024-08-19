CREATE TABLE wishlist.profile
(
    profile_id    BIGSERIAL PRIMARY KEY,
    name          VARCHAR(50)  NOT NULL,
    meli_nickname VARCHAR(255) NOT NULL,
    app_nickname  VARCHAR(255) NOT NULL,
    is_private    BOOLEAN,
    biography     VARCHAR(500),
    picture_url   VARCHAR(255),
    CONSTRAINT UK_app_nickname UNIQUE (app_nickname)
);