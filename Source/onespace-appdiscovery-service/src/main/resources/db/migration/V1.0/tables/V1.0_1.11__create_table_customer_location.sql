BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'customer_location'
) THEN create table customer_location (
      customer_id UUID references customer (customer_id),
      location    VARCHAR(256),
      site        VARCHAR(256),
      department  VARCHAR(256)
  );
RAISE NOTICE 'Table "customer_location" created successfully.';
ELSE RAISE NOTICE 'Table "customer_location" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
