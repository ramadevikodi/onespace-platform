BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'application_modality_mapping'
) THEN create table application_modality_mapping (
  application_id UUID references application (application_id),
  modality_id UUID references modality (modality_id),
  primary key (application_id, modality_id)
);
RAISE NOTICE 'Table "application_modality_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "application_modality_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
