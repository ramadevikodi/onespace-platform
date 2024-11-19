BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'category'
) THEN create table category (
  category_id UUID default gen_random_uuid() primary key,
  category_name VARCHAR(256),
  description text
);
RAISE NOTICE 'Table "category" created successfully.';
ELSE RAISE NOTICE 'Table "category" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
