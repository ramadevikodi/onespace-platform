BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'application_proposition_mapping'
) THEN create table application_proposition_mapping (
  application_id UUID references application (application_id),
  proposition_id UUID references proposition (proposition_id),
  primary key (application_id, proposition_id)
);
RAISE NOTICE 'Table "application_proposition_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "application_proposition_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
