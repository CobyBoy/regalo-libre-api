-- Create the schema meli if it doesn't exist
DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT 1
                FROM pg_namespace
                WHERE nspname = 'meli'
            ) THEN
            CREATE SCHEMA meli;
            RAISE NOTICE 'Schema meli created.';
        ELSE
            RAISE NOTICE 'Schema meli already exists.';
        END IF;
    END
$$;

-- Create the schema wishlist if it doesn't exist
DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT 1
                FROM pg_namespace
                WHERE nspname = 'wishlist'
            ) THEN
            CREATE SCHEMA wishlist;
            RAISE NOTICE 'Schema wishlist created.';
        ELSE
            RAISE NOTICE 'Schema wishlist already exists.';
        END IF;
    END
$$;

-- Create the schema auth0 if it doesn't exist
DO
$$
    BEGIN
        IF NOT EXISTS(
                SELECT 1
                FROM pg_namespace
                WHERE nspname = 'auth0'
            ) THEN
            CREATE SCHEMA auth0;
            RAISE NOTICE 'Schema auth0 created.';
        ELSE
            RAISE NOTICE 'Schema auth0 already exists.';
        END IF;
    END
$$;
