BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'modality'
) THEN create table modality (
  modality_id UUID default gen_random_uuid() primary key,
  modality_name VARCHAR(256)
);
RAISE NOTICE 'Table "modality" created successfully.';
ELSE RAISE NOTICE 'Table "modality" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
