BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'business_unit_external_system_mapping'
) THEN create table business_unit_external_system_mapping (
  business_unit_id UUID references business_unit (business_unit_id) primary key,
  hsp_iam_org_id uuid,
  sap_account_id VARCHAR(255)
);
RAISE NOTICE 'Table "business_unit_external_system_mapping" created successfully.';
ELSE RAISE NOTICE 'Table "business_unit_external_system_mapping" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
