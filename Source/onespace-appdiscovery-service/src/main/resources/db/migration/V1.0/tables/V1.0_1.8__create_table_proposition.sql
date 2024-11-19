BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'proposition'
) THEN create table proposition (
  proposition_id UUID default gen_random_uuid() primary key,
  proposition_name VARCHAR(256) unique,
  description VARCHAR(512) constraint proposition_name_not_null check (
    not (
      proposition_name is null
      or proposition_name = ''
    )
  ),
  is_active boolean default true
);
RAISE NOTICE 'Table "proposition" created successfully.';
ELSE RAISE NOTICE 'Table "proposition" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
