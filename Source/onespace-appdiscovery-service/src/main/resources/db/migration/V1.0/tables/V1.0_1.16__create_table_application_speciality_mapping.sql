BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'application_speciality_mapping'
) THEN create table application_speciality_mapping (
  application_id UUID references application (application_id),
  speciality_id UUID references speciality (speciality_id),
  primary key (application_id, speciality_id)
);
RAISE NOTICE 'Table "application_speciality_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "application_speciality_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;

