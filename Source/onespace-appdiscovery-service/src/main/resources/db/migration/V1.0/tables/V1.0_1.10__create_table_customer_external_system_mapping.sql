BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'customer_external_system_mapping'
) THEN create table customer_external_system_mapping (
  customer_id UUID references customer (customer_id) primary key,
  hsp_iam_org_id uuid,
  sap_account_id VARCHAR(255)
);
RAISE NOTICE 'Table "customer_external_system_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "customer_external_system_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
