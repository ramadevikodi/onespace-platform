BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'nononespace_application'
) THEN CREATE TABLE nononespace_application (
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
RAISE NOTICE 'Table "nononespace_application" created successfully.';
ELSE RAISE NOTICE 'Table "nononespace_application" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
