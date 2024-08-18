USE [regalo-libre-db]
CREATE TABLE wishlist.wishlist
(
    wishlist_id   BIGINT IDENTITY (1,1) NOT NULL,
    auth0_user_id BIGINT                NOT NULL,
    private_id    UNIQUEIDENTIFIER      NOT NULL,
    public_id     VARCHAR(255)          NOT NULL,
    name          NVARCHAR(50)          NOT NULL,
    description   NVARCHAR(300)         NULL,
    is_private    BIT,
    created_at    DATETIME2(6),
    updated_at    DATETIME2(6),
    PRIMARY KEY (wishlist_id),
    CONSTRAINT FK_wishlist_auth0user FOREIGN KEY (auth0_user_id) REFERENCES auth0.auth0user (auth0_user_id),
    CONSTRAINT UK_private_id UNIQUE (private_id),
    CONSTRAINT UK_public_id UNIQUE (public_id),
    CONSTRAINT UK_unique_list_name_per_user UNIQUE (name, auth0_user_id)
);
--CREATE UNIQUE INDEX idx_wishlists_privateId ON wishlist.wishlist (privateId);