BEGIN;
DO $$
BEGIN
    ALTER TABLE speciality ADD COLUMN description text;

    RAISE NOTICE 'Added columns to table "speciality" successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the table alteration. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;