USE [wishlist-db]
-- Wishlist-Product Many-to-Many Relationship
CREATE TABLE wishlist.wishlist_bookmark
(
    wishlist_id BIGINT NOT NULL,
    bookmark_id BIGINT NOT NULL,
    CONSTRAINT FK_wishlist_bookmark_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlist.wishlist (id),
    CONSTRAINT FK_wishlist_bookmark_bookmark FOREIGN KEY (bookmark_id) REFERENCES meli.bookmark (id),
    PRIMARY KEY (wishlist_id, bookmark_id)
);
