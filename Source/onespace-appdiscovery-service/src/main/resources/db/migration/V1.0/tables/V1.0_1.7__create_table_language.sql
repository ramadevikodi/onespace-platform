BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'language'
) THEN create table language (
  language_id UUID default gen_random_uuid() primary key,
  language_code VARCHAR(256),
  description VARCHAR(512),
  date_format VARCHAR(256),
  time_format VARCHAR(256),
  currency_format VARCHAR(256)
);
RAISE NOTICE 'Table "language" created successfully.';
ELSE RAISE NOTICE 'Table "language" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
