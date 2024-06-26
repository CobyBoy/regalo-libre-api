USE [wishlist-db]
-- User-Bookmarked Products Many-to-Many Relationship
CREATE TABLE meli.user_bookmark
(
    user_id BIGINT       NOT NULL,
    meli_id varchar(255) NOT NULL,
    CONSTRAINT FK_userproduct_user FOREIGN KEY (user_id) REFERENCES meli.muser (id),
    CONSTRAINT FK_userproduct_product FOREIGN KEY (meli_id) REFERENCES meli.bookmark (id),
    PRIMARY KEY (user_id, meli_id)
);