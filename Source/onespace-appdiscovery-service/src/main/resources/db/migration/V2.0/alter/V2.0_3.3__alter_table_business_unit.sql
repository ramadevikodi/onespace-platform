BEGIN;
DO $$
BEGIN
    ALTER TABLE business_unit ALTER COLUMN description TYPE text;

    RAISE NOTICE 'Added columns to table "action" successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the table alteration. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;