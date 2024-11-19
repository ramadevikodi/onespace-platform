BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'action_potential_owners'
) THEN create table action_potential_owners (
  action_id uuid references action (action_id),
  potential_owner uuid
);
RAISE NOTICE 'Table "action_potential_owners" created successfully.';
ELSE RAISE NOTICE 'Table "action_potential_owners" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
