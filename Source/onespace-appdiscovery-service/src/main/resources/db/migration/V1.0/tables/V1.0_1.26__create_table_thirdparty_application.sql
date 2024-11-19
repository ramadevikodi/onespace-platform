BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'thirdparty_application'
) THEN CREATE TABLE thirdparty_application (
	id uuid,
	"name" varchar(256),
	description text,
	url varchar(256),
	status varchar(50),
	app_order int4,
	registered_by varchar(256),
	registered_datetime timestamptz,
	icon varchar(256) NULL,
	customer_id UUID references customer (customer_id)
);
RAISE NOTICE 'Table "thirdparty_application" created successfully.';
ELSE RAISE NOTICE 'Table "thirdparty_application" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
