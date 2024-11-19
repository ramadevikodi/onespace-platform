BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'deployment'
) THEN create table deployment (
  deployment_id UUID default gen_random_uuid() primary key,
  deployment_mode VARCHAR(50)
);
RAISE NOTICE 'Table "deployment" created successfully.';
ELSE RAISE NOTICE 'Table "deployment" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
