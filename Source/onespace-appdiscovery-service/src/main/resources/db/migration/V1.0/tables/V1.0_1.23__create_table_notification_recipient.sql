BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'notification_recipient'
) THEN CREATE TABLE notification_recipient (
  id uuid,
  notification_id uuid references app_notification (id),
  recipient_id uuid,
  "read" bool
);
RAISE NOTICE 'Table "notification_recipient" created successfully.';
ELSE RAISE NOTICE 'Table "notification_recipient" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
