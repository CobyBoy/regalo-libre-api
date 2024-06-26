USE [wishlist-db]
CREATE TABLE wishlist.wishlist
(
    id          BIGINT IDENTITY (1,1) NOT NULL,
    user_id     BIGINT                NOT NULL,
    privateId   UNIQUEIDENTIFIER      NOT NULL,
    publicId    VARCHAR(255)          NOT NULL,
    name        NVARCHAR(50)          NOT NULL,
    description NVARCHAR(300)         NULL,
    isPrivate   BIT,
    createdAt   DATETIME2(6),
    PRIMARY KEY (id),
    CONSTRAINT FK_wishlists_user FOREIGN KEY (user_id) REFERENCES meli.muser (id),
    CONSTRAINT UK_wishlists_private_id UNIQUE (privateId),
    CONSTRAINT UK_wishlists_public_id UNIQUE (publicId)
);
--CREATE UNIQUE INDEX idx_wishlists_privateId ON wishlist.wishlist (privateId);