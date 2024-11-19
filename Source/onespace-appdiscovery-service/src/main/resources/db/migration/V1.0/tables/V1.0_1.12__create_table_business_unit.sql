BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'business_unit'
) THEN create table business_unit (
  business_unit_id UUID default gen_random_uuid() primary key,
  cluster VARCHAR(256),
  segment VARCHAR(256),
  business_unit_name VARCHAR(256),
  description VARCHAR(512)
);
RAISE NOTICE 'Table "business_unit" created successfully.';
ELSE RAISE NOTICE 'Table "business_unit" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
