BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'market'
) THEN create table market (
  market_id uuid default gen_random_uuid() primary key,
  market_name varchar(256),
  region varchar(256),
  country varchar(256),
  sub_region varchar(256),
  is_active boolean default true
);
RAISE NOTICE 'Table "market" created successfully.';
ELSE RAISE NOTICE 'Table "market" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
