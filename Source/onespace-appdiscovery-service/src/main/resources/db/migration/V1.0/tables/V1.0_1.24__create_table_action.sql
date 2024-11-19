BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'action'
) THEN create table action (
  action_id uuid default gen_random_uuid() primary key,
  initiator uuid,
  datetime timestamp with time zone,
  title varchar(256),
  type varchar(20),
  notify BOOLEAN,
  message varchar(2048),
  completion_datetime timestamp with time zone,
  expiry_datetime timestamp with time zone,
  metadata jsonb,
  related_object varchar(256)
);
RAISE NOTICE 'Table "action" created successfully.';
ELSE RAISE NOTICE 'Table "action" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
