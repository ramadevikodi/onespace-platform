BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'application_status'
) THEN create table application_status (
  status_id UUID default gen_random_uuid() primary key,
  status_name VARCHAR(50)
);
RAISE NOTICE 'Table "application_status" created successfully.';
ELSE RAISE NOTICE 'Table "application_status" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
