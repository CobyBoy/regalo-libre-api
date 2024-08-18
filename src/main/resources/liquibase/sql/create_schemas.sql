-- Create the schema meli
IF NOT EXISTS(SELECT 1
              FROM sys.schemas
              WHERE name = 'meli')
    BEGIN
        -- Create the schema if it does not exist
        EXEC ('CREATE SCHEMA meli;');
        PRINT 'Schema meli created.';
    END
ELSE
    BEGIN
        PRINT 'Schema meli already exists.';
    END

-- Create the schema wishlist
IF NOT EXISTS(SELECT 1
              FROM sys.schemas
              WHERE name = 'wishlist')
    BEGIN
        -- Create the schema if it does not exist
        EXEC ('CREATE SCHEMA wishlist;');
        PRINT 'Schema wishlist created.';
    END
ELSE
    BEGIN
        PRINT 'Schema wishlist already exists.';
    END

-- Create the schema wishlist
IF NOT EXISTS(SELECT 1
              FROM sys.schemas
              WHERE name = 'auth0')
    BEGIN
        -- Create the schema if it does not exist
        EXEC ('CREATE SCHEMA auth0;');
        PRINT 'Schema auth0 created.';
    END
ELSE
    BEGIN
        PRINT 'Schema auth0 already exists.';
    END
