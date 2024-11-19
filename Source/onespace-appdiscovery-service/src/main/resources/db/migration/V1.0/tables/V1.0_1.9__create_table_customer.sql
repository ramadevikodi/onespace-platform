BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'customer'
) THEN create table customer (
  customer_id UUID default gen_random_uuid() primary key,
  customer_name VARCHAR(256),
  description VARCHAR(512),
  address VARCHAR(256),
  email_contact VARCHAR(256),
  is_active boolean default true,
  market_id UUID references market (market_id)
);
RAISE NOTICE 'Table "customer" created successfully.';
ELSE RAISE NOTICE 'Table "customer" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
