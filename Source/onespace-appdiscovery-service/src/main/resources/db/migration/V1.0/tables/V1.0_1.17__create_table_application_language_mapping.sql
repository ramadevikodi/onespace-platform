BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'application_language_mapping'
) THEN create table application_language_mapping (
  application_id UUID references application (application_id),
  language_id UUID references language (language_id),
  primary key (application_id, language_id)
);
RAISE NOTICE 'Table "application_language_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "application_language_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
