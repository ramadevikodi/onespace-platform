BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'app_notification'
) THEN create table app_notification (
  id UUID default gen_random_uuid() primary key,
  title character varying(256),
  message text,
  category character varying(256),
  source UUID references application (application_id),
  created_at timestamp with time zone,
  expiry timestamp with time zone
);
RAISE NOTICE 'Table "app_notification" created successfully.';
ELSE RAISE NOTICE 'Table "app_notification" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
