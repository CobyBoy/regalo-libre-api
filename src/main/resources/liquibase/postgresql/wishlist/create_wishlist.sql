CREATE TABLE wishlist.wishlist
(
    wishlist_id   BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    auth0_user_id BIGINT       NOT NULL,
    private_id    UUID         NOT NULL,
    public_id     VARCHAR(255) NOT NULL,
    name          VARCHAR(50)  NOT NULL,
    description   VARCHAR(300),
    is_private    BOOLEAN,
    created_at    TIMESTAMP WITH TIME ZONE,
    updated_at    TIMESTAMP WITH TIME ZONE,
    CONSTRAINT FK_wishlist_auth0user FOREIGN KEY (auth0_user_id) REFERENCES auth0.auth0user (auth0_user_id),
    CONSTRAINT UK_private_id UNIQUE (private_id),
    CONSTRAINT UK_public_id UNIQUE (public_id),
    CONSTRAINT UK_unique_list_name_per_user UNIQUE (name, auth0_user_id)
);