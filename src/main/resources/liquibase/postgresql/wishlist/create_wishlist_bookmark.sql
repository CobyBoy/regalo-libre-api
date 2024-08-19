CREATE TABLE wishlist.wishlist_bookmark
(
    wishlist_id BIGINT       NOT NULL,
    bookmark_id VARCHAR(255) NOT NULL,
    CONSTRAINT FK_wishlist_bookmark_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlist.wishlist (wishlist_id) ON DELETE CASCADE,
    CONSTRAINT FK_wishlist_bookmark_bookmark FOREIGN KEY (bookmark_id) REFERENCES meli.bookmark (meli_id) ON DELETE CASCADE,
    PRIMARY KEY (wishlist_id, bookmark_id)
);