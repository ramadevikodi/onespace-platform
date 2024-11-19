BEGIN;
DO $$
BEGIN
    ALTER TABLE action ADD COLUMN source UUID REFERENCES application (application_id);
	ALTER TABLE action RENAME COLUMN completion_datetime TO due_datetime;

    RAISE NOTICE 'Added columns to table "action" successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the table alteration. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;