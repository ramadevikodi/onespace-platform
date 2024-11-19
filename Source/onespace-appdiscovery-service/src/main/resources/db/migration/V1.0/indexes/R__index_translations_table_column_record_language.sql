DO $$
BEGIN
    BEGIN
        CREATE INDEX idx_translations_table_column_record_language
        ON translations (table_name, column_name, record_id, language_code);
    EXCEPTION
      WHEN duplicate_object THEN
      RAISE NOTICE 'Index already exists';
      WHEN others THEN
      RAISE NOTICE 'An error occurred: %', SQLERROR;
    END;
END $$;