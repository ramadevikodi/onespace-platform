BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'application_market_mapping'
) THEN create table application_market_mapping (
  application_id UUID references application (application_id),
  market_id UUID references market (market_id),
  primary key (application_id, market_id)
);
RAISE NOTICE 'Table "application_market_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "application_market_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
