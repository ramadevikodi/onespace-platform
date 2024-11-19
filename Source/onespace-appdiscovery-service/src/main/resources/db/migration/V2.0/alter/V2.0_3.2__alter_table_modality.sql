BEGIN;
DO $$
BEGIN
    ALTER TABLE modality ADD COLUMN description text;

    RAISE NOTICE 'Added columns to table "modality" successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the table alteration. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;