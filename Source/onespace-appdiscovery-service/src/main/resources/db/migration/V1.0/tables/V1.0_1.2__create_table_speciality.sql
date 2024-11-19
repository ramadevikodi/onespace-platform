BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'speciality'
) THEN create table speciality (
  speciality_id UUID default gen_random_uuid() primary key,
  speciality_name VARCHAR(256)
);
RAISE NOTICE 'Table "speciality" created successfully.';
ELSE RAISE NOTICE 'Table "speciality" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
