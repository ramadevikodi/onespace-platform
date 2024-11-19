BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'translations'
) THEN create table translations (
  translation_id UUID default gen_random_uuid() primary key,
  table_name VARCHAR(256) not null,
  column_name VARCHAR(256) not null,
  record_id UUID not null,
  language_code VARCHAR(256) not null,
  translated_text text not null,
  unique (
    table_name, column_name, record_id,
    language_code
  )
);
RAISE NOTICE 'Table "translations" created successfully.';
ELSE RAISE NOTICE 'Table "translations" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
