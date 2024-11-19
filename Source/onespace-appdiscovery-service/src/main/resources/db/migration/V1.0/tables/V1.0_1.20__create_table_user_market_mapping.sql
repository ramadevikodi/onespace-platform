BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'user_market_mapping'
) THEN create table user_market_mapping (
  hsp_user_uuid UUID,
  market_id UUID references market (market_id),
  primary key (hsp_user_uuid, market_id)
);
RAISE NOTICE 'Table "user_market_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "user_market_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
